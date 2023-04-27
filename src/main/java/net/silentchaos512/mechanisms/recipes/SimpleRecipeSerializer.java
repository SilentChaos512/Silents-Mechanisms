package net.silentchaos512.mechanisms.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.init.ModRecipes;
import net.silentchaos512.mechanisms.utls.JsonNames;

public abstract class SimpleRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
    public SimpleRecipeSerializer() {
        ModRecipes.ALL_SERIALIZERS.put(this.getSerializerId(), this);
    }

    public abstract ResourceLocation getSerializerId();

    protected final ItemStack getItemStack(JsonObject json) {
        if (json.has(JsonNames.RESULT)) {
            if (json.get(JsonNames.RESULT).isJsonObject()) {
                return ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, JsonNames.RESULT));
            } else {
                String itemId = GsonHelper.getAsString(json, JsonNames.RESULT);
                return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)));
            }
        }

        throw new IllegalStateException("Can not find result");
    }
}
