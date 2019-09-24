package net.silentchaos512.mechanisms.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.lwjgl.opengl.GL11;

public final class RenderUtils {
    private RenderUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static void renderGuiTank(IFluidHandler fluidHandler, int tank, double x, double y, double zLevel, double width, double height) {
        FluidStack stack = fluidHandler.getFluidInTank(tank);
        int tankCapacity = fluidHandler.getTankCapacity(tank);
        renderGuiTank(stack, tankCapacity, x, y, zLevel, width, height);
    }

    public static void renderGuiTank(FluidStack stack, int tankCapacity, double x, double y, double zLevel, double width, double height) {
        // Adapted from Ender IO
        int amount = stack.getAmount();
        if (stack.getFluid() == null || amount <= 0) {
            return;
        }

        ResourceLocation stillTexture = stack.getFluid().getAttributes().getStillTexture();
        TextureAtlasSprite icon = Minecraft.getInstance().getTextureMap().getSprite(stillTexture);

        int renderAmount = (int) Math.max(Math.min(height, amount * height / tankCapacity), 1);
        int posY = (int) (y + height - renderAmount);

        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.color3f(1, 1, 1);

        GlStateManager.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {
                int drawWidth = (int) Math.min(width - i, 16);
                int drawHeight = Math.min(renderAmount - j, 16);

                int drawX = (int) (x + i);
                int drawY = posY + j;

                double minU = icon.getMinU();
                double maxU = icon.getMaxU();
                double minV = icon.getMinV();
                double maxV = icon.getMaxV();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder tes = tessellator.getBuffer();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                tes.pos(drawX, drawY + drawHeight, 0).tex(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.pos(drawX + drawWidth, drawY + drawHeight, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.pos(drawX + drawWidth, drawY, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
                tes.pos(drawX, drawY, 0).tex(minU, minV).endVertex();
                tessellator.draw();
            }
        }
        GlStateManager.disableBlend();
        GlStateManager.color3f(1, 1, 1);
    }
}
