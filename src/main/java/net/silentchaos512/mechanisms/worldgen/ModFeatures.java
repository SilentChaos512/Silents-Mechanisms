package net.silentchaos512.mechanisms.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.RegisterEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.registration.DirectRegistry;

import java.util.List;

@SuppressWarnings({"unused", "deprecation"})
public class ModFeatures {

    public static final DirectRegistry<PlacedFeature> ALL_FEATURES = new DirectRegistry<>(true);

    public ModFeatures() {
    }

    public static void init(RegisterEvent.RegisterHelper<PlacedFeature> ignored) {
        FeatureConfigs.init();
        Features.init();
    }

    private static String toModLoc(String path) {
        return SilentsMechanisms.MODID + ":" + path;
    }


    public static final class OreVein {
        public final Holder<PlacedFeature> orePlacedFeature;

        public OreVein(Metals.Ore ore) {
            List<OreConfiguration.TargetBlockState> targetBlockStates = ImmutableList.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ALL_ORE_BLOCKS.get(ore).defaultBlockState()));
            Holder<ConfiguredFeature<OreConfiguration, ?>> configuredFeatureHolder = FeatureUtils.register(SilentsMechanisms.MODID + ":ore_" + ore.name().toLowerCase() + "_config", Feature.ORE, new OreConfiguration(targetBlockStates, ore.oreVeinValues.veinSize()));
            final String id = "ore_" + ore.name().toLowerCase();
            this.orePlacedFeature = PlacementUtils.register(SilentsMechanisms.MODID + ":" + id, configuredFeatureHolder, commonOrePlacement(ore.oreVeinValues.veinCount(), HeightRangePlacement.triangle(VerticalAnchor.absolute(ore.oreVeinValues.minHeight()), VerticalAnchor.absolute(ore.oreVeinValues.maxHeight()))));
        }

        private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
            return ImmutableList.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
        }

        private static List<PlacementModifier> commonOrePlacement(int veinCount, PlacementModifier modifier) {
            return orePlacement(CountPlacement.of(veinCount), modifier);
        }

        public Holder<PlacedFeature> getOrePlacedFeature() {
            return orePlacedFeature;
        }
    }

    public static final class FeatureConfigs {
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_TIN = register(Metals.Ore.TIN, ModBlocks.TIN_ORE);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_SILVER = register(Metals.Ore.SILVER, ModBlocks.SILVER_ORE);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_LEAD = register(Metals.Ore.LEAD, ModBlocks.LEAD_ORE);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_NICKEL = register(Metals.Ore.NICKEL, ModBlocks.NICKEL_ORE);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_PLATINUM = register(Metals.Ore.PLATINUM, ModBlocks.PLATINUM_ORE);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_ZINC = register(Metals.Ore.ZINC, ModBlocks.ZINC_ORE);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_BISMUTH = register(Metals.Ore.BISMUTH, ModBlocks.BISMUTH_ORE);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_BAUXITE = register(Metals.Ore.BAUXITE, ModBlocks.BAUXITE_ORE);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_URANIUM = register(Metals.Ore.URANIUM, ModBlocks.URANIUM_ORE);

        public static final Holder<ConfiguredFeature<LakeFeature.Configuration, ?>> OIL_LAKE = FeatureUtils.register(toModLoc("oil_lake"), Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(ModBlocks.FLUID_OIL), BlockStateProvider.simple(Blocks.STONE.defaultBlockState())));

        private static Holder<ConfiguredFeature<OreConfiguration, ?>> register(Metals.Ore oreType, Block oreBlock) {
            return FeatureUtils.register(toModLoc(oreType.toString() + "_ore_config"), Feature.ORE, new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, oreBlock.defaultBlockState(), oreType.getOreVeinValues().veinSize()));
        }

        private static void init() {
        }
    }

    public static final class Features {
        public static final Holder<PlacedFeature> ORE_TIN = registerOre(Metals.Ore.TIN, FeatureConfigs.ORE_TIN);
        public static final Holder<PlacedFeature> ORE_SILVER = registerOre(Metals.Ore.SILVER, FeatureConfigs.ORE_SILVER);
        public static final Holder<PlacedFeature> ORE_LEAD = registerOre(Metals.Ore.LEAD, FeatureConfigs.ORE_LEAD);
        public static final Holder<PlacedFeature> ORE_NICKEL = registerOre(Metals.Ore.NICKEL, FeatureConfigs.ORE_NICKEL);
        public static final Holder<PlacedFeature> ORE_PLATINUM = registerOre(Metals.Ore.PLATINUM, FeatureConfigs.ORE_PLATINUM);
        public static final Holder<PlacedFeature> ORE_BISMUTH = registerOre(Metals.Ore.BISMUTH, FeatureConfigs.ORE_BISMUTH);
        public static final Holder<PlacedFeature> ORE_URANIUM = registerOre(Metals.Ore.URANIUM, FeatureConfigs.ORE_URANIUM);
        public static final Holder<PlacedFeature> ORE_BAUXITE = registerOre(Metals.Ore.BAUXITE, FeatureConfigs.ORE_BAUXITE);
        public static final Holder<PlacedFeature> ORE_ZINC = registerOre(Metals.Ore.ZINC, FeatureConfigs.ORE_ZINC);
        public static final Holder<PlacedFeature> OIL_LAKE = PlacementUtils.register(toModLoc("oil_lake"), FeatureConfigs.OIL_LAKE, RarityFilter.onAverageOnceEvery(7), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(128)));

        private static Holder<PlacedFeature> registerOre(Metals.Ore oreType, Holder<ConfiguredFeature<OreConfiguration, ?>> oreConfig) {
            return PlacementUtils.register( toModLoc("ore_" + oreType.toString()), oreConfig, CountPlacement.of(oreType.getOreVeinValues().veinCount()), HeightRangePlacement.triangle(VerticalAnchor.absolute(oreType.getOreVeinValues().minHeight()), VerticalAnchor.absolute(oreType.getOreVeinValues().maxHeight())));
        }

        private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
            return ImmutableList.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
        }

        private static List<PlacementModifier> commonOrePlacement(int veinCount, PlacementModifier modifier) {
            return orePlacement(CountPlacement.of(veinCount), modifier);
        }

        private static void init() {
        }
    }
}
