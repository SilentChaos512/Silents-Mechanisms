package net.silentchaos512.mechanisms.data.recipes;

import com.google.common.collect.Lists;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.data.tag.ModItemTags;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.utls.MechanismsUtils;

import java.util.function.Consumer;

@SuppressWarnings("ConstantConditions")
public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);

    }

    private static String getRegistryPath(ItemLike item) {
        return MechanismsUtils.getRegistryName(ForgeRegistries.ITEMS, item.asItem()).getPath();
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        this.buildCraftingTableRecipes(consumer);
        this.buildSmeltingRecipes(consumer);
    }

    private void buildSmeltingRecipes(Consumer<FinishedRecipe> consumer) {
        oreBlasting(consumer, Lists.newArrayList(ModItems.COPPER_DUST), Items.COPPER_INGOT, 0.2f, 100, "");
        oreSmelting(consumer, Lists.newArrayList(ModItems.COPPER_DUST), Items.COPPER_INGOT, 0.2f, 200, "");

        oreBlasting(consumer, Lists.newArrayList(ModItems.GOLD_DUST), Items.GOLD_INGOT, 0.2f, 100, "");
        oreSmelting(consumer, Lists.newArrayList(ModItems.GOLD_DUST), Items.GOLD_INGOT, 0.2f, 200, "");

        oreBlasting(consumer, Lists.newArrayList(ModItems.IRON_DUST), Items.IRON_INGOT, 0.2f, 100, "");
        oreSmelting(consumer, Lists.newArrayList(ModItems.IRON_DUST), Items.IRON_INGOT, 0.2f, 200, "");

        //Ore Chunks -> Ingot
        ModItems.ALL_ORE_METALS.column(Metals.OreMetalType.INGOT).forEach((oreMetal, ingotItem) -> {
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.RAW)), ingotItem, 0.2f, 100)
                    .unlockedBy("get_item", has(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.RAW)))
                    .save(consumer, SilentsMechanisms.location("blasting/" + getRegistryPath(ingotItem) + "_from_raw"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.RAW)), ingotItem, 0.2f, 200)
                    .unlockedBy("get_item", has(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.RAW)))
                    .save(consumer, SilentsMechanisms.location("smelting/" + getRegistryPath(ingotItem) + "_from_raw"));
        });

        ModItemTags.ALL_ALLOY_TAGS.column(Metals.AlloyType.DUST).forEach((alloy, dustTag) -> {
            Item respectiveIngotItem = ModItems.ALL_ALLOYS.get(alloy, Metals.AlloyType.INGOT);
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(dustTag), respectiveIngotItem, 0.2f, 100)
                    .unlockedBy("get_item", has(dustTag))
                    .save(consumer, SilentsMechanisms.location("blasting/" + getRegistryPath(respectiveIngotItem) + "_from_dust"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(dustTag), respectiveIngotItem, 0.2f, 200)
                    .unlockedBy("get_item", has(dustTag))
                    .save(consumer, SilentsMechanisms.location("smelting/" + getRegistryPath(respectiveIngotItem) + "_from_dust"));
        });

        ModItemTags.ALL_METAL_TAGS.column(Metals.OreMetalType.DUST).forEach((alloy, dustTag) -> {
            Item respectiveIngotItem = ModItems.ALL_ORE_METALS.get(alloy, Metals.OreMetalType.INGOT);
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(dustTag), respectiveIngotItem, 0.2f, 100)
                    .unlockedBy("get_item", has(dustTag))
                    .save(consumer, SilentsMechanisms.location("blasting/" + getRegistryPath(respectiveIngotItem) + "_from_dust"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(dustTag), respectiveIngotItem, 0.2f, 200)
                    .unlockedBy("get_item", has(dustTag))
                    .save(consumer, SilentsMechanisms.location("smelting/" + getRegistryPath(respectiveIngotItem) + "_from_dust"));
        });

        //Silk Touched Ores -> Ingot
        ModBlocks.ALL_ORE_BLOCKS.forEach((ore, oreBlock) -> {
            Item respectiveIngotItem = ModItems.ALL_ORE_METALS.get(ore.respectiveMetal, Metals.OreMetalType.INGOT);
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(oreBlock), respectiveIngotItem, 0.2f, 100)
                    .unlockedBy("has_item", has(oreBlock))
                    .save(consumer, SilentsMechanisms.location("blasting/" + getRegistryPath(respectiveIngotItem) + "_from_ore"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(oreBlock), respectiveIngotItem, 0.2f, 200)
                    .unlockedBy("has_item", has(oreBlock))
                    .save(consumer, SilentsMechanisms.location("smelting/" + getRegistryPath(respectiveIngotItem) + "_from_ore"));
        });
    }

    private void buildCraftingTableRecipes(Consumer<FinishedRecipe> consumer) {
        //ingot -> storage blocks
        //storage block -> ingots
        for (Metals.StorageBlockProvider provider : Metals.StorageBlockProvider.ALL_PROVIDERS) {
            TagKey<Item> ingredient = provider.getIngredientTag();
            ShapedRecipeBuilder.shaped(provider.getStorageBlock())
                    .pattern("AAA")
                    .pattern("AAA")
                    .pattern("AAA")
                    .define('A', ingredient)
                    .unlockedBy("hasItem", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ingredient).build()))
                    .save(consumer, SilentsMechanisms.location("crafting/" + getRegistryPath(provider.getStorageBlock())));
            ShapelessRecipeBuilder.shapeless(provider.getIngot(), 9)
                    .requires(provider.getStorageBlock())
                    .unlockedBy("hasItem", InventoryChangeTrigger.TriggerInstance.hasItems(provider.getStorageBlock()))
                    .save(consumer, SilentsMechanisms.location("crafting/" + getRegistryPath(provider.getIngot())));
        }

        //nugget -> ingot
        //ingot -> nugget
        ModItems.ALL_ORE_METALS.column(Metals.OreMetalType.INGOT).forEach((oreMetal, itemItemRegistryObject) -> {
            ShapelessRecipeBuilder.shapeless(itemItemRegistryObject)
                    .requires(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.NUGGET)), 9)
                    .unlockedBy("get_item", has(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.NUGGET)))
                    .save(consumer, SilentsMechanisms.location(getRegistryPath(itemItemRegistryObject) + "_from_nuggets"));
            ShapelessRecipeBuilder.shapeless(ModItems.ALL_ORE_METALS.get(oreMetal, Metals.OreMetalType.NUGGET), 9)
                    .requires(itemItemRegistryObject)
                    .unlockedBy("get_item", has(itemItemRegistryObject))
                    .save(consumer);
        });

        ModItems.ALL_ALLOYS.column(Metals.AlloyType.INGOT).forEach((alloy, alloyItem) -> {
            ShapelessRecipeBuilder.shapeless(alloyItem)
                    .requires(Ingredient.of(ModItemTags.ALL_ALLOY_TAGS.get(alloy, Metals.AlloyType.NUGGET)), 9)
                    .unlockedBy("get_item", has(ModItemTags.ALL_ALLOY_TAGS.get(alloy, Metals.AlloyType.NUGGET)))
                    .save(consumer, SilentsMechanisms.location(getRegistryPath(alloyItem) + "_from_nuggets"));
            ShapelessRecipeBuilder.shapeless(ModItems.ALL_ALLOYS.get(alloy, Metals.AlloyType.NUGGET), 9)
                    .requires(alloyItem).unlockedBy("has_item", has(alloyItem))
                    .save(consumer);
        });

        ShapedRecipeBuilder.shaped(Items.LEATHER)
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.ZOMBIE_LEATHER)
                .unlockedBy("get_item", has(ModItems.ZOMBIE_LEATHER))
                .save(consumer);

        DryingRackBlock.ALL_RACKS.forEach(dryingRackBlock -> {
            ShapedRecipeBuilder.shaped(dryingRackBlock)
                    .pattern("AAA")
                    .define('A', dryingRackBlock.woodMaterial)
                    .unlockedBy("has_item", has(dryingRackBlock.woodMaterial))
                    .save(consumer);
        });
    }
}
