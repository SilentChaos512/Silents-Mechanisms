package net.silentchaos512.mechanisms.block;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.lib.tile.SyncVariable;

import javax.annotation.Nullable;
import java.util.Collection;

public abstract class AbstractMachineTileEntity<R extends IRecipe<?>> extends AbstractEnergyInventoryTileEntity {
    @SyncVariable(name = "Progress")
    protected int progress;
    @SyncVariable(name = "ProcessTime")
    protected int processTime;

    protected final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return AbstractMachineTileEntity.this.progress;
                case 1:
                    return AbstractMachineTileEntity.this.processTime;
                case 2:
                    return AbstractMachineTileEntity.this.getEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    AbstractMachineTileEntity.this.progress = value;
                    break;
                case 1:
                    AbstractMachineTileEntity.this.processTime = value;
                    break;
                case 2:
                    AbstractMachineTileEntity.this.setEnergyStoredDirectly(value);
                    break;
            }
        }

        @Override
        public int size() {
            return 3;
        }
    };

    protected AbstractMachineTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract) {
        super(typeIn, inventorySize, maxEnergy, maxReceive, maxExtract);
    }

    protected abstract int getEnergyUsedPerTick();

    protected BlockState getActiveState(BlockState currentState) {
        return currentState.with(AbstractFurnaceBlock.LIT, true);
    }

    protected BlockState getInactiveState(BlockState currentState) {
        return currentState.with(AbstractFurnaceBlock.LIT, false);
    }

    protected abstract int[] getOutputSlots();

    @Nullable
    protected abstract R getRecipe();

    protected abstract int getProcessTime(R recipe);

    protected abstract Collection<ItemStack> getProcessResults(R recipe);

    protected void sendUpdate(BlockState newState) {
        if (world == null) return;
        BlockState oldState = world.getBlockState(pos);
        if (oldState != newState) {
            world.setBlockState(pos, newState, 3);
            world.notifyBlockUpdate(pos, oldState, newState, 3);
        }
    }

    protected void setInactiveState() {
        if (world == null) return;
        if (progress > 0) {
            progress = 0;
            sendUpdate(getInactiveState(world.getBlockState(pos)));
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

                if (getRecipe() == null) {
                    // Nothing left to process
                    setInactiveState();
                } else {
                    // Continue processing next output
                    progress = 0;
                }
            } else {
                sendUpdate(getActiveState(world.getBlockState(pos)));
            }
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
    public IIntArray getFields() {
        return fields;
    }
}
