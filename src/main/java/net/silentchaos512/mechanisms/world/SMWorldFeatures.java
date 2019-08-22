package net.silentchaos512.mechanisms.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.config.Config;
import net.silentchaos512.mechanisms.init.Ores;

public final class SMWorldFeatures {
    private SMWorldFeatures() {}

    public static void addFeaturesToBiomes() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            for (Ores ore : Ores.values()) {
                Config.COMMON.getOreConfig(ore).ifPresent(config -> {
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(
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
    }
}
