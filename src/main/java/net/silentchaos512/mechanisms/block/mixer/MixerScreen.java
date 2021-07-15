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
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int x, int y) {
        if (isHovering(28, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(0), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(44, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(1), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(60, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(2), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(76, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(3), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(116, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(4), MixerTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(153, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.energyWithMax(menu.getEnergyStored(), menu.getMaxEnergyStored()), x, y);
        }
        super.renderTooltip(matrixStack, x, y);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);

        int xPos = (this.width - this.imageWidth) / 2;
        int yPos = (this.height - this.imageHeight) / 2;

        // Progress arrow
        int progress = menu.getProgress();
        int processTime = menu.getProcessTime();
        int length = processTime > 0 && progress > 0 && progress < processTime ? progress * 24 / processTime : 0;
        blit(matrixStack, xPos + 92, yPos + 35, 176, 14, length + 1, 16);

        // Energy meter
        int energyBarHeight = menu.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Tanks
        RenderUtils.renderGuiTank(menu.getFluidInTank(0), MixerTileEntity.TANK_CAPACITY, xPos + 29, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(menu.getFluidInTank(1), MixerTileEntity.TANK_CAPACITY, xPos + 45, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(menu.getFluidInTank(2), MixerTileEntity.TANK_CAPACITY, xPos + 61, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(menu.getFluidInTank(3), MixerTileEntity.TANK_CAPACITY, xPos + 77, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(menu.getFluidInTank(4), MixerTileEntity.TANK_CAPACITY, xPos + 117, yPos + 18, 0, 12, 50);
    }
}
