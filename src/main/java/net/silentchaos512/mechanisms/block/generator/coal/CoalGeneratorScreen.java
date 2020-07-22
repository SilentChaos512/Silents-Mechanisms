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
        this.func_230459_a_(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void func_230459_a_(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (isPointInRegion(153, 17, 13, 51, mouseX, mouseY)) {
            ITextComponent text = TextUtil.energyWithMax(container.getEnergyStored(), container.tileEntity.getMaxEnergyStored());
            renderTooltip(matrixStack, text, mouseX, mouseY);
        }
        super.func_230459_a_(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        super.func_230450_a_(matrixStack, partialTicks, mouseX, mouseY);
        
        if (minecraft == null) return;
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;

        // Fuel remaining
        if (container.isBurning()) {
            int height = getFlameIconHeight();
            blit(matrixStack, xPos + 81, yPos + 53 + 12 - height, 176, 12 - height, 14, height + 1);
        }

        // Energy meter
        int energyBarHeight = container.getEnergyBarHeight();
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
        int total = container.getTotalBurnTime();
        if (total == 0) total = 200;
        return container.getBurnTime() * 13 / total;
    }
}
