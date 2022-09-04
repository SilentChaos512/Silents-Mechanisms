package net.silentchaos512.mechanisms.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;

import java.util.List;

@SuppressWarnings("unused")
public class ModFeatures {

    // ====== ORES
    private static final OreVein ORE_TIN = new OreVein(Metals.Ore.TIN);
    private static final OreVein ORE_SILVER = new OreVein(Metals.Ore.SILVER);
    private static final OreVein ORE_LEAD = new OreVein(Metals.Ore.LEAD);
    private static final OreVein ORE_NICKEL = new OreVein(Metals.Ore.NICKEL);
    private static final OreVein ORE_PLATINUM = new OreVein(Metals.Ore.PLATINUM);
    private static final OreVein ORE_ZINC = new OreVein(Metals.Ore.ZINC);
    private static final OreVein ORE_BISMUTH = new OreVein(Metals.Ore.BISMUTH);
    private static final OreVein ORE_BAUXITE = new OreVein(Metals.Ore.BAUXITE);
    private static final OreVein ORE_URANIUM = new OreVein(Metals.Ore.URANIUM);
    public static final List<OreVein> VEINS = ImmutableList.of(ORE_TIN, ORE_SILVER, ORE_LEAD, ORE_NICKEL, ORE_PLATINUM, ORE_ZINC, ORE_BISMUTH, ORE_BAUXITE, ORE_URANIUM);

    //===== OIL LAKES

    //private static final Holder<ConfiguredFeature<LakeFeature.Configuration, ?>> OIL_LAKE_CONFIG = FeatureUtils.register("silents_mechanisms:oil_lake", Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(ModBlocks.FLUID_OIL.get()), BlockStateProvider.simple(Blocks.STONE)));
    //public static final Holder<PlacedFeature> OIL_LAKE_FEATURE = PlacementUtils.register("silents_mechanisms:oil_lake_surface", OIL_LAKE_CONFIG, RarityFilter.onAverageOnceEvery(7), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(128)));

    public ModFeatures() {
    }

    public static void init() {
    }


    public static final class OreVein {
        public final Holder<PlacedFeature> orePlacedFeature;

        public OreVein(Metals.Ore ore) {
            List<OreConfiguration.TargetBlockState> targetBlockStates = ImmutableList.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ALL_ORE_BLOCKS.get(ore).defaultBlockState()));
            Holder<ConfiguredFeature<OreConfiguration, ?>> configuredFeatureHolder = FeatureUtils.register("ore_" + ore.name().toLowerCase(), Feature.ORE, new OreConfiguration(targetBlockStates, ore.oreVeinValues.veinSize()));
            this.orePlacedFeature = PlacementUtils.register("ore" + ore.name().toLowerCase(), configuredFeatureHolder, commonOrePlacement(ore.oreVeinValues.veinCount(), HeightRangePlacement.uniform(VerticalAnchor.absolute(ore.oreVeinValues.maxHeight()), VerticalAnchor.absolute(ore.oreVeinValues.minHeight()))));
        }

        private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
            return ImmutableList.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
        }

        private static List<PlacementModifier> commonOrePlacement(int veinCount, PlacementModifier modifier) {
            return orePlacement(CountPlacement.of(veinCount), modifier);
        }
    }
}
