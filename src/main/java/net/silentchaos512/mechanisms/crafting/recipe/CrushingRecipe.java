package net.silentchaos512.mechanisms.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.mechanisms.block.AbstractMachineTileEntity;
import net.silentchaos512.mechanisms.init.ModRecipes;
import net.silentchaos512.mechanisms.item.MachineUpgrades;
import net.silentchaos512.mechanisms.util.Constants;
import net.silentchaos512.utils.MathUtils;

import java.util.*;
import java.util.stream.Collectors;

public class CrushingRecipe implements IRecipe<IInventory> {
    private final ResourceLocation recipeId;
    private int processTime;
    private Ingredient ingredient;
    private final Map<ItemStack, Float> results = new LinkedHashMap<>();

    public CrushingRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    /**
     * Get the time (in ticks) required to crush one ingredient
     *
     * @return The process time in ticks
     */
    public int getProcessTime() {
        return processTime;
    }

    /**
     * Get the input ingredient for the recipe
     *
     * @return The input ingredient
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * Get results of crushing. Some results may have a limited chance of being produced, and this
     * method takes that into account.
     *
     * @param inv The crusher
     * @return Results of crushing
     */
    public List<ItemStack> getResults(IInventory inv) {
        int outputUpgrades = inv instanceof AbstractMachineTileEntity
                ? ((AbstractMachineTileEntity) inv).getUpgradeCount(MachineUpgrades.OUTPUT_CHANCE)
                : 0;
        return results.entrySet().stream()
                .filter(e -> {
                    float chance = e.getValue() + outputUpgrades * Constants.UPGRADE_SECONDARY_OUTPUT_AMOUNT;
                    return MathUtils.tryPercentage(chance);
                })
                .map(e -> e.getKey().copy())
                .collect(Collectors.toList());
    }

    /**
     * Get the possible results of crushing. Useful for making sure there is enough room in the
     * inventory.
     *
     * @param inv The crusher
     * @return All possible results of crushing
     */
    public Set<ItemStack> getPossibleResults(IInventory inv) {
        return results.keySet();
    }

    public List<Pair<ItemStack, Float>> getPossibleResultsWithChances() {
        return results.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Deprecated
    @Override
    public ItemStack assemble(IInventory inv) {
        // DO NOT USE
        return getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Deprecated
    @Override
    public ItemStack getResultItem() {
        // DO NOT USE
        return !results.isEmpty() ? results.keySet().iterator().next() : ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUSHING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.Types.CRUSHING;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrushingRecipe> {
        @Override
        public CrushingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            CrushingRecipe recipe = new CrushingRecipe(recipeId);
            recipe.processTime = JSONUtils.getAsInt(json, "process_time", 400);
            recipe.ingredient = Ingredient.fromJson(json.get("ingredient"));
            JsonArray resultsArray = json.getAsJsonArray("results");
            for (JsonElement element : resultsArray) {
                JsonObject obj = element.getAsJsonObject();
                String itemId = JSONUtils.getAsString(obj, "item");
                Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(itemId));
                int count = JSONUtils.getAsInt(obj, "count", 1);
                ItemStack stack = new ItemStack(item, count);
                float chance = JSONUtils.getAsFloat(obj, "chance", 1);
                recipe.results.put(stack, chance);
            }
            return recipe;
        }

        @Override
        public CrushingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            CrushingRecipe recipe = new CrushingRecipe(recipeId);
            recipe.processTime = buffer.readVarInt();
            recipe.ingredient = Ingredient.fromNetwork(buffer);
            int resultCount = buffer.readByte();
            for (int i = 0; i < resultCount; ++i) {
                ResourceLocation itemId = buffer.readResourceLocation();
                int count = buffer.readVarInt();
                float chance = buffer.readFloat();
                Item item = ForgeRegistries.ITEMS.getValue(itemId);
                recipe.results.put(new ItemStack(item, count), chance);
            }
            return recipe;
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CrushingRecipe recipe) {
            buffer.writeVarInt(recipe.processTime);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeByte(recipe.results.size());
            recipe.results.forEach((stack, chance) -> {
                buffer.writeResourceLocation(Objects.requireNonNull(stack.getItem().getRegistryName()));
                buffer.writeVarInt(stack.getCount());
                buffer.writeFloat(chance);
            });
        }
    }
}
