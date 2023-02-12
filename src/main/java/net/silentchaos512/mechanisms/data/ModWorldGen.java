package net.silentchaos512.mechanisms.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModWorldGen {
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
}
