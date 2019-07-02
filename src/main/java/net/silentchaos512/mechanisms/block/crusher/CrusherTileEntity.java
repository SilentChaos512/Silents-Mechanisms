package net.silentchaos512.mechanisms.block.crusher;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.mechanisms.block.IEnergyHandler;
import net.silentchaos512.mechanisms.capability.EnergyStorageImpl;
import net.silentchaos512.mechanisms.crafting.recipe.CrushingRecipe;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class CrusherTileEntity extends LockableSidedInventoryTileEntity implements IEnergyHandler, ITickableTileEntity {
    // Energy constant
    public static final int MAX_ENERGY = 100_000;
    public static final int MAX_RECEIVE = 1_000;
    public static final int ENERGY_USED_PER_TICK = 25;

    // Inventory constants
    static final int INPUT_SLOT_COUNT = 1;
    static final int OUTPUT_SLOT_COUNT = 4;
    static final int INVENTORY_SIZE = INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT;
    private static final int[] SLOTS_INPUT = {0};
    private static final int[] SLOTS_OUTPUT = IntStream.range(INPUT_SLOT_COUNT, INVENTORY_SIZE).toArray();
    private static final int[] SLOTS_ALL = IntStream.range(0, INVENTORY_SIZE).toArray();

    @Getter
    @SyncVariable(name = "Progress")
    private int progress;
    @Getter
    @SyncVariable(name = "ProcessTime")
    private int processTime;

    private final EnergyStorageImpl energy;

    final IIntArray fields = new IIntArray() {
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

    public CrusherTileEntity() {
        super(ModTileEntities.crusher, INVENTORY_SIZE);
        energy = new EnergyStorageImpl(MAX_ENERGY, MAX_RECEIVE, 0, this);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        CrushingRecipe recipe = world.getRecipeManager().getRecipe(CrushingRecipe.RECIPE_TYPE, this, world).orElse(null);
        if (recipe != null && hasRoomInOutput(recipe) && getEnergyStored() >= ENERGY_USED_PER_TICK) {
            // Process
            processTime = recipe.getProcessTime();
            ++progress;
            energy.consumeEnergy(ENERGY_USED_PER_TICK);

            if (progress > processTime) {
                // Create results
                createAndStoreResults(recipe);
                consumeIngredients(recipe);
                progress = 0;
            }

            sendUpdate();
        } else if (recipe == null) {
            setNeutralState();
        }
    }

    private void sendUpdate() {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    private void setNeutralState() {
        boolean update = false;
        if (progress > 0) {
            progress = 0;
            update = true;
        }
        if (update) {
            sendUpdate();
        }
    }

    private boolean hasRoomInOutput(CrushingRecipe recipe) {
        // Determine if all possible results could fit
        Set<ItemStack> results = recipe.getPossibleResults(this);
        for (ItemStack stack : results) {
            if (!hasRoomForOutputItem(stack)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasRoomForOutputItem(ItemStack stack) {
        // Determine if the item can fit in any output slot
        for (int i : SLOTS_OUTPUT) {
            ItemStack output = getStackInSlot(i);
            if (canItemsStack(stack, output)) {
                return true;
            }
        }
        return false;
    }

    private static boolean canItemsStack(ItemStack a, ItemStack b) {
        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    private void createAndStoreResults(CrushingRecipe recipe) {
        // Create results and store them in the output slots (assumes there is room)
        recipe.getResults(this).forEach(this::storeResultItem);
    }

    private void storeResultItem(ItemStack stack) {
        // Merge the item into any output slot it can fit in
        for (int i : SLOTS_OUTPUT) {
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

    private void consumeIngredients(CrushingRecipe recipe) {
        decrStackSize(0, 1);
    }

    @Override
    public EnergyStorageImpl getEnergyImpl() {
        return energy;
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP)
            return SLOTS_INPUT;
        if (side == Direction.DOWN)
            return SLOTS_OUTPUT;
        return SLOTS_ALL;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index < INPUT_SLOT_COUNT;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index >= INPUT_SLOT_COUNT;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "crusher");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new CrusherContainer(id, playerInventory, this);
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

    List<String> getDebugText() {
        return ImmutableList.of(
                "progress = " + progress,
                "processTime = " + processTime,
                "energy = " + getEnergyStored() + " FE / " + getMaxEnergyStored() + " FE",
                "ENERGY_USED_PER_TICK = " + ENERGY_USED_PER_TICK,
                "MAX_RECEIVE = " + MAX_RECEIVE
        );
    }
}
