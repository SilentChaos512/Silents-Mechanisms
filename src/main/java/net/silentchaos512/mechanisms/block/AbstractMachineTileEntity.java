package net.silentchaos512.mechanisms.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.mechanisms.capability.EnergyStorageImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public abstract class AbstractMachineTileEntity<R extends IRecipe<?>> extends LockableSidedInventoryTileEntity implements IEnergyHandler, ITickableTileEntity {
    @SyncVariable(name = "Progress")
    protected int progress;
    @SyncVariable(name = "ProcessTime")
    protected int processTime;

    private final EnergyStorageImpl energy;

    protected final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return progress;
                case 1:
                    return processTime;
                case 2:
                    return getEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
                case 1:
                    processTime = value;
                    break;
                case 2:
                    setEnergyStoredDirectly(value);
                    break;
            }
        }

        @Override
        public int size() {
            return 3;
        }
    };

    protected AbstractMachineTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract) {
        super(typeIn, inventorySize);
        this.energy = new EnergyStorageImpl(maxEnergy, maxReceive, maxExtract, this);
    }

    protected abstract int getEnergyUsedPerTick();

    protected abstract BlockState getActiveState();

    protected abstract BlockState getInactiveState();

    protected abstract int[] getOutputSlots();

    @Nullable
    protected abstract R getRecipe();

    protected abstract int getProcessTime(R recipe);

    protected abstract Collection<ItemStack> getProcessResults(R recipe);

    public int getProgress() {
        return progress;
    }

    public int getProcessTime() {
        return processTime;
    }

    protected void sendUpdate() {
        if (world == null) return;
        sendUpdate(world.getBlockState(pos));
    }

    protected void sendUpdate(BlockState newState) {
        if (world == null) return;
        world.notifyBlockUpdate(pos, world.getBlockState(pos), newState, 3);
    }

    protected void setInactiveState() {
        if (progress > 0) {
            progress = 0;
            sendUpdate(getInactiveState());
        }
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        R recipe = getRecipe();
        if (recipe != null && getEnergyStored() >= getEnergyUsedPerTick() && hasRoomInOutput(getProcessResults(recipe))) {
            // Process
            processTime = getProcessTime(recipe);
            ++progress;
            energy.consumeEnergy(getEnergyUsedPerTick());

            if (progress >= processTime) {
                // Create result
                getProcessResults(recipe).forEach(this::storeResultItem);
                consumeIngredients(recipe);
                progress = 0;
            }

            sendUpdate(getActiveState());
        } else if (recipe == null) {
            setInactiveState();
        }
    }

    private boolean hasRoomInOutput(Iterable<ItemStack> results) {
        for (ItemStack stack : results) {
            if (!hasRoomForOutputItem(stack)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasRoomForOutputItem(ItemStack stack) {
        for (int i : getOutputSlots()) {
            ItemStack output = getStackInSlot(i);
            if (canItemsStack(stack, output)) {
                return true;
            }
        }
        return false;
    }

    protected static boolean canItemsStack(ItemStack a, ItemStack b) {
        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    private void storeResultItem(ItemStack stack) {
        // Merge the item into any output slot it can fit in
        for (int i : getOutputSlots()) {
            ItemStack output = getStackInSlot(i);
            if (canItemsStack(stack, output)) {
                if (output.isEmpty()) {
                    setInventorySlotContents(i, stack);
                } else {
                    output.setCount(output.getCount() + stack.getCount());
                }
                return;
            }
        }
    }

    protected void consumeIngredients(R recipe) {
        decrStackSize(0, 1);
    }

    @Override
    public EnergyStorageImpl getEnergyImpl() {
        return energy;
    }

    @Override
    public void read(CompoundNBT tags) {
        super.read(tags);
        SyncVariable.Helper.readSyncVars(this, tags);
        readEnergy(tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
        writeEnergy(tags);
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        SyncVariable.Helper.readSyncVars(this, packet.getNbtCompound());
        readEnergy(packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
        writeEnergy(tags);
        return tags;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && cap == CapabilityEnergy.ENERGY) {
            return getEnergy(side).cast();
        }
        return super.getCapability(cap, side);
    }
}
