package net.silentchaos512.mechanisms.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.mechanisms.JsonNames;

public abstract class SimpleRecipeSerializer<T extends Recipe<?>> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
    public SimpleRecipeSerializer() {
        super.setRegistryName(getSerializerId());
    }

    protected abstract ResourceLocation getSerializerId();

    @SuppressWarnings("deprecation")
    protected final ItemStack getItemStack(JsonObject json) {
        if (json.has(JsonNames.RESULT)) {
            if (json.get(JsonNames.RESULT).isJsonObject()) {
                return ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, JsonNames.RESULT));
            } else {
                String itemId = GsonHelper.getAsString(json, JsonNames.RESULT);
                return new ItemStack(Registry.ITEM.get(new ResourceLocation(itemId)));
            }
        }

        throw new IllegalStateException("Can not find result");
    }
}
