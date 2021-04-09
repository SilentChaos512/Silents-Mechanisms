package net.silentchaos512.mechanisms.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.lib.util.Lazy;
import net.silentchaos512.lib.world.placement.DimensionFilterConfig;
import net.silentchaos512.lib.world.placement.LibPlacements;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.config.Config;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.Ores;
import net.silentchaos512.mechanisms.world.feature.OilLakesFeature;

import java.util.ArrayList;
import java.util.Collection;

@Mod.EventBusSubscriber(modid = SilentMechanisms.MOD_ID)
public final class SMWorldFeatures {
    private static final Lazy<ConfiguredFeature<?, ?>> OIL_LAKES_STANDARD = Lazy.of(() -> createOilLakeFeature(1f));
    private static final Lazy<ConfiguredFeature<?, ?>> OIL_LAKES_COMMON = Lazy.of(() -> createOilLakeFeature(0.67f));

    private SMWorldFeatures() {}

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(OilLakesFeature.INSTANCE.setRegistryName(SilentMechanisms.getId("oil_lakes")));

        for (Ores ore : Ores.values()) {
            ore.getConfig().ifPresent(config ->
                    registerConfiguredFeature(ore.getName() + "_vein", ore.getConfiguredFeature()));
        }
        registerConfiguredFeature("oil_lakes_standard", OIL_LAKES_STANDARD.get());
        registerConfiguredFeature("oil_lakes_common", OIL_LAKES_COMMON.get());
    }

    private static void registerConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        SilentMechanisms.LOGGER.debug("register configured feature '{}'", name);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, SilentMechanisms.getId(name), configuredFeature);
    }

    @SubscribeEvent
    public static void addFeaturesToBiomes(BiomeLoadingEvent biome) {
        if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
            for (Ores ore : Ores.values()) {
                ore.getConfig().ifPresent(config -> {
                    if (config.isEnabled()) {
                        addOre(biome, ore);
                    }
                });
            }

            addOilLakes(biome);
        }
    }

    private static ConfiguredFeature<?, ?> createOilLakeFeature(float multi) {
        final int config = Config.worldGenOilLakeChance.get();
        if (config > 0) {
            // Read the blacklist from the config file
            Collection<RegistryKey<World>> blacklist = new ArrayList<>();
            for (String str : Config.worldGenOilDimensionBlacklist.get()) {
                ResourceLocation id = ResourceLocation.tryCreate(str);
                if (id != null) {
                    RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, id);
                    blacklist.add(key);
                }
            }

            return OilLakesFeature.INSTANCE
                    .withConfiguration(new BlockStateFeatureConfig(ModBlocks.OIL.asBlockState()))
                    .withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig((int) (multi * config))))
                    .withPlacement(LibPlacements.DIMENSION_FILTER.configure(new DimensionFilterConfig(false, blacklist)));
        } else {
            return Feature.NO_OP.withConfiguration(new NoFeatureConfig());
        }
    }

    private static void addOilLakes(BiomeLoadingEvent biome) {
        final int config = Config.worldGenOilLakeChance.get();
        if (config > 0 && biome.getName() != null) {
            // Somewhat more common in deserts
            ConfiguredFeature<?, ?> feature = biome.getName().equals(Biomes.DESERT.getRegistryName())
                    ? OIL_LAKES_COMMON.get() : OIL_LAKES_STANDARD.get();
            biome.getGeneration().withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, feature);
        }
    }

    private static void addOre(BiomeLoadingEvent biome, Ores ore) {
        biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore.getConfiguredFeature());
    }
}
