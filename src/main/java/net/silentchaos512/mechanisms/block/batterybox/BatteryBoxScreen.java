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
        this.func_230459_a_(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void func_230459_a_(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (isPointInRegion(153, 17, 13, 51, mouseX, mouseY)) {
            ITextComponent text = TextUtil.energyWithMax(container.getEnergyStored(), container.getTileEntity().getMaxEnergyStored());
            renderTooltip(matrixStack, text, mouseX, mouseY);
        }
        super.func_230459_a_(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        if (minecraft == null) return;
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        blit(matrixStack, xPos, yPos, 0, 0, this.xSize, this.ySize);

        // Energy meter
        int energyBarHeight = 50 * container.getEnergyStored() / container.tileEntity.getMaxEnergyStored();
        if (energyBarHeight > 0) {
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Debug text
//        int y = 5;
//        for (String line : container.tileEntity.getDebugText()) {
//            font.drawString(line, 5, y, 0xFFFFFF);
//            y += 10;
//        }
    }
}
