package net.silentchaos512.mechanisms.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.mechanisms.init.ModRecipes;

public class DryingRecipe implements IRecipe<IInventory> {
    private final ResourceLocation recipeId;
    private int processTime;
    private Ingredient ingredient;
    private ItemStack result;

    public DryingRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public int getProcessTime() {
        return processTime;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        ItemStack stack = inv.getStackInSlot(0);
        return ingredient.test(stack);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.DRYING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.Types.DRYING;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DryingRecipe> {
        @Override
        public DryingRecipe read(ResourceLocation recipeId, JsonObject json) {
            DryingRecipe recipe = new DryingRecipe(recipeId);
            recipe.processTime = JSONUtils.getInt(json, "process_time", 400);
            recipe.ingredient = Ingredient.deserialize(json.get("ingredient"));
            recipe.result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return recipe;
        }

        @Override
        public DryingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            DryingRecipe recipe = new DryingRecipe(recipeId);
            recipe.processTime = buffer.readVarInt();
            recipe.ingredient = Ingredient.read(buffer);
            recipe.result = buffer.readItemStack();
            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, DryingRecipe recipe) {
            buffer.writeVarInt(recipe.processTime);
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }
}
