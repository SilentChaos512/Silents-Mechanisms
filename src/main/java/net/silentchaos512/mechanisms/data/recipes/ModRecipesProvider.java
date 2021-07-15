package net.silentchaos512.mechanisms.data.recipes;

import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.silentchaos512.lib.data.recipe.ExtendedShapedRecipeBuilder;
import net.silentchaos512.lib.data.recipe.ExtendedShapelessRecipeBuilder;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.FluidIngredient;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.init.ModTags;
import net.silentchaos512.mechanisms.item.CraftingItems;
import net.silentchaos512.mechanisms.item.MachineUpgrades;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ModRecipesProvider extends RecipeProvider {
    private static final int CRUSHING_CHUNKS_TIME = 300;
    private static final int CRUSHING_INGOT_TIME = 200;
    private static final int CRUSHING_ORE_TIME = 400;
    private static final float CRUSHING_CHUNKS_EXTRA_CHANCE = 0.1f;
    private static final float CRUSHING_ORE_STONE_CHANCE = 0.1f;

    public ModRecipesProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return "Silent's Mechanisms - Recipes";
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        registerCrafting(consumer);
        registerSmelting(consumer);
        registerAlloySmelting(consumer);
        registerCompressingRecipes(consumer);
        registerCrushingRecipes(consumer);
        registerInfusingRecipes(consumer);
    }

    private void registerCrafting(Consumer<IFinishedRecipe> consumer) {
        registerMetalCrafting(consumer);
        registerBlockCrafting(consumer);
        registerItemCrafting(consumer);
    }

    private void registerMetalCrafting(Consumer<IFinishedRecipe> consumer) {
        for (Metals metal : Metals.values()) {
            if (metal.getIngot().isPresent() && metal.getNuggetTag().isPresent()) {
                ExtendedShapedRecipeBuilder.vanillaBuilder(metal.getIngot().get())
                        .patternLine("###")
                        .patternLine("###")
                        .patternLine("###")
                        .key('#', metal.getNuggetTag().get())
                        .build(consumer, SilentMechanisms.getId("metals/" + metal.getName() + "_ingot_from_nugget"));
            }
            if (metal.getNugget().isPresent() && metal.getIngotTag().isPresent()) {
                ExtendedShapelessRecipeBuilder.vanillaBuilder(metal.getNugget().get(), 9)
                        .addIngredient(metal.getIngotTag().get())
                        .build(consumer, SilentMechanisms.getId("metals/" + metal.getName() + "_nugget"));
            }
            if (metal.getStorageBlock().isPresent() && metal.getIngotTag().isPresent()) {
                ExtendedShapedRecipeBuilder.vanillaBuilder(metal.getStorageBlock().get())
                        .patternLine("###")
                        .patternLine("###")
                        .patternLine("###")
                        .key('#', metal.getIngotTag().get())
                        .build(consumer, SilentMechanisms.getId("metals/" + metal.getName() + "_block"));
            }
            if (metal.getIngot().isPresent() && metal.getStorageBlockItemTag().isPresent()) {
                ExtendedShapelessRecipeBuilder.vanillaBuilder(metal.getIngot().get(), 9)
                        .addIngredient(metal.getStorageBlockItemTag().get())
                        .build(consumer, SilentMechanisms.getId("metals/" + metal.getName() + "_ingot_from_block"));
            }
        }
    }

    private void registerBlockCrafting(Consumer<IFinishedRecipe> consumer) {
        ExtendedShapedRecipeBuilder.vanillaBuilder(ModBlocks.ACACIA_DRYING_RACK)
                .patternLine("###")
                .key('#', Blocks.ACACIA_SLAB)
                .build(consumer);
        ExtendedShapedRecipeBuilder.vanillaBuilder(ModBlocks.BIRCH_DRYING_RACK)
                .patternLine("###")
                .key('#', Blocks.BIRCH_SLAB)
                .build(consumer);
        ExtendedShapedRecipeBuilder.vanillaBuilder(ModBlocks.DARK_OAK_DRYING_RACK)
                .patternLine("###")
                .key('#', Blocks.DARK_OAK_SLAB)
                .build(consumer);
        ExtendedShapedRecipeBuilder.vanillaBuilder(ModBlocks.JUNGLE_DRYING_RACK)
                .patternLine("###")
                .key('#', Blocks.JUNGLE_SLAB)
                .build(consumer);
        ExtendedShapedRecipeBuilder.vanillaBuilder(ModBlocks.OAK_DRYING_RACK)
                .patternLine("###")
                .key('#', Blocks.OAK_SLAB)
                .build(consumer);
        ExtendedShapedRecipeBuilder.vanillaBuilder(ModBlocks.SPRUCE_DRYING_RACK)
                .patternLine("###")
                .key('#', Blocks.SPRUCE_SLAB)
                .build(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.STONE_MACHINE_FRAME, 2)
                .pattern("/#/")
                .pattern("#s#")
                .pattern("/#/")
                .define('/', Blocks.SMOOTH_STONE)
                .define('#', Tags.Items.GLASS)
                .define('s', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_item", has(Blocks.SMOOTH_STONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ALLOY_MACHINE_FRAME, 2)
                .pattern("/#/")
                .pattern("#s#")
                .pattern("/#/")
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('#', Tags.Items.GLASS)
                .define('s', ModTags.Items.STEELS)
                .unlockedBy("has_item", has(Metals.REDSTONE_ALLOY.getIngotTag().get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.BASIC_ALLOY_SMELTER)
                .pattern("###")
                .pattern("/X/")
                .pattern("O/O")
                .define('#', Metals.TIN.getIngotTag().get())
                .define('/', Metals.COPPER.getIngotTag().get())
                .define('X', ModBlocks.STONE_MACHINE_FRAME)
                .define('O', Blocks.BRICKS)
                .unlockedBy("has_item", has(ModBlocks.STONE_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ALLOY_SMELTER)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("OHO")
                .define('#', Metals.BISMUTH_BRASS.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Blocks.BRICKS)
                .define('H', CraftingItems.HEATING_ELEMENT)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ALLOY_SMELTER)
                .pattern("#C#")
                .pattern("/X/")
                .define('#', Metals.BISMUTH_BRASS.getIngotTag().get())
                .define('C', ModBlocks.BASIC_ALLOY_SMELTER)
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer, SilentMechanisms.getId("alloy_smelter_from_basic"));

        ShapedRecipeBuilder.shaped(ModBlocks.BASIC_CRUSHER)
                .pattern("###")
                .pattern("/X/")
                .pattern("O/O")
                .define('#', Metals.BRONZE.getIngotTag().get())
                .define('/', Metals.ALUMINUM.getIngotTag().get())
                .define('X', ModBlocks.STONE_MACHINE_FRAME)
                .define('O', Blocks.SMOOTH_STONE)
                .unlockedBy("has_item", has(ModBlocks.STONE_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.CRUSHER)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("ODO")
                .define('#', Metals.BISMUTH_STEEL.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Blocks.SMOOTH_STONE)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.CRUSHER)
                .pattern("#C#")
                .pattern("/X/")
                .pattern(" D ")
                .define('#', Metals.BISMUTH_STEEL.getIngotTag().get())
                .define('C', ModBlocks.BASIC_CRUSHER)
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer, SilentMechanisms.getId("crusher_from_basic"));

        ShapedRecipeBuilder.shaped(ModBlocks.COMPRESSOR)
                .pattern("#D#")
                .pattern("/X/")
                .pattern("ODC")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Blocks.SMOOTH_STONE)
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ELECTRIC_FURNACE)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("OHO")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Blocks.SMOOTH_STONE)
                .define('H', CraftingItems.HEATING_ELEMENT)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.REFINERY)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("OHO")
                .define('#', Metals.ALUMINUM_STEEL.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', ModItems.EMPTY_CANISTER)
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Metals.ELECTRUM.getIngotTag().get())
                .define('H', CraftingItems.HEATING_ELEMENT)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.MIXER)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("OHO")
                .define('#', Metals.BISMUTH_STEEL.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', ModItems.EMPTY_CANISTER)
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Metals.BRASS.getIngotTag().get())
                .define('H', Items.PISTON)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.SOLIDIFIER)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("OHO")
                .define('#', Metals.STEEL.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', ModItems.EMPTY_CANISTER)
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Metals.SILVER.getIngotTag().get())
                .define('H', Items.IRON_BARS)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.INFUSER)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("OPO")
                .define('#', Metals.BISMUTH_BRASS.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', ModItems.EMPTY_CANISTER)
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Metals.NICKEL.getIngotTag().get())
                .define('P', ModTags.Items.PLASTIC)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.PUMP)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("OHO")
                .define('#', Metals.ALUMINUM.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', Metals.INVAR.getIngotTag().get())
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Items.BUCKET)
                .define('H', Items.PISTON)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.COAL_GENERATOR)
                .pattern("###")
                .pattern("/X/")
                .pattern("OAO")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('/', Metals.COPPER.getIngotTag().get())
                .define('X', ModBlocks.STONE_MACHINE_FRAME)
                .define('O', Tags.Items.COBBLESTONE)
                .define('A', Metals.REFINED_IRON.getIngotTag().get())
                .unlockedBy("has_item", has(ModBlocks.STONE_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.LAVA_GENERATOR)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("#O#")
                .define('#', Metals.INVAR.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('O', Blocks.SMOOTH_STONE)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.DIESEL_GENERATOR)
                .pattern("#C#")
                .pattern("/X/")
                .pattern("#B#")
                .define('#', Metals.STEEL.getIngotTag().get())
                .define('C', CraftingItems.CIRCUIT_BOARD)
                .define('/', Ingredient.fromValues(Stream.of(
                        new Ingredient.TagList(Metals.PLATINUM.getNuggetTag().get()),
                        new Ingredient.TagList(Metals.SILVER.getIngotTag().get())
                )))
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('B', Items.BUCKET)
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.BATTERY_BOX)
                .pattern("#B#")
                .pattern("/X/")
                .pattern("L/L")
                .define('#', Metals.ALUMINUM.getIngotTag().get())
                .define('B', ModItems.BATTERY)
                .define('/', ModBlocks.WIRE)
                .define('X', ModBlocks.ALLOY_MACHINE_FRAME)
                .define('L', Metals.LEAD.getIngotTag().get())
                .unlockedBy("has_item", has(ModBlocks.ALLOY_MACHINE_FRAME))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.WIRE, 12)
                .pattern("///")
                .pattern("###")
                .define('/', Ingredient.fromValues(Stream.of(
                        new Ingredient.TagList(Metals.COPPER.getIngotTag().get()),
                        new Ingredient.TagList(Metals.REFINED_IRON.getIngotTag().get())
                )))
                .define('#', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .unlockedBy("has_item", has(Metals.REDSTONE_ALLOY.getIngotTag().get()))
                .save(consumer);
    }

    private void registerItemCrafting(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(CraftingItems.CIRCUIT_BOARD, 3)
                .pattern("/G/")
                .pattern("###")
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('#', Metals.COPPER.getIngotTag().get())
                .unlockedBy("has_item", has(Metals.REDSTONE_ALLOY.getIngotTag().get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(CraftingItems.HEATING_ELEMENT, 2)
                .pattern("##")
                .pattern("##")
                .pattern("/ ")
                .define('#', Metals.COPPER.getIngotTag().get())
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .unlockedBy("has_item", has(Metals.REDSTONE_ALLOY.getIngotTag().get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(CraftingItems.PLASTIC_SHEET)
                .pattern("##")
                .pattern("##")
                .define('#', CraftingItems.PLASTIC_PELLETS)
                .unlockedBy("has_item", has(CraftingItems.PLASTIC_PELLETS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(CraftingItems.UPGRADE_CASE, 2)
                .pattern("###")
                .pattern("###")
                .pattern("///")
                .define('#', ModTags.Items.PLASTIC)
                .define('/', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_item", has(ModTags.Items.PLASTIC))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(MachineUpgrades.PROCESSING_SPEED)
                .requires(CraftingItems.UPGRADE_CASE)
                .requires(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .requires(Metals.SILVER.getIngotTag().get())
                .requires(Metals.SILVER.getIngotTag().get())
                .unlockedBy("has_item", has(CraftingItems.UPGRADE_CASE))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(MachineUpgrades.OUTPUT_CHANCE)
                .requires(CraftingItems.UPGRADE_CASE)
                .requires(Tags.Items.STORAGE_BLOCKS_LAPIS)
                .requires(Metals.PLATINUM.getIngotTag().get())
                .requires(Metals.PLATINUM.getIngotTag().get())
                .unlockedBy("has_item", has(CraftingItems.UPGRADE_CASE))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(MachineUpgrades.ENERGY_EFFICIENCY)
                .requires(CraftingItems.UPGRADE_CASE)
                .requires(Items.GLOWSTONE)
                .requires(Metals.ELECTRUM.getIngotTag().get())
                .requires(Metals.ELECTRUM.getIngotTag().get())
                .unlockedBy("has_item", has(CraftingItems.UPGRADE_CASE))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(MachineUpgrades.RANGE)
                .requires(CraftingItems.UPGRADE_CASE)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(Metals.INVAR.getIngotTag().get())
                .requires(Metals.INVAR.getIngotTag().get())
                .unlockedBy("has_item", has(CraftingItems.UPGRADE_CASE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.WRENCH)
                .pattern("/ /")
                .pattern(" # ")
                .pattern(" / ")
                .define('/', Tags.Items.INGOTS_IRON)
                .define('#', Metals.REFINED_IRON.getIngotTag().get())
                .unlockedBy("has_item", has(Metals.REFINED_IRON.getIngotTag().get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.BATTERY)
                .pattern(" / ")
                .pattern("#X#")
                .pattern("LXL")
                .define('/', Metals.REDSTONE_ALLOY.getIngotTag().get())
                .define('#', Tags.Items.INGOTS_IRON)
                .define('X', Tags.Items.DUSTS_REDSTONE)
                .define('L', Metals.LEAD.getIngotTag().get())
                .unlockedBy("has_item", has(Metals.REDSTONE_ALLOY.getIngotTag().get()))
                .save(consumer);

        ExtendedShapedRecipeBuilder.vanillaBuilder(ModItems.HAND_PUMP)
                .patternLine("/C#")
                .patternLine(" B#")
                .key('/', Metals.ALUMINUM.getIngotTag().get())
                .key('C', ModItems.EMPTY_CANISTER)
                .key('#', ModTags.Items.PLASTIC)
                .key('B', ModItems.BATTERY)
                .build(consumer);

        ExtendedShapedRecipeBuilder.vanillaBuilder(ModItems.EMPTY_CANISTER, 8)
                .patternLine(" # ")
                .patternLine("# #")
                .patternLine(" # ")
                .key('#', Metals.ALUMINUM.getIngotTag().get())
                .build(consumer);

        ExtendedShapelessRecipeBuilder.vanillaBuilder(ModItems.EMPTY_CANISTER)
                .addIngredient(ModItems.CANISTER)
                .build(consumer, SilentMechanisms.getId("canister_clear"));

        ExtendedShapelessRecipeBuilder.vanillaBuilder(Items.LEATHER)
                .addIngredient(CraftingItems.ZOMBIE_LEATHER, 4)
                .build(consumer, SilentMechanisms.getId("leather"));
    }

    private void registerSmelting(Consumer<IFinishedRecipe> consumer) {
        for (Metals metal : Metals.values()) {
            if (metal.getIngot().isPresent() && (metal.getChunksTag().isPresent() || metal.getDustTag().isPresent())) {
                smeltingAndBlasting(consumer, metal.getName() + "_ingot",
                        metal.getSmeltables(false), metal.getIngot().get());
            }
            if (metal.getIngot().isPresent() && metal.getOreItemTag().isPresent()) {
                smeltingAndBlasting(consumer, metal.getName() + "_ingot_from_ore",
                        Ingredient.of(metal.getOreItemTag().get()), metal.getIngot().get());
            }
        }

        smeltingAndBlasting(consumer, "iron_ingot", Metals.IRON.getSmeltables(false), Items.IRON_INGOT);
        smeltingAndBlasting(consumer, "gold_ingot", Metals.GOLD.getSmeltables(false), Items.GOLD_INGOT);

        assert (Metals.REFINED_IRON.getIngot().isPresent());
        smeltingAndBlasting(consumer, "refined_iron_ingot", Ingredient.of(Tags.Items.INGOTS_IRON), Metals.REFINED_IRON.getIngot().get());
    }

    private void smeltingAndBlasting(Consumer<IFinishedRecipe> consumer, String name, Ingredient ingredient, IItemProvider result) {
        CookingRecipeBuilder.smelting(ingredient, result, 1f, 200)
                .unlockedBy("has_item", has(Blocks.FURNACE))
                .save(consumer, SilentMechanisms.getId("smelting/" + name));
        CookingRecipeBuilder.blasting(ingredient, result, 1f, 100)
                .unlockedBy("has_item", has(Blocks.FURNACE))
                .save(consumer, SilentMechanisms.getId("blasting/" + name));
    }

    private static void registerAlloySmelting(Consumer<IFinishedRecipe> consumer) {
        AlloySmeltingRecipeBuilder.builder(Metals.ALUMINUM_STEEL, 4, 600)
                .ingredient(Metals.IRON, 2)
                .ingredient(ModTags.Items.DUSTS_COAL, 3)
                .ingredient(Metals.ALUMINUM, 1)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.BISMUTH_BRASS, 4, 400)
                .ingredient(Metals.COPPER, 2)
                .ingredient(Metals.ZINC, 1)
                .ingredient(Metals.BISMUTH, 1)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.BISMUTH_STEEL, 4, 600)
                .ingredient(Metals.IRON, 2)
                .ingredient(ModTags.Items.DUSTS_COAL, 3)
                .ingredient(Metals.BISMUTH, 1)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.BRASS, 4, 400)
                .ingredient(Metals.COPPER, 3)
                .ingredient(Metals.ZINC, 1)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.BRONZE, 4, 400)
                .ingredient(Metals.COPPER, 3)
                .ingredient(Metals.TIN, 1)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.ELECTRUM, 2, 400)
                .ingredient(Metals.GOLD, 1)
                .ingredient(Metals.SILVER, 1)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.ENDERIUM, 4, 500)
                .ingredient(Metals.LEAD, 3)
                .ingredient(Metals.PLATINUM, 1)
                .ingredient(Tags.Items.ENDER_PEARLS, 4)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.INVAR, 3, 400)
                .ingredient(Metals.IRON, 2)
                .ingredient(Metals.NICKEL, 1)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.LUMIUM, 4, 500)
                .ingredient(Metals.TIN, 3)
                .ingredient(Metals.SILVER, 1)
                .ingredient(Tags.Items.DUSTS_GLOWSTONE, 4)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.REDSTONE_ALLOY, 2, 200)
                .ingredient(Metals.IRON, 1)
                .ingredient(Tags.Items.DUSTS_REDSTONE, 4)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.SIGNALUM, 4, 500)
                .ingredient(Metals.COPPER, 3)
                .ingredient(Metals.SILVER, 1)
                .ingredient(Tags.Items.DUSTS_REDSTONE, 10)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(CraftingItems.SOLDER, 12, 200)
                .ingredient(Metals.TIN, 1)
                .ingredient(Metals.LEAD, 1)
                .build(consumer);
        AlloySmeltingRecipeBuilder.builder(Metals.STEEL, 2, 600)
                .ingredient(Metals.IRON, 2)
                .ingredient(ModTags.Items.DUSTS_COAL, 2)
                .build(consumer);
    }

    private static void registerCompressingRecipes(Consumer<IFinishedRecipe> consumer) {
        CompressingRecipeBuilder.builder(Items.BLAZE_POWDER, 4, Items.BLAZE_ROD, 1, 400)
                .build(consumer);
        assert (Metals.COMPRESSED_IRON.getIngot().isPresent());
        CompressingRecipeBuilder.builder(Tags.Items.INGOTS_IRON, 1, Metals.COMPRESSED_IRON.getIngot().get(), 1, 400)
                .build(consumer);
        CompressingRecipeBuilder.builder(Tags.Items.STORAGE_BLOCKS_COAL, 16, Items.DIAMOND, 1, 800)
                .build(consumer);
    }

    private static void registerCrushingRecipes(Consumer<IFinishedRecipe> consumer) {
        for (Metals metal : Metals.values()) {
            if (metal.getOreItemTag().isPresent() && metal.getChunks().isPresent()) {
                crushingOre(metal.getOreItemTag().get(), metal.getChunks().get(), Blocks.COBBLESTONE)
                        .build(consumer);
            }
            if (metal.getChunksTag().isPresent() && metal.getDust().isPresent()) {
                crushingChunks(metal.getChunksTag().get(), metal.getDust().get())
                        .build(consumer);
            }
            if (metal.getIngotTag().isPresent() && metal.getDust().isPresent()) {
                crushingIngot(metal.getIngotTag().get(), metal.getDust().get())
                        .build(consumer, SilentMechanisms.getId("crushing/" + metal.getName() + "_dust_from_ingot"));
            }
        }

        // Vanilla ores
        CrushingRecipeBuilder.builder(Tags.Items.ORES_COAL, CRUSHING_ORE_TIME)
                .result(Items.COAL, 2)
                .result(Items.COBBLESTONE, 1, CRUSHING_ORE_STONE_CHANCE)
                .result(Items.DIAMOND, 1, 0.001f)
                .build(consumer);
        CrushingRecipeBuilder.builder(Tags.Items.ORES_LAPIS, CRUSHING_ORE_TIME)
                .result(Items.LAPIS_LAZULI, 12)
                .build(consumer);
        CrushingRecipeBuilder.builder(Tags.Items.ORES_REDSTONE, CRUSHING_ORE_TIME)
                .result(Items.REDSTONE, 6)
                .build(consumer);
        crushingOreBonus(Tags.Items.ORES_QUARTZ, Items.QUARTZ).build(consumer);
        crushingOreBonus(Tags.Items.ORES_DIAMOND, Items.DIAMOND).build(consumer);
        crushingOreBonus(Tags.Items.ORES_EMERALD, Items.EMERALD).build(consumer);
        crushingOre(Tags.Items.ORES_GOLD, Metals.GOLD.getChunks().get(), Blocks.COBBLESTONE).build(consumer);
        crushingOre(Blocks.NETHER_GOLD_ORE, Metals.GOLD.getChunks().get(), Blocks.NETHERRACK)
                .build(consumer, SilentMechanisms.getId("crushing/gold_chunks_nether"));
        crushingOre(Tags.Items.ORES_IRON, Metals.IRON.getChunks().get(), Blocks.COBBLESTONE).build(consumer);

        CrushingRecipeBuilder.builder(Blocks.ANCIENT_DEBRIS, 2 * CRUSHING_ORE_TIME)
                .result(Items.NETHERITE_SCRAP, 2)
                .result(Items.NETHERITE_SCRAP, 1, 0.1f)
                .result(Items.NETHERITE_SCRAP, 1, 0.01f)
                .build(consumer);

        // Others
        CrushingRecipeBuilder.builder(Tags.Items.RODS_BLAZE, 200)
                .result(Items.BLAZE_POWDER, 4)
                .build(consumer);
        CrushingRecipeBuilder.builder(Blocks.CLAY, 100)
                .result(Items.CLAY_BALL, 4)
                .build(consumer);
        CrushingRecipeBuilder.builder(Items.COAL, 200)
                .result(CraftingItems.COAL_DUST, 1)
                .build(consumer);
        CrushingRecipeBuilder.builder(Blocks.GLOWSTONE, 100)
                .result(Items.GLOWSTONE_DUST, 4)
                .build(consumer);
        CrushingRecipeBuilder.builder(Tags.Items.COBBLESTONE, 200)
                .result(Blocks.GRAVEL, 1)
                .build(consumer);
        CrushingRecipeBuilder.builder(ItemTags.LOGS, 200)
                .result(Items.PAPER, 1, 0.75f)
                .result(Items.PAPER, 1, 0.25f)
                .result(Items.STICK, 1, 0.25f)
                .result(Items.STICK, 1, 0.25f)
                .build(consumer);
        CrushingRecipeBuilder.builder(
                Ingredient.of(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR, Blocks.CHISELED_QUARTZ_BLOCK, Blocks.SMOOTH_QUARTZ),
                200)
                .result(Items.QUARTZ, 4)
                .build(consumer, SilentMechanisms.getId("crushing/quartz_from_blocks"));
        CrushingRecipeBuilder.builder(Ingredient.of(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE), 200)
                .result(Blocks.RED_SAND, 4)
                .build(consumer, SilentMechanisms.getId("crushing/red_sand_from_sandstone"));
        CrushingRecipeBuilder.builder(Ingredient.of(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE), 200)
                .result(Blocks.SAND, 4)
                .build(consumer, SilentMechanisms.getId("crushing/sand_from_sandstone"));
        CrushingRecipeBuilder.builder(Blocks.GRAVEL, 200)
                .result(Blocks.SAND, 1)
                .result(Items.FLINT, 1, 0.1f)
                .build(consumer);
    }

    private static void registerInfusingRecipes(Consumer<IFinishedRecipe> consumer) {
        FluidIngredient water100mb = new FluidIngredient(FluidTags.WATER, 100);
        InfusingRecipeBuilder.builder(Items.WHITE_CONCRETE, 1, 100, Items.WHITE_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.ORANGE_CONCRETE, 1, 100, Items.ORANGE_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.MAGENTA_CONCRETE, 1, 100, Items.MAGENTA_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.LIGHT_BLUE_CONCRETE, 1, 100, Items.LIGHT_BLUE_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.YELLOW_CONCRETE, 1, 100, Items.YELLOW_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.LIME_CONCRETE, 1, 100, Items.LIME_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.PINK_CONCRETE, 1, 100, Items.PINK_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.GRAY_CONCRETE, 1, 100, Items.GRAY_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.LIGHT_GRAY_CONCRETE, 1, 100, Items.LIGHT_GRAY_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.CYAN_CONCRETE, 1, 100, Items.CYAN_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.PURPLE_CONCRETE, 1, 100, Items.PURPLE_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.BLUE_CONCRETE, 1, 100, Items.BLUE_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.BROWN_CONCRETE, 1, 100, Items.BROWN_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.GREEN_CONCRETE, 1, 100, Items.GREEN_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.RED_CONCRETE, 1, 100, Items.RED_CONCRETE_POWDER, water100mb).build(consumer);
        InfusingRecipeBuilder.builder(Items.BLACK_CONCRETE, 1, 100, Items.BLACK_CONCRETE_POWDER, water100mb).build(consumer);
    }

    public static CrushingRecipeBuilder crushingChunks(ITag<Item> chunks, IItemProvider dust) {
        return CrushingRecipeBuilder.crushingChunks(chunks, dust, CRUSHING_CHUNKS_TIME, CRUSHING_CHUNKS_EXTRA_CHANCE);
    }

    public static CrushingRecipeBuilder crushingIngot(ITag<Item> ingot, IItemProvider dust) {
        return CrushingRecipeBuilder.crushingIngot(ingot, dust, CRUSHING_INGOT_TIME);
    }

    public static CrushingRecipeBuilder crushingOre(ITag<Item> ore, IItemProvider chunks, @Nullable IItemProvider extra) {
        return CrushingRecipeBuilder.crushingOre(ore, chunks, CRUSHING_ORE_TIME, extra, CRUSHING_ORE_STONE_CHANCE);
    }

    public static CrushingRecipeBuilder crushingOre(IItemProvider ore, IItemProvider chunks, @Nullable IItemProvider extra) {
        return CrushingRecipeBuilder.crushingOre(ore, chunks, CRUSHING_ORE_TIME, extra, CRUSHING_ORE_STONE_CHANCE);
    }

    public static CrushingRecipeBuilder crushingOreBonus(ITag<Item> ore, IItemProvider item) {
        return CrushingRecipeBuilder.builder(ore, CRUSHING_ORE_TIME)
                .result(item, 2)
                .result(item, 1, 0.1f)
                .result(Blocks.COBBLESTONE, 1, 0.1f);
    }
}
