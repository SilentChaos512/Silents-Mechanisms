package net.silentchaos512.mechanisms.block.generator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseContainer;

public abstract class AbstractFluidGeneratorContainer<T extends AbstractFluidFuelGeneratorTileEntity> extends AbstractMachineBaseContainer<T> {
    public AbstractFluidGeneratorContainer(ContainerType<?> type, int id, T tileEntityIn, IIntArray fieldsIn) {
        super(type, id, tileEntityIn, fieldsIn);
    }

    public int getBurnTime() {
        return fields.get(5);
    }

    @SuppressWarnings("deprecation") // Use of Registry
    public FluidStack getFluidInTank() {
        int fluidId = this.fields.get(7);
        Fluid fluid = Registry.FLUID.byId(fluidId);
        int amount = this.fields.get(8);
        return new FluidStack(fluid, amount);
    }

    protected abstract boolean isFuel(ItemStack stack);

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int inventorySize = 2;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index != 0) {
                if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerInventoryEnd) {
                    if (!this.moveItemStackTo(itemstack1, playerInventoryEnd, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerHotbarEnd && !this.moveItemStackTo(itemstack1, inventorySize, playerInventoryEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
