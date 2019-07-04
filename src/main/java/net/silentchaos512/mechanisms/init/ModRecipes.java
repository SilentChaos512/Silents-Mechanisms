package net.silentchaos512.mechanisms.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.silentchaos512.mechanisms.crafting.recipe.CompressingRecipe;
import net.silentchaos512.mechanisms.crafting.recipe.CrushingRecipe;
import net.silentchaos512.mechanisms.util.Constants;

public class ModRecipes {
    public static void init() {
        registerType(Constants.COMPRESSING, CompressingRecipe.RECIPE_TYPE);
        registerType(Constants.CRUSHING, CrushingRecipe.RECIPE_TYPE);
        registerSerializer(Constants.COMPRESSING, CompressingRecipe.SERIALIZER);
        registerSerializer(Constants.CRUSHING, CrushingRecipe.SERIALIZER);
    }

    private static void registerSerializer(ResourceLocation name, IRecipeSerializer<?> serializer) {
        IRecipeSerializer.register(name.toString(), serializer);
    }

    private static void registerType(ResourceLocation name, IRecipeType<?> recipeType) {
        Registry.register(Registry.RECIPE_TYPE, name, recipeType);
    }
}
