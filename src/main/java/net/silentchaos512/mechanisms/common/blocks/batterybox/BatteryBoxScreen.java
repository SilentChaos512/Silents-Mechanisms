package net.silentchaos512.mechanisms.common.blocks.batterybox;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.silentchaos512.mechanisms.common.abstracts.BaseMenuScreen;
import net.silentchaos512.mechanisms.utls.MenuUtils;
import net.silentchaos512.mechanisms.utls.TranslateUtils;

public class BatteryBoxScreen extends BaseMenuScreen<BatteryBoxMenu> {
    public BatteryBoxScreen(BatteryBoxMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        MenuUtils.renderMenuScreen(this, pPoseStack, "battery_box.png", 176, 166);

        int energyBarHeight = menu.getEnergyBar();
        if (energyBarHeight > 0) {
            super.blit(pPoseStack, leftPos + 154, topPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
        super.renderTooltip(pPoseStack, pX, pY);
        MenuUtils.renderToolTip(
                this,
                pPoseStack,
                TranslateUtils.translateEnergyWithMax(menu.getData().get(0), menu.getData().get(1)),
                pX, pY,
                153, 17, 13, 52
        );
    }
}
