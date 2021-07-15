package net.silentchaos512.mechanisms.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
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
import java.util.List;

public class SolidifyingRecipe implements IFluidRecipe<IFluidInventory> {
    private final ResourceLocation recipeId;
    private int processTime;
    private FluidIngredient ingredient;
    private ItemStack result;

    public SolidifyingRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public FluidIngredient getIngredient() {
        return ingredient;
    }

    public int getProcessTime() {
        return processTime;
    }

    @Override
    public boolean matches(IFluidInventory inv, World worldIn) {
        return ingredient.test(inv.getFluidInTank(0));
    }

    @Override
    public List<FluidStack> getFluidResults(IFluidInventory inv) {
        return getFluidOutputs();
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return Collections.emptyList();
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return Collections.singletonList(ingredient);
    }

    @Override
    public ItemStack assemble(IFluidInventory inv) {
        return getResultItem();
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.SOLIDIFYING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.Types.SOLIDIFYING;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SolidifyingRecipe> {
        @Override
        public SolidifyingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            SolidifyingRecipe recipe = new SolidifyingRecipe(recipeId);
            recipe.processTime = JSONUtils.getAsInt(json, "process_time");
            recipe.ingredient = FluidIngredient.deserialize(json.getAsJsonObject("ingredient"));
            recipe.result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return recipe;
        }

        @Nullable
        @Override
        public SolidifyingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            SolidifyingRecipe recipe = new SolidifyingRecipe(recipeId);
            recipe.processTime = buffer.readVarInt();
            recipe.ingredient = FluidIngredient.read(buffer);
            recipe.result = buffer.readItem();
            return recipe;
        }

        @Override
        public void toNetwork(PacketBuffer buffer, SolidifyingRecipe recipe) {
            buffer.writeVarInt(recipe.processTime);
            recipe.ingredient.write(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
