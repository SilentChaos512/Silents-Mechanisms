package net.silentchaos512.mechanisms.block.electricfurnace;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.silentchaos512.lib.inventory.SlotOutputOnly;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.block.AbstractMachineContainer;
import net.silentchaos512.mechanisms.init.ModContainers;

public class ElectricFurnaceContainer extends AbstractMachineContainer<ElectricFurnaceTileEntity> {
    public ElectricFurnaceContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new ElectricFurnaceTileEntity(), new IntArray(3));
    }

    public ElectricFurnaceContainer(int id, PlayerInventory playerInventory, ElectricFurnaceTileEntity tileEntity, IIntArray fieldsIn) {
        super(ModContainers.electricFurnace, id, tileEntity, fieldsIn);

        this.addSlot(new Slot(this.tileEntity, 0, 56, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 1, 117, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = 2;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index == 1) {
                if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 0) {
                if (this.isSmeltingIngredient(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerInventoryEnd) {
                    if (!this.mergeItemStack(itemstack1, playerInventoryEnd, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerHotbarEnd && !this.mergeItemStack(itemstack1, inventorySize, playerInventoryEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean isSmeltingIngredient(ItemStack stack) {
        // TODO
        return true;
    }
}
