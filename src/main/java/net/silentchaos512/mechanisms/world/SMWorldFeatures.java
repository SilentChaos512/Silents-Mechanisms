package net.silentchaos512.mechanisms.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.config.Config;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.Ores;
import net.silentchaos512.mechanisms.world.feature.OilLakesFeature;

@Mod.EventBusSubscriber(modid = SilentMechanisms.MOD_ID)
public final class SMWorldFeatures {
    private SMWorldFeatures() {}

    @SubscribeEvent
    public static void addFeaturesToBiomes(BiomeLoadingEvent biome) {
        if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
            for (Ores ore : Ores.values()) {
                addOre(biome, ore);
            }

            addOilLakes(biome);
        }
    }

    private static void addOilLakes(BiomeLoadingEvent biome) {
        final int config = Config.worldGenOilLakeChance.get();
        if (config > 0) {
            // Somewhat more common in deserts
            final int chance = biome.getName().equals(Biomes.DESERT.getRegistryName()) ? 2 * config / 3 : config;
            biome.getGeneration().withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, OilLakesFeature.INSTANCE
                    .withConfiguration(new BlockStateFeatureConfig(ModBlocks.OIL.asBlockState()))
                    .withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(chance)))
            );
        }
    }

    private static void addOre(BiomeLoadingEvent biome, Ores ore) {
        ore.getConfig().ifPresent(config -> {
            if (config.isEnabled()) {
                int bottom = config.getMinHeight();
                biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                        .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, ore.asBlockState(), config.getVeinSize()))
                        .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(bottom, bottom, config.getMaxHeight())))
                        .func_242731_b(config.getVeinCount())
                );
            }
        });
    }
}
