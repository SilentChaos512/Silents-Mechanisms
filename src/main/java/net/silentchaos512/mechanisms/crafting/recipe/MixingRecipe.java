package net.silentchaos512.mechanisms.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.FluidIngredient;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.IFluidInventory;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.IFluidRecipe;
import net.silentchaos512.mechanisms.init.ModRecipes;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MixingRecipe implements IFluidRecipe<IFluidInventory> {
    private final ResourceLocation recipeId;
    private int processTime;
    private final List<FluidIngredient> ingredients = NonNullList.create();
    private FluidStack result;

    public MixingRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public int getProcessTime() {
        return processTime;
    }

    @Override
    public boolean matches(IFluidInventory inv, World worldIn) {
        final int inputTanks = Math.min(4, inv.getTanks());
        Set<Integer> matchedTanks = new HashSet<>();

        for (FluidIngredient ingredient : ingredients) {
            for (int i = 0; i < inputTanks; ++i) {
                FluidStack stack = inv.getFluidInTank(i);

                if (!matchedTanks.contains(i) && ingredient.test(stack)) {
                    matchedTanks.add(i);
                    break;
                }
            }
        }

        return matchedTanks.size() == ingredients.size();
    }

    @Override
    public List<FluidStack> getFluidResults(IFluidInventory inv) {
        return getFluidOutputs();
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return Collections.singletonList(result.copy());
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.MIXING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.Types.MIXING;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MixingRecipe> {
        @Override
        public MixingRecipe read(ResourceLocation recipeId, JsonObject json) {
            MixingRecipe recipe = new MixingRecipe(recipeId);
            recipe.processTime = JSONUtils.getInt(json, "process_time");
            JSONUtils.getJsonArray(json, "ingredients").forEach(e ->
                    recipe.ingredients.add(FluidIngredient.deserialize(e.getAsJsonObject())));
            recipe.result = IFluidRecipe.deserializeFluid(JSONUtils.getJsonObject(json, "result"));
            return recipe;
        }

        @Nullable
        @Override
        public MixingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            MixingRecipe recipe = new MixingRecipe(recipeId);
            recipe.processTime = buffer.readVarInt();
            int ingredientCount = buffer.readByte();
            for (int i = 0; i < ingredientCount; ++i) {
                recipe.ingredients.add(FluidIngredient.read(buffer));
            }
            recipe.result = IFluidRecipe.readFluid(buffer);
            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, MixingRecipe recipe) {
            buffer.writeVarInt(recipe.processTime);
            buffer.writeByte(recipe.ingredients.size());
            recipe.ingredients.forEach(ingredient -> ingredient.write(buffer));
            IFluidRecipe.writeFluid(buffer, recipe.result);
        }
    }
}
