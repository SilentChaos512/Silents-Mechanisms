package net.silentchaos512.mechanisms.block.refinery;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseScreen;
import net.silentchaos512.mechanisms.client.renderer.RenderUtils;
import net.silentchaos512.mechanisms.util.TextUtil;

public class RefineryScreen extends AbstractMachineBaseScreen<RefineryContainer> {
    public static final ResourceLocation TEXTURE = SilentMechanisms.getId("textures/gui/refinery.png");

    public RefineryScreen(RefineryContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
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
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(0), RefineryTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(68, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(1), RefineryTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(84, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(2), RefineryTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(100, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(3), RefineryTileEntity.TANK_CAPACITY), x, y);
        }
        if (isHovering(116, 17, 13, 51, x, y)) {
            renderTooltip(matrixStack, TextUtil.fluidWithMax(menu.getFluidInTank(4), RefineryTileEntity.TANK_CAPACITY), x, y);
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
        blit(matrixStack, xPos + 43, yPos + 35, 176, 14, length + 1, 16);

        // Energy meter
        int energyBarHeight = menu.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Tanks
        RenderUtils.renderGuiTank(menu.getFluidInTank(0), RefineryTileEntity.TANK_CAPACITY, xPos + 29, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(menu.getFluidInTank(1), RefineryTileEntity.TANK_CAPACITY, xPos + 69, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(menu.getFluidInTank(2), RefineryTileEntity.TANK_CAPACITY, xPos + 85, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(menu.getFluidInTank(3), RefineryTileEntity.TANK_CAPACITY, xPos + 101, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(menu.getFluidInTank(4), RefineryTileEntity.TANK_CAPACITY, xPos + 117, yPos + 18, 0, 12, 50);
    }
}
