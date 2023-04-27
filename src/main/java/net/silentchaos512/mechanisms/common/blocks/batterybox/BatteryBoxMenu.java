package net.silentchaos512.mechanisms.common.blocks.batterybox;

import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.mechanisms.common.abstracts.AbstractEnergyMenu;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.init.ModMenus;
import net.silentchaos512.mechanisms.utls.MenuUtils;

public class BatteryBoxMenu extends AbstractEnergyMenu {
    private static final int CONTAINER_SIZE = BatteryBoxBlockEntity.CONTAINER_SIZE;
    private final ContainerLevelAccess access;
    public BatteryBoxMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, playerInventory, new SimpleContainer(BatteryBoxBlockEntity.CONTAINER_SIZE), new SimpleContainerData(4), ContainerLevelAccess.NULL);
    }

    public BatteryBoxMenu(int pContainerId, Inventory inventory, Container container, ContainerData data, ContainerLevelAccess access) {
        super(ModMenus.BATTERY_BOX, pContainerId, inventory, container, ModBlocks.BATTERY_BOX, data);
        this.access = access;


        this.addSlot(new BatterySlot(container, 0, 71, 19));
        this.addSlot(new BatterySlot(container, 1, 71, 37));
        this.addSlot(new BatterySlot(container, 2, 71, 55));
        this.addSlot(new BatterySlot(container, 3, 89, 19));
        this.addSlot(new BatterySlot(container, 4, 89, 37));
        this.addSlot(new BatterySlot(container, 5, 89, 55));


        MenuUtils.putPlayerInventory(this, inventory, 8, 84);

        super.addDataSlots(data);
    }

    public final int getEnergyBar() {

        int capacity = super.getData().get(1);
        int energyClamp = Mth.clamp(super.getData().get(0), 0, capacity);

        return capacity > 0 ? 50 * energyClamp / capacity : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if (pIndex < CONTAINER_SIZE) {
                if (!super.moveItemStackTo(itemStack1, CONTAINER_SIZE, super.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack1, itemStack);
            } else if (pIndex < super.slots.size()) {
                if (itemStack1.is(ModItems.BATTERY)) {
                    if (!super.moveItemStackTo(itemStack1, 0, CONTAINER_SIZE, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemStack1);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, this.blockAccess);
    }

    private static final class BatterySlot extends Slot {
        public BatterySlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return pStack.is(ModItems.BATTERY);
        }
    }
}
