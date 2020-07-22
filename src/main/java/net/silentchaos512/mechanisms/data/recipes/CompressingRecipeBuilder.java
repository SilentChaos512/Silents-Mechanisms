package net.silentchaos512.mechanisms.data.recipes;

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
import net.silentchaos512.mechanisms.crafting.recipe.CompressingRecipe;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CompressingRecipeBuilder {
    private final Ingredient ingredient;
    private final int ingredientCount;
    private final ItemStack result;
    private final int processTime;

    private CompressingRecipeBuilder(Ingredient ingredient, int ingredientCount, IItemProvider result, int resultCount, int processTime) {
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
        this.result = new ItemStack(result, resultCount);
        this.processTime = processTime;
    }

    public static CompressingRecipeBuilder builder(Ingredient ingredient, int ingredientCount, IItemProvider result, int resultCount, int processTime) {
        return new CompressingRecipeBuilder(ingredient, ingredientCount, result, resultCount, processTime);
    }

    public static CompressingRecipeBuilder builder(ITag<Item> ingredient, int ingredientCount, IItemProvider result, int resultCount, int processTime) {
        return builder(Ingredient.fromTag(ingredient), ingredientCount, result, resultCount, processTime);
    }

    public static CompressingRecipeBuilder builder(IItemProvider ingredient, int ingredientCount, IItemProvider result, int resultCount, int processTime) {
        return builder(Ingredient.fromItems(ingredient), ingredientCount, result, resultCount, processTime);
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        ResourceLocation resultId = NameUtils.fromItem(result);
        ResourceLocation id = new ResourceLocation(
                "minecraft".equals(resultId.getNamespace()) ? SilentMechanisms.MOD_ID : resultId.getNamespace(),
                "compressing/" + resultId.getPath());
        build(consumer, id);
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final CompressingRecipeBuilder builder;

        public Result(ResourceLocation id, CompressingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("process_time", builder.processTime);

            JsonObject ingredient = new JsonObject();
            ingredient.add("value", builder.ingredient.serialize());
            ingredient.addProperty("count", builder.ingredientCount);
            json.add("ingredient", ingredient);

            JsonObject result = new JsonObject();
            result.addProperty("item", NameUtils.fromItem(builder.result).toString());
            if (builder.result.getCount() > 1) {
                result.addProperty("count", builder.result.getCount());
            }
            json.add("result", result);
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return CompressingRecipe.SERIALIZER;
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
