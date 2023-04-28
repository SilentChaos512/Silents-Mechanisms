package net.silentchaos512.mechanisms.common.abstracts;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseMenuContainer extends AbstractContainerMenu {
    public final Inventory playerInventory;
    public final ContainerLevelAccess access = ContainerLevelAccess.NULL;
    public final Container container;
    public final ContainerData data;
    protected final Block blockAccess;
    //Used as coordinate for AUTO inventory label pos calculating and rendering in screen
    public int playerInventoryX;
    public int playerInventoryY;

    public BaseMenuContainer(@Nonnull MenuType<?> pMenuType, int pContainerId, Inventory playerInventory, Container container, Block blockAccess, ContainerData data) {
        super(pMenuType, pContainerId);
        this.playerInventory = playerInventory;
        this.container = container;
        this.blockAccess = blockAccess;

        this.data = data;
        super.addDataSlots(this.data);
    }

    public void addSlot(int index, Inventory inventory, int x, int y) {
        super.addSlot(new Slot(inventory, index, x, y));
    }

    protected void addSlot(int index, int x, int y) {
        super.addSlot(new Slot(this.container, index, x, y));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(access, pPlayer, blockAccess);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        container.stopOpen(pPlayer);
    }

    @Override
    public boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        return super.moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
    }

    @Override
    public abstract ItemStack quickMoveStack(Player player, int index);
}
