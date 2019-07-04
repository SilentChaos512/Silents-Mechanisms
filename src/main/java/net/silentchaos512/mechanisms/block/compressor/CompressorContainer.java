package net.silentchaos512.mechanisms.block.compressor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.silentchaos512.lib.inventory.SlotOutputOnly;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.init.ModContainers;

public class CompressorContainer extends Container {
    final CompressorTileEntity tileEntity;

    public CompressorContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new CompressorTileEntity(), new IntArray(3));
    }

    public CompressorContainer(int id, PlayerInventory playerInventory, CompressorTileEntity tileEntity, IIntArray fieldsIn) {
        super(ModContainers.electricFurnace, id);
        this.tileEntity = tileEntity;

        this.addSlot(new Slot(this.tileEntity, 0, 56, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 1, 117, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

        func_216961_a(fieldsIn);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        // TODO
        return true;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 1) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 0) {
                if (this.isCompressingIngredient(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 29) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
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

    private boolean isCompressingIngredient(ItemStack stack) {
        // TODO
        return true;
    }
}
