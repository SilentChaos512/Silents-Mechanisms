package net.silentchaos512.mechanisms.utls;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.common.abstracts.BaseMenuContainer;
import net.silentchaos512.mechanisms.common.abstracts.BaseMenuScreen;

public class MenuUtils {
    //U.C
    private MenuUtils() {
    }

    public static void putPlayerInventory(BaseMenuContainer container, Inventory inventory, int topLeftX, int topLeftY) {

        container.playerInventoryX = topLeftX;
        container.playerInventoryY = topLeftY;

        //inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                container.addSlot(j + i * 9 + 9, inventory, topLeftX + j * 18, topLeftY + i * 18);
            }
        }


        //hotbar
        for (int k = 0; k < 9; ++k) {
            container.addSlot(k, inventory, topLeftX + k * 18, topLeftY + 58);
        }

        container.waitingForInventory = false;
    }

    public static void renderMenuScreen(AbstractContainerScreen<?> screen, PoseStack pose, String imageLoc, int width, int height) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SilentsMechanisms.location("textures/gui/" + imageLoc));
        screen.blit(pose, screen.getGuiLeft(), screen.getGuiTop(), 0, 0, width, height);
    }

    public static void renderToolTip(BaseMenuScreen<?> screen, PoseStack pose, Component component, int mouseX, int mouseY, int x, int y, int width, int height) {
        if (screen.isHovering(x, y, width, height, mouseX, mouseY)) {
            screen.renderTooltip(pose, component, mouseX, mouseY);
        }
    }

}
