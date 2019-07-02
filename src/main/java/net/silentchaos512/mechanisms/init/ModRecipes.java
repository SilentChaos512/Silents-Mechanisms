package net.silentchaos512.mechanisms.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.crafting.recipe.CrushingRecipe;

public class ModRecipes {
    public static void init() {
        registerType("crushing", CrushingRecipe.RECIPE_TYPE);
        registerSerializer("crushing", CrushingRecipe.SERIALIZER);
    }

    private static void registerSerializer(String name, IRecipeSerializer<?> serializer) {
        IRecipeSerializer.register(SilentMechanisms.getId(name).toString(), serializer);
    }

    private static void registerType(String name, IRecipeType<?> recipeType) {
        Registry.register(Registry.RECIPE_TYPE, SilentMechanisms.getId(name), recipeType);
    }
}
