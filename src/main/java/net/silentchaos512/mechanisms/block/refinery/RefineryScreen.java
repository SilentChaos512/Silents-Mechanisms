package net.silentchaos512.mechanisms.block.refinery;

import com.mojang.blaze3d.platform.GlStateManager;
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
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        if (isPointInRegion(28, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(TextUtil.fluidWithMax(container.getFluidInTank(0), RefineryTileEntity.TANK_CAPACITY).getFormattedText(), mouseX, mouseY);
        }
        if (isPointInRegion(68, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(TextUtil.fluidWithMax(container.getFluidInTank(1), RefineryTileEntity.TANK_CAPACITY).getFormattedText(), mouseX, mouseY);
        }
        if (isPointInRegion(84, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(TextUtil.fluidWithMax(container.getFluidInTank(2), RefineryTileEntity.TANK_CAPACITY).getFormattedText(), mouseX, mouseY);
        }
        if (isPointInRegion(100, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(TextUtil.fluidWithMax(container.getFluidInTank(3), RefineryTileEntity.TANK_CAPACITY).getFormattedText(), mouseX, mouseY);
        }
        if (isPointInRegion(116, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(TextUtil.fluidWithMax(container.getFluidInTank(4), RefineryTileEntity.TANK_CAPACITY).getFormattedText(), mouseX, mouseY);
        }
        if (isPointInRegion(153, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(TextUtil.energyWithMax(container.getEnergyStored(), container.getMaxEnergyStored()).getFormattedText(), mouseX, mouseY);
        }
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (minecraft == null) return;
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        blit(xPos, yPos, 0, 0, this.xSize, this.ySize);

        // Progress arrow
        int progress = container.getProgress();
        int processTime = container.getProcessTime();
        int length = processTime > 0 && progress > 0 && progress < processTime ? progress * 24 / processTime : 0;
        blit(xPos + 43, yPos + 35, 176, 14, length + 1, 16);

        // Energy meter
        int energyBarHeight = container.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Tanks
        RenderUtils.renderGuiTank(container.getFluidInTank(0), RefineryTileEntity.TANK_CAPACITY, xPos + 29, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(container.getFluidInTank(1), RefineryTileEntity.TANK_CAPACITY, xPos + 69, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(container.getFluidInTank(2), RefineryTileEntity.TANK_CAPACITY, xPos + 85, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(container.getFluidInTank(3), RefineryTileEntity.TANK_CAPACITY, xPos + 101, yPos + 18, 0, 12, 50);
        RenderUtils.renderGuiTank(container.getFluidInTank(4), RefineryTileEntity.TANK_CAPACITY, xPos + 117, yPos + 18, 0, 12, 50);
    }
}
