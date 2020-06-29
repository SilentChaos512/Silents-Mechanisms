package net.silentchaos512.mechanisms.data.recipes;

import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModTags;
import net.silentchaos512.mechanisms.item.CraftingItems;

import javax.annotation.Nullable;
import java.util.function.Consumer;

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
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerSmelting(consumer);
        registerAlloySmelting(consumer);
        registerCrushingRecipes(consumer);
    }

    private void registerSmelting(Consumer<IFinishedRecipe> consumer) {
        for (Metals metal : Metals.values()) {
            if (metal.getIngot().isPresent() && (metal.getChunksTag().isPresent() || metal.getDustTag().isPresent())) {
                CookingRecipeBuilder.smeltingRecipe(metal.getSmeltables(false), metal.getIngot().get(), 1f, 200)
                        .addCriterion("has_item", hasItem(Blocks.FURNACE))
                        .build(consumer, SilentMechanisms.getId("smelting/" + metal.getName() + "_ingot"));
                CookingRecipeBuilder.blastingRecipe(metal.getSmeltables(false), metal.getIngot().get(), 1f, 100)
                        .addCriterion("has_item", hasItem(Blocks.FURNACE))
                        .build(consumer, SilentMechanisms.getId("blasting/" + metal.getName() + "_ingot"));
            }
            if (metal.getIngot().isPresent() && metal.getOreTag().isPresent()) {
                CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(metal.getOreItemTag().get()), metal.getIngot().get(), 1f, 200)
                        .addCriterion("has_item", hasItem(Blocks.FURNACE))
                        .build(consumer, SilentMechanisms.getId("smelting/" + metal.getName() + "_ingot_from_ore"));
                CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(metal.getOreItemTag().get()), metal.getIngot().get(), 1f, 100)
                        .addCriterion("has_item", hasItem(Blocks.FURNACE))
                        .build(consumer, SilentMechanisms.getId("blasting/" + metal.getName() + "_ingot_from_ore"));
            }
        }
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
        crushingOre(Tags.Items.ORES_IRON, Metals.IRON.getChunks().get(), Blocks.COBBLESTONE).build(consumer);

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
                Ingredient.fromItems(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR, Blocks.CHISELED_QUARTZ_BLOCK, Blocks.SMOOTH_QUARTZ),
                200)
                .result(Items.QUARTZ, 4)
                .build(consumer, SilentMechanisms.getId("crushing/quartz_from_blocks"));
        CrushingRecipeBuilder.builder(Ingredient.fromItems(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE), 200)
                .result(Blocks.RED_SAND, 4)
                .build(consumer, SilentMechanisms.getId("crushing/red_sand_from_sandstone"));
        CrushingRecipeBuilder.builder(Ingredient.fromItems(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE), 200)
                .result(Blocks.SAND, 4)
                .build(consumer, SilentMechanisms.getId("crushing/sand_from_sandstone"));
        CrushingRecipeBuilder.builder(Blocks.GRAVEL, 200)
                .result(Blocks.SAND, 1)
                .result(Items.FLINT, 1, 0.1f)
                .build(consumer);
    }

    public static CrushingRecipeBuilder crushingChunks(Tag<Item> chunks, IItemProvider dust) {
        return CrushingRecipeBuilder.crushingChunks(chunks, dust, CRUSHING_CHUNKS_TIME, CRUSHING_CHUNKS_EXTRA_CHANCE);
    }

    public static CrushingRecipeBuilder crushingIngot(Tag<Item> ingot, IItemProvider dust) {
        return CrushingRecipeBuilder.crushingIngot(ingot, dust, CRUSHING_INGOT_TIME);
    }

    public static CrushingRecipeBuilder crushingOre(Tag<Item> ore, IItemProvider chunks, @Nullable IItemProvider extra) {
        return CrushingRecipeBuilder.crushingOre(ore, chunks, CRUSHING_ORE_TIME, extra, CRUSHING_ORE_STONE_CHANCE);
    }

    public static CrushingRecipeBuilder crushingOreBonus(Tag<Item> ore, IItemProvider item) {
        return CrushingRecipeBuilder.builder(ore, CRUSHING_ORE_TIME)
                .result(item, 2)
                .result(item, 1, 0.1f)
                .result(Blocks.COBBLESTONE, 1, 0.1f);
    }
}
