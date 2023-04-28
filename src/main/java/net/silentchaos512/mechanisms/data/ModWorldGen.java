package net.silentchaos512.mechanisms.data;


import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.worldgen.OreVeinValues;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
public class ModWorldGen {
    public static final ResourceKey<PlacedFeature> OIL_LAKE_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, SilentsMechanisms.location("oil_lake"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> OIL_LAKE_CONFIG = ResourceKey.create(Registries.CONFIGURED_FEATURE, SilentsMechanisms.location("oil_lake_config"));

    private static void generate(DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> lookupProvider, final boolean includeServer) {
        dataGenerator.addProvider(includeServer, new DatapackBuiltinEntriesProvider(
                dataGenerator.getPackOutput(),
                lookupProvider,
                new RegistrySetBuilder().add(Registries.CONFIGURED_FEATURE, ModWorldGen::registerConfigs).add(Registries.PLACED_FEATURE, ModWorldGen::registerFeature),
                Set.of(SilentsMechanisms.MODID)));
    }

    private static void registerFeature(BootstapContext<PlacedFeature> bootstrap) {
        HolderGetter<ConfiguredFeature<?, ?>> getter = bootstrap.lookup(Registries.CONFIGURED_FEATURE);

        for (Metals.Ore ore : Metals.Ore.values()) {
            final OreVeinValues values = ore.getOreVeinValues();
            PlacementUtils.register(bootstrap, ore.getPlacedFeatureKey(), getter.getOrThrow(ore.getFeatureConfigKey()), orePlacement(values.veinCount(), HeightRangePlacement.triangle(VerticalAnchor.absolute(values.minHeight()), VerticalAnchor.absolute(values.minHeight()))));
        }

        PlacementUtils.register(bootstrap, OIL_LAKE_FEATURE, getter.getOrThrow(OIL_LAKE_CONFIG), RarityFilter.onAverageOnceEvery(7), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(70)));
    }

    private static void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> bootstrap) {
        for (Metals.Ore ore : Metals.Ore.values()) {
            final OreVeinValues values = ore.getOreVeinValues();
            final TagMatchTest stoneRule = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
            final TagMatchTest deepslateRule = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

            final OreConfiguration.TargetBlockState normal = OreConfiguration.target(stoneRule, ore.getOreBlock().defaultBlockState());
            final OreConfiguration.TargetBlockState deepslate = OreConfiguration.target(deepslateRule, ore.getDeepslateOreBlock().defaultBlockState());

            FeatureUtils.register(bootstrap, ore.getFeatureConfigKey(), Feature.ORE, new OreConfiguration(List.of(normal, deepslate), values.veinSize()));
        }

        FeatureUtils.register(bootstrap, OIL_LAKE_CONFIG, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(ModBlocks.FLUID_OIL), BlockStateProvider.simple(Blocks.STONE)));
    }
    /*
           // Oil lakes
           ResourceLocation oilLakeName = SilentsMechanisms.location("oil_lake");
           ConfiguredFeature<?, ?> oilLakeFeature = new ConfiguredFeature<>(Feature.LAKE,
                   new LakeFeature.Configuration(BlockStateProvider.simple(ModBlocks.FLUID_OIL), BlockStateProvider.simple(Blocks.STONE.defaultBlockState()))
           );
           features.put(oilLakeName, oilLakeFeature);
           PlacedFeature oilLakePlaced = new PlacedFeature(holder(oilLakeFeature, ops, oilLakeName), Lists.newArrayList(
                   RarityFilter.onAverageOnceEvery(7),
                   InSquarePlacement.spread(),
                   HeightRangePlacement.uniform(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(70))
           ));
           placedFeatures.put(oilLakeName, oilLakePlaced);

           BiomeModifier lakes = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                   overworldBiomes,
                   HolderSet.direct(Lists.newArrayList(holderPlaced(oilLakePlaced, ops, oilLakeName))),
                   GenerationStep.Decoration.LAKES
           );
            */
    private static List<PlacementModifier> orePlacement(int veinsInChunk, PlacementModifier modifier) {
        return List.of(CountPlacement.of(veinsInChunk), InSquarePlacement.spread(), modifier, BiomeFilter.biome());
    }

    public static void register(GatherDataEvent event) {
        generate(event.getGenerator(), event.getLookupProvider(), event.includeServer());
    }


}
