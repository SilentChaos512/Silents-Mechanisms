package net.silentchaos512.mechanisms.block.generator.lava;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseScreen;
import net.silentchaos512.mechanisms.client.renderer.RenderUtils;
import net.silentchaos512.mechanisms.util.TextUtil;

public class LavaGeneratorScreen extends AbstractMachineBaseScreen<LavaGeneratorContainer> {
    public static final ResourceLocation TEXTURE = SilentMechanisms.getId("textures/gui/fluid_generator.png");

    public LavaGeneratorScreen(LavaGeneratorContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
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
        if (isHovering(135, 17, 13, 51, x, y)) {
            ITextComponent text = TextUtil.fluidWithMax(menu.getFluidInTank(), LavaGeneratorTileEntity.TANK_CAPACITY);
            renderTooltip(matrixStack, text, x, y);
        }
        if (isHovering(153, 17, 13, 51, x, y)) {
            ITextComponent text = TextUtil.energyWithMax(menu.getEnergyStored(), menu.getMaxEnergyStored());
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

        // Energy meter
        int energyBarHeight = menu.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Fluid tank
        RenderUtils.renderGuiTank(menu.getFluidInTank(), LavaGeneratorTileEntity.TANK_CAPACITY, xPos + 136, yPos + 18, 0, 12, 50);
    }
}
