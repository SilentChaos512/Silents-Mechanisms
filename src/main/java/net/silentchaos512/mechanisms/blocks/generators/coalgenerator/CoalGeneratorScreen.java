package net.silentchaos512.mechanisms.blocks.generators.coalgenerator;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.silentchaos512.mechanisms.abstracts.BaseMenuScreen;
import net.silentchaos512.mechanisms.utls.MenuUtils;
import net.silentchaos512.mechanisms.utls.TranslateUtils;

public class CoalGeneratorScreen extends BaseMenuScreen<CoalGeneratorMenu> {
    public CoalGeneratorScreen(CoalGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(PoseStack pose, float pPartialTick, int pMouseX, int pMouseY) {
        MenuUtils.renderMenuScreen(this, pose, "coal_generator.png", 176, 166);
        if (menu.isWorking()) {
            super.blit(pose, leftPos + 81, topPos + 53 + 12 - menu.getBurnProcess(), 176, 12 - menu.getBurnProcess(), 14, menu.getBurnProcess() + 1);
        }

        int energyBarHeight = menu.getEnergyBar();
        if (energyBarHeight > 0) {
            super.blit(pose, leftPos + 154, topPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }
    }


    @Override
    public void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
        Component energyWithMax = TranslateUtils.translateEnergyWithMax(menu.getEnergy(), menu.getEnergyCapacity());
        MenuUtils.renderToolTip(this, pPoseStack,energyWithMax, pX, pY, 153, 17, 13, 52);
        super.renderTooltip(pPoseStack, pX, pY);
    }
}
