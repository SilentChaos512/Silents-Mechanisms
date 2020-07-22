package net.silentchaos512.mechanisms.block.pump;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseScreen;
import net.silentchaos512.mechanisms.block.refinery.RefineryTileEntity;
import net.silentchaos512.mechanisms.client.renderer.RenderUtils;
import net.silentchaos512.mechanisms.util.TextUtil;

public class PumpScreen extends AbstractMachineBaseScreen<PumpContainer> {
    private static final ResourceLocation TEXTURE = SilentMechanisms.getId("textures/gui/pump.png");

    public PumpScreen(PumpContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
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
        if (isPointInRegion(135, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(container.getFluidInTank(), RefineryTileEntity.TANK_CAPACITY), mouseX, mouseY);
        }
        if (isPointInRegion(153, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(matrixStack, TextUtil.energyWithMax(container.getEnergyStored(), container.getMaxEnergyStored()), mouseX, mouseY);
        }
        super.func_230459_a_(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        super.func_230450_a_(matrixStack, partialTicks, mouseX, mouseY);

        if (minecraft == null) return;
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;

        // Energy meter
        int energyBarHeight = container.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Tank
        RenderUtils.renderGuiTank(container.getFluidInTank(), RefineryTileEntity.TANK_CAPACITY, xPos + 136, yPos + 18, 0, 12, 50);
    }
}
