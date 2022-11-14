package net.silentchaos512.mechanisms.utls;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.ModRecipes;

public class MechanismsUtils {
    private MechanismsUtils() {
        throw new IllegalStateException("Can't Initialize Utility Class");
    }

    public static <T extends Recipe<?>> RecipeType<T> makeRecipeType(String name) {
        RecipeType<T> recipeType = RecipeType.simple(SilentsMechanisms.location(name));
        ModRecipes.ALL_RECIPE_TYPES.add(recipeType);
        return recipeType;
    }

    public static <T> ResourceLocation getRegistryName(IForgeRegistry<T> registry, T object) {
        return registry.getKey(object);
    }
}
