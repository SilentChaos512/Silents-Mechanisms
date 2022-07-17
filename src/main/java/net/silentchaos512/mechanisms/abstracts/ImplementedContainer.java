package net.silentchaos512.mechanisms.abstracts;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ImplementedContainer extends Container, MenuProvider {
    NonNullList<ItemStack> getItemContainer();
    
    @Override
    default int getContainerSize() {
        return getItemContainer().size();
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default ItemStack getItem(int pSlot) {
        return getItemContainer().get(pSlot);
    }

    @Override
    default ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(getItemContainer(), pSlot, pAmount);
    }

    @Override
    default ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(getItemContainer(), pSlot);
    }

    @Override
    default void setItem(int pSlot, ItemStack pStack) {
        getItemContainer().set(pSlot, pStack);
    }

    @Override
    default boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    default void clearContent() {
        getItemContainer().clear();
    }
}
