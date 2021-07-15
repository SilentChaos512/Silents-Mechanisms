package net.silentchaos512.mechanisms.block.batterybox;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.util.TextUtil;

public class BatteryBoxScreen extends ContainerScreen<BatteryBoxContainer> {
    public static final ResourceLocation TEXTURE = SilentMechanisms.getId("textures/gui/battery_box.png");

    public BatteryBoxScreen(BatteryBoxContainer containerIn, PlayerInventory playerInventoryIn, ITextComponent titleIn) {
        super(containerIn, playerInventoryIn, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int x, int y) {
        if (isHovering(153, 17, 13, 51, x, y)) {
            ITextComponent text = TextUtil.energyWithMax(menu.getEnergyStored(), menu.getTileEntity().getMaxEnergyStored());
            renderTooltip(matrixStack, text, x, y);
        }
        super.renderTooltip(matrixStack, x, y);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (minecraft == null) return;
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(TEXTURE);
        int xPos = (this.width - this.imageWidth) / 2;
        int yPos = (this.height - this.imageHeight) / 2;
        blit(matrixStack, xPos, yPos, 0, 0, this.imageWidth, this.imageHeight);

        // Energy meter
        int energyBarHeight = 50 * menu.getEnergyStored() / menu.tileEntity.getMaxEnergyStored();
        if (energyBarHeight > 0) {
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }
    }
}
