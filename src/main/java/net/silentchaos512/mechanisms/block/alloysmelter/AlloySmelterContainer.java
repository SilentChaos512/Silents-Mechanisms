package net.silentchaos512.mechanisms.block.alloysmelter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.silentchaos512.lib.inventory.SlotOutputOnly;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.block.AbstractMachineContainer;
import net.silentchaos512.mechanisms.block.AbstractMachineTileEntity;
import net.silentchaos512.mechanisms.init.MachineType;
import net.silentchaos512.mechanisms.util.MachineTier;

public class AlloySmelterContainer extends AbstractMachineContainer<AlloySmelterTileEntity> {
    public AlloySmelterContainer(int id, PlayerInventory playerInventory, MachineTier tier) {
        this(id, playerInventory, MachineType.ALLOY_SMELTER.create(tier), new IntArray(AbstractMachineTileEntity.FIELDS_COUNT));
    }

    protected AlloySmelterContainer(int id, PlayerInventory playerInventory, AlloySmelterTileEntity tileEntityIn, IIntArray fieldsIn) {
        super(MachineType.ALLOY_SMELTER.getContainerType(tileEntityIn.getMachineTier()), id, tileEntityIn, fieldsIn);

        for (int i = 0; i < AlloySmelterTileEntity.INPUT_SLOT_COUNT; ++i) {
            this.addSlot(new Slot(this.tileEntity, i, 17 + 18 * i, 35));
        }
        this.addSlot(new SlotOutputOnly(this.tileEntity, AlloySmelterTileEntity.INPUT_SLOT_COUNT, 126, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
        this.addUpgradeSlots();
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = this.tileEntity.getSizeInventory();
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index == inventorySize - 1) {
                if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= inventorySize) {
                if (this.isAlloySmeltingIngredient(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, inventorySize - 1, false)) {
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

    private boolean isAlloySmeltingIngredient(ItemStack stack) {
        // TODO
        return true;
    }
}
