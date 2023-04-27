package net.silentchaos512.mechanisms.data;


import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.worldgen.OreVeinValues;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

//Content is commented out for 1.19.3 changes
public class ModWorldGen {
    private static void registerFeatures(DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> lookupProvider, final boolean includeServer) {
        dataGenerator.addProvider(includeServer, new DatapackBuiltinEntriesProvider(
                dataGenerator.getPackOutput(),
                lookupProvider,
                new RegistrySetBuilder().add(Registries.CONFIGURED_FEATURE, ModWorldGen::registerConfigs).add(Registries.PLACED_FEATURE, ModWorldGen::registerFeatures),
                Set.of(SilentsMechanisms.MODID)));
    }

    private static void registerFeatures(BootstapContext<PlacedFeature> bootstrap) {
        for (Metals.Ore ore : Metals.Ore.values()) {
            final OreVeinValues values = ore.getOreVeinValues();
            HolderGetter<ConfiguredFeature<?, ?>> getter = bootstrap.lookup(Registries.CONFIGURED_FEATURE);
            PlacementUtils.register(bootstrap, ore.getPlacedFeatureKey(), getter.getOrThrow(ore.getFeatureConfigKey()), orePlacement(values.veinCount(), HeightRangePlacement.uniform(VerticalAnchor.absolute(values.minHeight()), VerticalAnchor.absolute(values.minHeight()))));

        }
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
    }

    private static List<PlacementModifier> orePlacement(int veinsInChunk, PlacementModifier modifier) {
        return List.of(CountPlacement.of(veinsInChunk), InSquarePlacement.spread(), modifier, BiomeFilter.biome());
    }

    public static void register(GatherDataEvent event) {
        registerFeatures(event.getGenerator(), event.getLookupProvider(), event.includeServer());
    }

    /*
    public static void init(DataGenerator generator, ExistingFileHelper existingFileHelper) {

        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());

        Map<ResourceLocation, ConfiguredFeature<?, ?>> features = new LinkedHashMap<>();
        Map<ResourceLocation, PlacedFeature> placedFeatures = new LinkedHashMap<>();

        // Ores
        for (Metals.Ore metal : Metals.Ore.values()) {
            ResourceLocation name = SilentsMechanisms.location(metal.toString() + "_ore");

            ConfiguredFeature<?, ?> feature = createOreConfiguredFeature(metal);
            features.put(name, feature);

            PlacedFeature placedFeature = createOrePlacedFeature(ops, metal, name, feature);
            placedFeatures.put(name, placedFeature);
        }

        HolderSet<Biome> overworldBiomes = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD);

        BiomeModifier overworldOres = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatures.entrySet().stream().map(e -> holderPlaced(e.getValue(), ops, e.getKey())).collect(Collectors.toList())),
                GenerationStep.Decoration.UNDERGROUND_ORES
        );

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

        // Add the actual data providers
        DataProvider configuredFeatureProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, SilentsMechanisms.MODID, ops, Registry.CONFIGURED_FEATURE_REGISTRY,
                features);
        DataProvider placedFeatureProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, SilentsMechanisms.MODID, ops, Registry.PLACED_FEATURE_REGISTRY,
                placedFeatures);
        DataProvider biomeModifierProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, SilentsMechanisms.MODID, ops, ForgeRegistries.Keys.BIOME_MODIFIERS,
                ImmutableMap.of(
                        SilentsMechanisms.location("overworld_ores"), overworldOres,
                        SilentsMechanisms.location("lakes"), lakes
                )
        );

        generator.addProvider(true, configuredFeatureProvider);
        generator.addProvider(true, placedFeatureProvider);
        generator.addProvider(true, biomeModifierProvider);
    }

    @NotNull
    private static ConfiguredFeature<OreConfiguration, Feature<OreConfiguration>> createOreConfiguredFeature(Metals.Ore metal) {
        return new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(
                        Lists.newArrayList(
                                OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, metal.getOreBlock().defaultBlockState()),
                                OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, metal.getDeepslateOreBlock().defaultBlockState())
                        ),
                        metal.oreVeinValues.veinSize()
                )
        );
    }

    @NotNull
    private static PlacedFeature createOrePlacedFeature(RegistryOps<JsonElement> ops, Metals.Ore metal, ResourceLocation name, ConfiguredFeature<?, ?> feature) {
        return new PlacedFeature(
                holder(feature, ops, name),
                commonOrePlacement(
                        metal.oreVeinValues.veinCount(),
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(metal.oreVeinValues.minHeight()),
                                VerticalAnchor.absolute(metal.oreVeinValues.maxHeight())
                        )
                )
        );
    }

    public static Holder<ConfiguredFeature<?, ?>> holder(ConfiguredFeature<?, ?> feature, RegistryOps<JsonElement> ops, ResourceLocation location) {
        return ops.registryAccess.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, location));
    }

    public static Holder<PlacedFeature> holderPlaced(PlacedFeature feature, RegistryOps<JsonElement> ops, ResourceLocation location) {
        return ops.registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY).getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, location));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return Lists.newArrayList(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(count), modifier);
    }
         */
}
