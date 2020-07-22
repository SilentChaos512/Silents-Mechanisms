package net.silentchaos512.mechanisms.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.silentchaos512.mechanisms.data.client.ModBlockStateProvider;
import net.silentchaos512.mechanisms.data.client.ModItemModelProvider;
import net.silentchaos512.mechanisms.data.loot.ModLootTableProvider;
import net.silentchaos512.mechanisms.data.recipes.ModRecipesProvider;

public final class DataGenerators {
    private DataGenerators() {}

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen);
        gen.addProvider(blockTags);
        gen.addProvider(new ModItemTagsProvider(gen, blockTags));
        gen.addProvider(new ModRecipesProvider(gen));
        gen.addProvider(new ModLootTableProvider(gen));

        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
    }
}
