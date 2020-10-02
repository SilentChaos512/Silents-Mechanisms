package net.silentchaos512.mechanisms.block.mixer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseScreen;
import net.silentchaos512.mechanisms.client.renderer.RenderUtils;
import net.silentchaos512.mechanisms.util.TextUtil;

public class MixerScreen extends AbstractMachineBaseScreen<MixerContainer> {
    public static final ResourceLocation TEXTURE = SilentMechanisms.getId("textures/gui/mixer.png");

    public MixerScreen(MixerContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
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
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
        if (isPointInRegion(28, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(container.getFluidInTank(0), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isPointInRegion(44, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(container.getFluidInTank(1), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isPointInRegion(60, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(container.getFluidInTank(2), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isPointInRegion(76, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(container.getFluidInTank(3), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isPointInRegion(116, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(container.getFluidInTank(4), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isPointInRegion(153, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.energyWithMax(container.getEnergyStored(), container.getMaxEnergyStored()), x, y);
        }
        super.renderHoveredTooltip(matrixStack, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);

        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;

        // Progress arrow
        int progress = container.getProgress();
        int processTime = container.getProcessTime();
        int length = processTime > 0 && progress > 0 && progress < processTime ? progress * 24 / processTime : 0;
        blit(matrixStack, xPos + 92, yPos + 35, 176, 14, length + 1, 16);

        // Energy meter
        int energyBarHeight = container.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Tanks
        RenderUtils.renderGuiTank(container.getFluidInTank(0), MixerTileEntity.TANK_CAPACITY, xPos + 29, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(container.getFluidInTank(1), MixerTileEntity.TANK_CAPACITY, xPos + 45, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(container.getFluidInTank(2), MixerTileEntity.TANK_CAPACITY, xPos + 61, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(container.getFluidInTank(3), MixerTileEntity.TANK_CAPACITY, xPos + 77, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(container.getFluidInTank(4), MixerTileEntity.TANK_CAPACITY, xPos + 117, yPos + 18, 0, 12, 50);
    }
}
