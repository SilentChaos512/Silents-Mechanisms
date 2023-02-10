package net.silentchaos512.mechanisms.common.abstracts;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class BaseMenuScreen<T extends BaseMenuContainer> extends AbstractContainerScreen<T> {
    public BaseMenuScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        super.inventoryLabelX = pMenu.playerInventoryX;
        super.inventoryLabelY = pMenu.playerInventoryY - 14;

        super.titleLabelY = 8;
        super.titleLabelX = 6;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    public boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        return super.isHovering(pX, pY, pWidth, pHeight, pMouseX, pMouseY);
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
        super.renderTooltip(pPoseStack, pX, pY);
    }
}
