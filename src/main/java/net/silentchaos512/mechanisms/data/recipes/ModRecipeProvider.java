package net.silentchaos512.mechanisms.data.recipes;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.data.tag.ModItemTags;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);

    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        this.buildCraftingTableRecipes(consumer);
        this.buildSmeltingRecipes(consumer);
    }

    private void buildSmeltingRecipes(Consumer<FinishedRecipe> consumer) {

        //Ore Chunks -> Ingot
        ModItems.ALL_ORE_METALS.column(Metals.OreMetalType.INGOT).forEach((oreMetal, ingotItem) -> {
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.CHUNKS)), ingotItem.get(), 0.2f, 100).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.CHUNKS)).build())).save(consumer, SilentsMechanisms.loc("blasting/" + ingotItem.get().getRegistryName().getPath() + "_from_chunks"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.CHUNKS)), ingotItem.get(), 0.2f, 200).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.CHUNKS)).build())).save(consumer, SilentsMechanisms.loc("smelting/" + ingotItem.get().getRegistryName().getPath() + "_from_chunks"));
        });

        //Silk Touched Ores -> Ingot
        ModBlocks.ALL_ORE_BLOCKS.forEach((ore, oreBlock) -> {
            Item respectiveIngotItem = ModItems.ALL_ORE_METALS.get(ore.respectiveMetal, Metals.OreMetalType.INGOT).get();
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(oreBlock.get()), respectiveIngotItem, 0.2f, 100).unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(oreBlock.get())).save(consumer, SilentsMechanisms.loc("blasting/" + respectiveIngotItem.getRegistryName().getPath() + "_from_ore"));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(oreBlock.get()), respectiveIngotItem, 0.2f, 200).unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(oreBlock.get())).save(consumer, SilentsMechanisms.loc("smelting/" + respectiveIngotItem.getRegistryName().getPath() + "_from_ore"));

        });
    }

    private void buildCraftingTableRecipes(Consumer<FinishedRecipe> consumer) {

        //ingot -> storage blocks
        //storage block -> ingots
        ModBlocks.ALL_STORAGE_BLOCKS.forEach((metal, block) -> {
                    TagKey<Item> respectiveIngotTag = ModItemTags.ALL_METAL_TAGS.get(metal, Metals.OreMetalType.INGOT);
                    ShapedRecipeBuilder.shaped(block.get())
                            .pattern("AAA")
                            .pattern("AAA")
                            .pattern("AAA")
                            .define('A', respectiveIngotTag)
                            .unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(respectiveIngotTag).build()))
                            .save(consumer);


                    ShapelessRecipeBuilder.shapeless(ModItems.ALL_ORE_METALS.get(metal, Metals.OreMetalType.INGOT).get())
                            .requires(ModItemTags.ALL_STORAGE_BLOCKS_TAGS.get(metal))
                            .unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ALL_ORE_METALS.get(metal, Metals.OreMetalType.INGOT).get()))
                            .save(consumer);


        }
        );

        //nugget -> ingot
        //ingot -> nugget
        ModItems.ALL_ORE_METALS.column(Metals.OreMetalType.INGOT).forEach((oreMetal, itemItemRegistryObject) -> {
            ShapelessRecipeBuilder.shapeless(itemItemRegistryObject.get()).requires(Ingredient.of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.NUGGET)), 9).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItemTags.ALL_METAL_TAGS.get(oreMetal, Metals.OreMetalType.NUGGET)).build())).save(consumer, SilentsMechanisms.loc(itemItemRegistryObject.get().getRegistryName().getPath() + "_from_nuggets"));
            ShapelessRecipeBuilder.shapeless(ModItems.ALL_ORE_METALS.get(oreMetal, Metals.OreMetalType.NUGGET), 9).requires(itemItemRegistryObject.get()).unlockedBy("get_item", InventoryChangeTrigger.TriggerInstance.hasItems(itemItemRegistryObject.get())).save(consumer);
        });
    }
}
