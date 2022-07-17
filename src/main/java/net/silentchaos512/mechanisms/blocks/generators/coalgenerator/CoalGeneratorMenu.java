package net.silentchaos512.mechanisms.blocks.generators.coalgenerator;

import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.silentchaos512.mechanisms.abstracts.BaseMenuContainer;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModMenus;
import net.silentchaos512.mechanisms.utls.MenuUtils;

public final class CoalGeneratorMenu extends BaseMenuContainer {
    public CoalGeneratorMenu(int pContainerId, Inventory playerInventory, Container blockEntity, ContainerData data) {
        super(ModMenus.COAL_GENERATOR_MENU, pContainerId, playerInventory, blockEntity, ModBlocks.COAL_GENERATOR, data);

        super.addSlot(0, 80, 33);
        MenuUtils.putPlayerInventory(this, playerInventory, 8, 84);
    }

    public CoalGeneratorMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, playerInventory, new SimpleContainer(1), new SimpleContainerData(4));
    }

    public int getEnergyBar() {
        int capacity = data.get(1);
        int energyClamp = Mth.clamp(data.get(0), 0, capacity);

        return capacity > 0 ? 50 * energyClamp / capacity : 0;
    }

    public int getBurnProcess() {
        int duration = data.get(3) == 0 ? 200 : data.get(3);
        return data.get(2) * 13 / duration;
    }

    public boolean isWorking() {
        return data.get(2) > 0;
    }

    public int getEnergy() {
        return data.get(0);
    }

    public int getEnergyCapacity() {
        return data.get(1);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();

            if (index < 1) {
                if (!super.moveItemStackTo(itemStack1, 1, super.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (canItemBurnt(itemStack1) && !super.moveItemStackTo(itemStack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
        }

        return itemStack;
    }

    private boolean canItemBurnt(ItemStack item) {
        return ForgeHooks.getBurnTime(item, null)  > 0;
    }
}
