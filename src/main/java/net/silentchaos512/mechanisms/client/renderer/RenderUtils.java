package net.silentchaos512.mechanisms.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

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

        TextureAtlasSprite icon = getFluidTexture(stack);
        if (icon == null) {
            return;
        }

        int renderAmount = (int) Math.max(Math.min(height, amount * height / tankCapacity), 1);
        int posY = (int) (y + height - renderAmount);

        Minecraft.getInstance().getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS);
        int color = stack.getFluid().getAttributes().getColor();
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        RenderSystem.color3f(r, g, b);

        RenderSystem.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {
                int drawWidth = (int) Math.min(width - i, 16);
                int drawHeight = Math.min(renderAmount - j, 16);

                int drawX = (int) (x + i);
                int drawY = posY + j;

                float minU = icon.getU0();
                float maxU = icon.getU1();
                float minV = icon.getV0();
                float maxV = icon.getV1();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder tes = tessellator.getBuilder();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                tes.vertex(drawX, drawY + drawHeight, 0).uv(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.vertex(drawX + drawWidth, drawY + drawHeight, 0).uv(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
                tes.vertex(drawX + drawWidth, drawY, 0).uv(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
                tes.vertex(drawX, drawY, 0).uv(minU, minV).endVertex();
                tessellator.end();
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.color3f(1, 1, 1);
    }

    @Nullable
    public static TextureAtlasSprite getFluidTexture(FluidStack stack) {
        TextureAtlasSprite[] sprites = ForgeHooksClient.getFluidSprites(Minecraft.getInstance().level, BlockPos.ZERO, stack.getFluid().defaultFluidState());
        return sprites.length > 0 ? sprites[0] : null;
    }
}
