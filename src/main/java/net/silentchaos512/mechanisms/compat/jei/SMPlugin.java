package net.silentchaos512.mechanisms.compat.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.compat.jei.categories.BasicRecipeCategory;
import net.silentchaos512.mechanisms.compat.jei.categories.RackDryingCategory;

import java.util.List;

@JeiPlugin
public class SMPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_ID = SilentsMechanisms.location("sm_jei_plugin");
    private static List<BasicRecipeCategory<?>> BASIC_RECIPE_CATEGORIES;

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        final IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        BASIC_RECIPE_CATEGORIES = ImmutableList.of(
                new RackDryingCategory(guiHelper)
        );
        registration.addRecipeCategories(BASIC_RECIPE_CATEGORIES.toArray(new BasicRecipeCategory<?>[0]));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null)
            BASIC_RECIPE_CATEGORIES.forEach(category -> category.addRecipes(registration, level.getRecipeManager()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        BASIC_RECIPE_CATEGORIES.forEach(basicRecipeCategory -> basicRecipeCategory.addRecipeCatalysts(registration));
    }
}
