package net.silentchaos512.mechanisms.crafting.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.mechanisms.block.IMachineInventory;
import net.silentchaos512.mechanisms.util.Constants;
import net.silentchaos512.mechanisms.util.InventoryUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlloySmeltingRecipe implements IRecipe<IMachineInventory> {
    public static final IRecipeType<AlloySmeltingRecipe> RECIPE_TYPE = new IRecipeType<AlloySmeltingRecipe>() {
        @Override
        public String toString() {
            return Constants.ALLOY_SMELTING.toString();
        }
    };
    public static final Serializer SERIALIZER = new Serializer();

    private final ResourceLocation recipeId;
    private int processTime;
    private final Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
    private ItemStack result;

    public AlloySmeltingRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public int getProcessTime() {
        return processTime;
    }

    public void consumeIngredients(IMachineInventory inv) {
        ingredients.forEach(((ingredient, count) -> InventoryUtils.consumeItems(inv, ingredient, count)));
    }

    public Map<Ingredient, Integer> getIngredientMap() {
        return ImmutableMap.copyOf(ingredients);
    }

    @Override
    public boolean matches(IMachineInventory inv, World worldIn) {
        for (Ingredient ingredient : ingredients.keySet()) {
            int required = ingredients.get(ingredient);
            int found = InventoryUtils.getTotalCount(inv, ingredient);
            if (found < required) {
                return false;
            }
        }

        // Check for non-matching items
        for (int i = 0; i < inv.getInputSlotCount(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                boolean foundMatch = false;
                for (Ingredient ingredient : ingredients.keySet()) {
                    if (ingredient.test(stack)) {
                        foundMatch = true;
                        break;
                    }
                }
                if (!foundMatch) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(IMachineInventory inv) {
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
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return RECIPE_TYPE;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlloySmeltingRecipe> {
        @Override
        public AlloySmeltingRecipe read(ResourceLocation recipeId, JsonObject json) {
            AlloySmeltingRecipe recipe = new AlloySmeltingRecipe(recipeId);
            recipe.processTime = JSONUtils.getInt(json, "process_time", 400);
            recipe.result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));

            JSONUtils.getJsonArray(json, "ingredients").forEach(element -> {
                Ingredient ingredient = deserializeIngredient(element);
                int count = JSONUtils.getInt(element.getAsJsonObject(), "count", 1);
                recipe.ingredients.put(ingredient, count);
            });

            return recipe;
        }

        private static Ingredient deserializeIngredient(JsonElement element) {
            if (element.isJsonObject()) {
                JsonObject json = element.getAsJsonObject();
                if (json.has("value"))
                    return Ingredient.deserialize(json.get("value"));
                if (json.has("values"))
                    return Ingredient.deserialize(json.get("values"));
            }
            return Ingredient.deserialize(element);
        }

        @Override
        public AlloySmeltingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            AlloySmeltingRecipe recipe = new AlloySmeltingRecipe(recipeId);
            recipe.processTime = buffer.readVarInt();
            recipe.result = buffer.readItemStack();

            int ingredientCount = buffer.readByte();
            for (int i = 0; i < ingredientCount; ++i) {
                Ingredient ingredient = Ingredient.read(buffer);
                int count = buffer.readByte();
                recipe.ingredients.put(ingredient, count);
            }

            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, AlloySmeltingRecipe recipe) {
            buffer.writeVarInt(recipe.processTime);
            buffer.writeItemStack(recipe.result);

            buffer.writeByte(recipe.ingredients.size());
            recipe.ingredients.forEach((ingredient, count) -> {
                ingredient.write(buffer);
                buffer.writeByte(count);
            });
        }
    }
}
