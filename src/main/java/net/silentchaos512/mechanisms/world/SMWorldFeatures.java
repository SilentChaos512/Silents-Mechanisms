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
import net.silentchaos512.mechanisms.config.Config;
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

                addOilLakes(biome);
            }
        }
    }

    private static void addOilLakes(Biome biome) {
        final int config = Config.COMMON.worldGenOilLakeChance.get();
        if (config > 0) {
            // Somewhat more common in deserts
            final int chance = biome == Biomes.DESERT ? 2 * config / 3 : config;
            biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(
                    OilLakesFeature.INSTANCE,
                    new LakesConfig(ModBlocks.oil.getDefaultState()),
                    Placement.WATER_LAKE,
                    new LakeChanceConfig(chance)
            ));
        }
    }

    private static void addOre(Biome biome, Ores ore) {
        ore.getConfig().ifPresent(config -> {
            if (config.isEnabled()) {
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
            }
        });
    }
}
