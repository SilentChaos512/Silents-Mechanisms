package net.silentchaos512.mechanisms.api.crafting.recipe.fluid;

import net.minecraft.util.ResourceLocation;

@FunctionalInterface
public interface IFluidRecipeType<T extends IFluidRecipe<?>> {
    ResourceLocation getTypeName();
}
