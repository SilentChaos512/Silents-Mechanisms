package net.silentchaos512.mechanisms.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LakesConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.LakeChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.Ores;
import net.silentchaos512.mechanisms.world.feature.OilLakesFeature;

public final class SMWorldFeatures {
    private SMWorldFeatures() {}

    public static void addFeaturesToBiomes() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
                for (Ores ore : Ores.values()) {
                    addOre(biome, ore);
                }

                // Oil lakes, somewhat more common in deserts
                biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(
                        OilLakesFeature.INSTANCE,
                        new LakesConfig(ModBlocks.oil.getDefaultState()),
                        Placement.WATER_LAKE,
                        new LakeChanceConfig(biome == Biomes.DESERT ? 4 : 6)
                ));
            }
        }
    }

    private static void addOre(Biome biome, Ores ore) {
        ore.getConfig().ifPresent(config -> {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                    Feature.ORE,
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            ore.getBlock().getDefaultState(),
                            config.getVeinSize()
                    ),
                    Placement.COUNT_RANGE,
                    new CountRangeConfig(config.getVeinCount(), config.getMinHeight(), 0, config.getMaxHeight())
            ));
        });
    }
}
