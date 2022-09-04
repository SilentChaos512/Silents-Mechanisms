package net.silentchaos512.mechanisms.data.recipes;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.data.tag.ModItemTags;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.utls.MechanismsUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * <b> WARNING : This may be very brainstorming </b>
 */
@SuppressWarnings("ConstantConditions")
public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);

    }

    @SuppressWarnings("deprecation")
    private static String getRegistryPath(Item item) {
        return MechanismsUtils.getRegistryName(Registry.ITEM, item).getPath();
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        this.buildCraftingTableRecipes(consumer);
        this.buildSmeltingRecipes(consumer);
    }

    private void buildSmeltingRecipes(Consumer<FinishedRecipe> consumer) {

        oreBlasting(consumer, List.of(ModItems.GOLD_DUST, ModItems.GOLD_CHUNK), Items.GOLD_INGOT, 0.2f, 100, "");
        oreSmelting(consumer, List.of(ModItems.GOLD_DUST, ModItems.GOLD_CHUNK), Items.GOLD_INGOT, 0.2f, 200, "");

        oreBlasting(consumer, List.of(ModItems.IRON_DUST, ModItems.IRON_CHUNK), Items.IRON_INGOT, 0.2f, 100, "");
        oreSmelting(consumer, List.of(ModItems.IRON_DUST, ModItems.IRON_CHUNK), Items.IRON_INGOT, 0.2f, 200, "");


        //Ore Chunks -> Ingot
        ModItems.ALL_ORE_METALS.column(Metals.OreMetalType.INGOT).forEach((oreMetal, ingotItem) -> {
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.CHUNKS)), ingotItem, 0.2f, 100).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.CHUNKS)).build())).save(consumer, SilentsMechanisms.location("blasting/" + getRegistryPath(ingotItem) + "_from_chunks"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.CHUNKS)), ingotItem, 0.2f, 200).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.CHUNKS)).build())).save(consumer, SilentsMechanisms.location("smelting/" + getRegistryPath(ingotItem) + "_from_chunks"));
        });

        ModItemTags.ALL_ALLOY_TAGS.column(Metals.AlloyType.DUST).forEach((alloy, dustTag) -> {
            Item respectiveIngotItem = ModItems.ALL_ALLOYS.get(alloy, Metals.AlloyType.INGOT);
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(dustTag), respectiveIngotItem, 0.2f, 100).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(dustTag).build())).save(consumer, SilentsMechanisms.location("blasting/" + getRegistryPath(respectiveIngotItem) + "_from_dust"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(dustTag), respectiveIngotItem, 0.2f, 200).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(dustTag).build())).save(consumer, SilentsMechanisms.location("smelting/" + getRegistryPath(respectiveIngotItem) + "_from_dust"));
        });

        ModItemTags.ALL_METAL_TAGS.column(Metals.OreMetalType.DUST).forEach((alloy, dustTag) -> {
            Item respectiveIngotItem = ModItems.ALL_ORE_METALS.get(alloy, Metals.OreMetalType.INGOT);
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(dustTag), respectiveIngotItem, 0.2f, 100).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(dustTag).build())).save(consumer, SilentsMechanisms.location("blasting/" + getRegistryPath(respectiveIngotItem) + "_from_dust"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(dustTag), respectiveIngotItem, 0.2f, 200).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(dustTag).build())).save(consumer, SilentsMechanisms.location("smelting/" + getRegistryPath(respectiveIngotItem) + "_from_dust"));
        });

        //Silk Touched Ores -> Ingot
        ModBlocks.ALL_ORE_BLOCKS.forEach((ore, oreBlock) -> {
            Item respectiveIngotItem = ModItems.ALL_ORE_METALS.get(ore.respectiveMetal, Metals.OreMetalType.INGOT);
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(oreBlock), respectiveIngotItem, 0.2f, 100).unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(oreBlock)).save(consumer, SilentsMechanisms.location("blasting/" + getRegistryPath(respectiveIngotItem) + "_from_ore"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(oreBlock), respectiveIngotItem, 0.2f, 200).unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(oreBlock)).save(consumer, SilentsMechanisms.location("smelting/" + getRegistryPath(respectiveIngotItem) + "_from_ore"));
        });
    }

    private void buildCraftingTableRecipes(Consumer<FinishedRecipe> consumer) {

        //ingot -> storage blocks
        //storage block -> ingots
        ModBlocks.ALL_STORAGE_BLOCKS.forEach((metal, block) -> {
                    TagKey<Item> respectiveIngotTag = ModItemTags.ALL_METAL_TAGS.get(metal, Metals.OreMetalType.INGOT);
                    ShapedRecipeBuilder.shaped(block)
                            .pattern("AAA")
                            .pattern("AAA")
                            .pattern("AAA")
                            .define('A', respectiveIngotTag)
                            .unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(respectiveIngotTag).build()))
                            .save(consumer);


                    ShapelessRecipeBuilder.shapeless(ModItems.ALL_ORE_METALS.get(metal, Metals.OreMetalType.INGOT))
                            .requires(ModItemTags.ALL_STORAGE_BLOCKS_TAGS.get(metal))
                            .unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ALL_ORE_METALS.get(metal, Metals.OreMetalType.INGOT)))
                            .save(consumer);


                }
        );

        ModBlocks.ALL_ALLOY_STORAGE_BLOCKS.forEach((alloy, block) -> {
                    TagKey<Item> respectiveIngotTag = ModItemTags.ALL_ALLOY_TAGS.get(alloy, Metals.AlloyType.INGOT);
                    ShapedRecipeBuilder.shaped(block)
                            .pattern("AAA")
                            .pattern("AAA")
                            .pattern("AAA")
                            .define('A', respectiveIngotTag)
                            .unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(respectiveIngotTag).build()))
                            .save(consumer);


                    ShapelessRecipeBuilder.shapeless(ModItems.ALL_ALLOYS.get(alloy, Metals.AlloyType.INGOT))
                            .requires(ModItemTags.ALL_ALLOY_STORAGE_BLOCKS_TAGS.get(alloy))
                            .unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ALL_ALLOYS.get(alloy, Metals.AlloyType.INGOT)))
                            .save(consumer);


                }
        );

        //nugget -> ingot
        //ingot -> nugget
        ModItems.ALL_ORE_METALS.column(Metals.OreMetalType.INGOT).forEach((oreMetal, itemItemRegistryObject) -> {
            ShapelessRecipeBuilder.shapeless(itemItemRegistryObject).requires(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.NUGGET)), 9).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.NUGGET)).build())).save(consumer, SilentsMechanisms.location(getRegistryPath(itemItemRegistryObject) + "_from_nuggets"));
            ShapelessRecipeBuilder.shapeless(ModItems.ALL_ORE_METALS.get(oreMetal, Metals.OreMetalType.NUGGET), 9).requires(itemItemRegistryObject).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(itemItemRegistryObject)).save(consumer);
        });

        ModItems.ALL_ALLOYS.column(Metals.AlloyType.INGOT).forEach((alloy, alloyItem) -> {
            ShapelessRecipeBuilder.shapeless(alloyItem).requires(Ingredient.of(ModItemTags.ALL_ALLOY_TAGS.get(alloy, Metals.AlloyType.NUGGET)), 9).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItemTags.ALL_ALLOY_TAGS.get(alloy, Metals.AlloyType.NUGGET)).build())).save(consumer, SilentsMechanisms.location(getRegistryPath(alloyItem) + "_from_nuggets"));
            ShapelessRecipeBuilder.shapeless(ModItems.ALL_ALLOYS.get(alloy, Metals.AlloyType.NUGGET), 9).requires(alloyItem).unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(alloyItem)).save(consumer);
        });

        ShapedRecipeBuilder.shaped(Items.LEATHER).pattern("AA").pattern("AA").define('A', ModItems.ZOMBIE_LEATHER).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ZOMBIE_LEATHER)).save(consumer);
        ModBlocks.DRYING_RACK_BLOCKS.forEach(dryingRackBlock -> ShapedRecipeBuilder.shaped(dryingRackBlock).pattern("AAA").define('A', dryingRackBlock.woodMaterial).unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(dryingRackBlock.woodMaterial)).save(consumer));
    }
}
