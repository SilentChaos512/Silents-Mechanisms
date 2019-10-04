package net.silentchaos512.mechanisms.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.silentchaos512.mechanisms.crafting.recipe.*;
import net.silentchaos512.mechanisms.util.Constants;

public final class ModRecipes {
    private ModRecipes() {}

    public static void init() {
        registerType(Constants.ALLOY_SMELTING, AlloySmeltingRecipe.RECIPE_TYPE);
        registerType(Constants.COMPRESSING, CompressingRecipe.RECIPE_TYPE);
        registerType(Constants.CRUSHING, CrushingRecipe.RECIPE_TYPE);
        registerType(Constants.DRYING, DryingRecipe.RECIPE_TYPE);
        registerSerializer(Constants.ALLOY_SMELTING, AlloySmeltingRecipe.SERIALIZER);
        registerSerializer(Constants.COMPRESSING, CompressingRecipe.SERIALIZER);
        registerSerializer(Constants.CRUSHING, CrushingRecipe.SERIALIZER);
        registerSerializer(Constants.DRYING, DryingRecipe.SERIALIZER);
        registerSerializer(Constants.MIXING, MixingRecipe.SERIALIZER);
        registerSerializer(Constants.REFINING, RefiningRecipe.SERIALIZER);
        registerSerializer(Constants.SOLIDIFYING, SolidifyingRecipe.SERIALIZER);
    }

    private static void registerSerializer(ResourceLocation name, IRecipeSerializer<?> serializer) {
        IRecipeSerializer.register(name.toString(), serializer);
    }

    private static void registerType(ResourceLocation name, IRecipeType<?> recipeType) {
        Registry.register(Registry.RECIPE_TYPE, name, recipeType);
    }
}
