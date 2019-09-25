package net.silentchaos512.mechanisms.util.color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.ModList;

import java.util.List;

/**
 * TODO: Currently just leaning on JEI for this...
 */
public final class ColorGetter {
    private ColorGetter() {}

    public static int getColor(Fluid fluid) {
        if (fluid == Fluids.WATER) {
            return 0x0094FF;
        }
        if (ModList.get().isLoaded("jei")) {
            TextureAtlasSprite[] sprites = ForgeHooksClient.getFluidSprites(Minecraft.getInstance().world, BlockPos.ZERO, fluid.getDefaultState());
            if (sprites.length > 0) {
                return getColor(sprites[0]);
            }
        }
        return 0xFFFFFF;
    }

    public static int getColor(TextureAtlasSprite sprite) {
        if (ModList.get().isLoaded("jei")) {
            List<Integer> colors = mezz.jei.color.ColorGetter.getColors(sprite, 0xFFFFFF, 1);
            return colors.isEmpty() ? 0xFFFFFF : colors.get(0);
        }
        return 0xFFFFFF;
    }
}
