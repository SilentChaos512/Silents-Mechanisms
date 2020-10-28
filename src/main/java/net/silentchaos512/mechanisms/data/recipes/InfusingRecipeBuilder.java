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
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.FluidIngredient;
import net.silentchaos512.mechanisms.init.ModRecipes;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class InfusingRecipeBuilder {
    private final Ingredient ingredient;
    private final FluidIngredient fluid;
    private final ItemStack result;
    private final int processTime;

    private InfusingRecipeBuilder(Ingredient ingredient, FluidIngredient fluid, ItemStack result, int processTime) {
        this.ingredient = ingredient;
        this.fluid = fluid;
        this.result = result;
        this.processTime = processTime;
    }

    public static InfusingRecipeBuilder builder(IItemProvider result, int count, int processTime, Ingredient ingredient, FluidIngredient fluid) {
        return new InfusingRecipeBuilder(ingredient, fluid, new ItemStack(result, count), processTime);
    }

    public static InfusingRecipeBuilder builder(IItemProvider result, int count, int processTime, ITag<Item> ingredient, FluidIngredient fluid) {
        return builder(result, count, processTime, Ingredient.fromTag(ingredient), fluid);
    }

    public static InfusingRecipeBuilder builder(IItemProvider result, int count, int processTime, IItemProvider ingredient, FluidIngredient fluid) {
        return builder(result, count, processTime, Ingredient.fromItems(ingredient), fluid);
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        ResourceLocation resultId = NameUtils.fromItem(result);
        ResourceLocation id = new ResourceLocation(
                "minecraft".equals(resultId.getNamespace()) ? SilentMechanisms.MOD_ID : resultId.getNamespace(),
                "infusing/" + resultId.getPath());
        build(consumer, id);
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final InfusingRecipeBuilder builder;

        public Result(ResourceLocation id, InfusingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("process_time", builder.processTime);

            json.add("ingredient", builder.ingredient.serialize());
            json.add("fluid", builder.fluid.serialize());

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
            return ModRecipes.INFUSING.get();
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
