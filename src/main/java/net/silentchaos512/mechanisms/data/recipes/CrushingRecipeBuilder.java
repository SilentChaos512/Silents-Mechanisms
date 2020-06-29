package net.silentchaos512.mechanisms.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.crafting.recipe.CrushingRecipe;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class CrushingRecipeBuilder {
    private final Map<ItemStack, Float> results = new LinkedHashMap<>();
    private final Ingredient ingredient;
    private final int processTime;

    private CrushingRecipeBuilder(Ingredient ingredient, int processTime) {
        this.ingredient = ingredient;
        this.processTime = processTime;
    }

    public static CrushingRecipeBuilder builder(IItemProvider ingredient, int processTime) {
        return builder(Ingredient.fromItems(ingredient), processTime);
    }

    public static CrushingRecipeBuilder builder(Tag<Item> ingredient, int processTime) {
        return builder(Ingredient.fromTag(ingredient), processTime);
    }

    public static CrushingRecipeBuilder builder(Ingredient ingredient, int processTime) {
        return new CrushingRecipeBuilder(ingredient, processTime);
    }

    public CrushingRecipeBuilder result(IItemProvider item, int count, float chance) {
        results.put(new ItemStack(item, count), chance);
        return this;
    }

    public CrushingRecipeBuilder result(IItemProvider item, int count) {
        return result(item, count, 1f);
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        ResourceLocation firstResultId = NameUtils.fromItem(results.keySet().iterator().next());
        ResourceLocation id = "minecraft".equals(firstResultId.getNamespace())
                ? new ResourceLocation(SilentMechanisms.MOD_ID, "crushing/" + firstResultId.getPath())
                : firstResultId;
        build(consumer, id);
    }

    private void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new CrushingRecipeBuilder.Result(id, this));
    }

    public class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final CrushingRecipeBuilder builder;

        public Result(ResourceLocation id, CrushingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("process_time", builder.processTime);
            json.add("ingredient", builder.ingredient.serialize());

            JsonArray results = new JsonArray();
            builder.results.forEach((stack, chance) ->
                    results.add(serializeResult(stack, chance)));
            json.add("results", results);
        }

        private JsonObject serializeResult(ItemStack stack, float chance) {
            JsonObject json = new JsonObject();
            json.addProperty("item", NameUtils.fromItem(stack).toString());
            if (stack.getCount() > 1) {
                json.addProperty("count", stack.getCount());
            }
            if (chance < 1) {
                json.addProperty("chance", chance);
            }
            return json;
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return CrushingRecipe.SERIALIZER;
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
