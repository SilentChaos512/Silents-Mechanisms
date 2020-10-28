package net.silentchaos512.mechanisms.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModRecipes;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AlloySmeltingRecipeBuilder {
    private final Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
    private final ItemStack result;
    private final int processTime;

    private AlloySmeltingRecipeBuilder(IItemProvider result, int count, int processTime) {
        this.result = new ItemStack(result, count);
        this.processTime = processTime;
    }

    public static AlloySmeltingRecipeBuilder builder(IItemProvider result, int count, int processTime) {
        return new AlloySmeltingRecipeBuilder(result, count, processTime);
    }

    public static AlloySmeltingRecipeBuilder builder(Metals result, int count, int processTime) {
        return builder(result.getIngot().get(), count, processTime);
    }

    public AlloySmeltingRecipeBuilder ingredient(Ingredient ingredient, int count) {
        ingredients.put(ingredient, count);
        return this;
    }

    public AlloySmeltingRecipeBuilder ingredient(IItemProvider item, int count) {
        return ingredient(Ingredient.fromItems(item), count);
    }

    public AlloySmeltingRecipeBuilder ingredient(ITag<Item> tag, int count) {
        return ingredient(Ingredient.fromTag(tag), count);
    }

    public AlloySmeltingRecipeBuilder ingredient(Metals metal, int count) {
        return ingredient(metal.getSmeltables(), count);
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        ResourceLocation resultId = NameUtils.fromItem(result);
        ResourceLocation id = new ResourceLocation(
                "minecraft".equals(resultId.getNamespace()) ? SilentMechanisms.MOD_ID : resultId.getNamespace(),
                "alloy_smelting/" + resultId.getPath());
        build(consumer, id);
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final AlloySmeltingRecipeBuilder builder;

        public Result(ResourceLocation id, AlloySmeltingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("process_time", builder.processTime);

            JsonArray ingredients = new JsonArray();
            builder.ingredients.forEach((ingredient, count) ->
                    ingredients.add(serializeIngredient(ingredient, count)));
            json.add("ingredients", ingredients);

            JsonObject result = new JsonObject();
            result.addProperty("item", NameUtils.fromItem(builder.result).toString());
            if (builder.result.getCount() > 1) {
                result.addProperty("count", builder.result.getCount());
            }
            json.add("result", result);
        }

        private JsonElement serializeIngredient(Ingredient ingredient, int count) {
            JsonObject json = new JsonObject();
            json.add("value", ingredient.serialize());
            json.addProperty("count", count);
            return json;
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ModRecipes.ALLOY_SMELTING.get();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
