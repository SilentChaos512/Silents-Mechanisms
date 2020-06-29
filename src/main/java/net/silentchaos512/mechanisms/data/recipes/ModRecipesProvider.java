package net.silentchaos512.mechanisms.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ModRecipesProvider extends RecipeProvider {
    private static final int CRUSHING_ORE_TIME = 400;
    private static final float CRUSHING_ORE_STONE_CHANCE = 0.1f;
    private static final int CRUSHING_CHUNKS_TIME = 300;
    private static final float CRUSHING_CHUNKS_DUST_CHANCE = 0.1f;

    public ModRecipesProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return "Silent's Mechanisms - Recipes";
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        CrushingRecipeBuilder.builder(Tags.Items.ORES_COAL, CRUSHING_ORE_TIME)
                .result(Items.COAL, 2)
                .result(Items.COBBLESTONE, 1, CRUSHING_ORE_STONE_CHANCE)
                .result(Items.DIAMOND, 1, 0.001f)
                .build(consumer);
    }

    protected CrushingRecipeBuilder crushingChunks(Tag<Item> chunks, IItemProvider dust, int processTime, float extraChance) {
        return CrushingRecipeBuilder.builder(chunks, processTime)
                .result(dust, 1)
                .result(dust, 1, extraChance);
    }
}
