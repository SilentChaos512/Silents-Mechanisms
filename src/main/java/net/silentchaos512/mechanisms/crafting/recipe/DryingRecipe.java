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
        ItemStack stack = inv.getItem(0);
        return ingredient.test(stack);
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
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
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DryingRecipe> {
        @Override
        public DryingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            DryingRecipe recipe = new DryingRecipe(recipeId);
            recipe.processTime = JSONUtils.getAsInt(json, "process_time", 400);
            recipe.ingredient = Ingredient.fromJson(json.get("ingredient"));
            recipe.result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return recipe;
        }

        @Override
        public DryingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            DryingRecipe recipe = new DryingRecipe(recipeId);
            recipe.processTime = buffer.readVarInt();
            recipe.ingredient = Ingredient.fromNetwork(buffer);
            recipe.result = buffer.readItem();
            return recipe;
        }

        @Override
        public void toNetwork(PacketBuffer buffer, DryingRecipe recipe) {
            buffer.writeVarInt(recipe.processTime);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
