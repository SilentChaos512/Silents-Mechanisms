package net.silentchaos512.mechanisms.api.crafting.recipe.fluid;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface IFluidRecipeSerializer<T extends IFluidRecipe<?>> {
    ResourceLocation getId();

    T read(ResourceLocation recipeId, JsonObject json);

    @Nullable
    T read(ResourceLocation recipeId, PacketBuffer buffer);

    void write(PacketBuffer buffer, T recipe);

    static void register(IFluidRecipeSerializer<?> serializer) {
        ;
    }
}
