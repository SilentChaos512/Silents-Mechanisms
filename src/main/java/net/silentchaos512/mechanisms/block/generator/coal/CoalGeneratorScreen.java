package net.silentchaos512.mechanisms.block.generator.coal;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseScreen;
import net.silentchaos512.mechanisms.util.TextUtil;

public class CoalGeneratorScreen extends AbstractMachineBaseScreen<CoalGeneratorContainer> {
    public static final ResourceLocation TEXTURE = SilentMechanisms.getId("textures/gui/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);
    }

    @Override
    public ResourceLocation getGuiTexture() {
        return TEXTURE;
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
            ITextComponent text = TextUtil.energyWithMax(menu.getEnergyStored(), menu.tileEntity.getMaxEnergyStored());
            renderTooltip(matrixStack, text, x, y);
        }
        super.renderTooltip(matrixStack, x, y);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);

        if (minecraft == null) return;
        int xPos = (this.width - this.imageWidth) / 2;
        int yPos = (this.height - this.imageHeight) / 2;

        // Fuel remaining
        if (menu.isBurning()) {
            int height = getFlameIconHeight();
            blit(matrixStack, xPos + 81, yPos + 53 + 12 - height, 176, 12 - height, 14, height + 1);
        }

        // Energy meter
        int energyBarHeight = menu.getEnergyBarHeight();
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

    private int getFlameIconHeight() {
        int total = menu.getTotalBurnTime();
        if (total == 0) total = 200;
        return menu.getBurnTime() * 13 / total;
    }
}
