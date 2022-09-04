package net.silentchaos512.mechanisms.compat.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.mechanisms.SilentsMechanisms;

public record BackgroundHelper(ResourceLocation textureLocation, int width, int height) {
    public static ResourceLocation makeGui(String guiName) {
        return SilentsMechanisms.location("textures/jei/" + guiName + ".png");
    }

    public IDrawable createBackground(IGuiHelper guiHelper) {
        return guiHelper.createDrawable(textureLocation, 0, 0, width, height);
    }
}
