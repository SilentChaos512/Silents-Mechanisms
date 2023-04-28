package net.silentchaos512.mechanisms;


import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.silentchaos512.mechanisms.common.blocks.batterybox.BatteryBoxScreen;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackRenderer;
import net.silentchaos512.mechanisms.common.blocks.generators.coalgenerator.CoalGeneratorScreen;
import net.silentchaos512.mechanisms.data.client.ModBlockStateProvider;
import net.silentchaos512.mechanisms.data.client.ModItemModelProvider;
import net.silentchaos512.mechanisms.data.loot.ModLootTableProvider;
import net.silentchaos512.mechanisms.data.recipes.ModRecipeProvider;
import net.silentchaos512.mechanisms.data.server.SMBiomeModifiers;
import net.silentchaos512.mechanisms.data.tag.ModBlockTagProvider;
import net.silentchaos512.mechanisms.data.tag.ModItemTags;
import net.silentchaos512.mechanisms.init.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = SilentsMechanisms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class SMModEvents {
    private SMModEvents() {
    }

    @SubscribeEvent
    public static void onDataGeneration(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();
        final boolean includeServer = event.includeServer();

        ModBlockTagProvider blockTagProvider = dataGenerator.addProvider(includeServer, new ModBlockTagProvider(dataGenerator, lookupProvider, SilentsMechanisms.MODID, fileHelper));
        dataGenerator.addProvider(includeServer, new ModItemTags.Provider(dataGenerator, lookupProvider, blockTagProvider, SilentsMechanisms.MODID, fileHelper));
        dataGenerator.addProvider(includeServer, new ModLootTableProvider(dataGenerator));
        dataGenerator.addProvider(includeServer, new ModRecipeProvider(dataGenerator));
        //dataGenerator.addProvider(includeServer, new JsonCodecProvider<>(dataGenerator.getPackOutput(), fileHelper, SilentsMechanisms.MODID, JsonOps.INSTANCE, PackType.SERVER_DATA, "forge/biome_modifiers", ForgeMod.ADD_FEATURES_BIOME_MODIFIER_TYPE.get(), SMBiomeModifiers.get()));
        dataGenerator.addProvider(includeServer, (DataProvider.Factory<SMBiomeModifiers>) SMBiomeModifiers::new);

        final boolean includeClient = event.includeClient();
        ModBlockStateProvider blockStateProvider = dataGenerator.addProvider(includeClient, new ModBlockStateProvider(dataGenerator, fileHelper));
        dataGenerator.addProvider(includeClient, new ModItemModelProvider(dataGenerator, fileHelper));
    }

    private static void registerModifier(DataGenerator dataGenerator, ExistingFileHelper fileHelper, boolean includeServer) {
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
        HolderSet<Biome> biome = ops.getter(Registries.BIOME).orElseThrow().getOrThrow(BiomeTags.IS_OVERWORLD);
        HolderSet<PlacedFeature> ores = HolderSet.direct(Arrays.stream(Metals.Ore.values()).map(ore -> Holder.Reference.createStandAlone(null, ore.getPlacedFeatureKey())).collect(Collectors.toList()));

        dataGenerator.addProvider(includeServer, new JsonCodecProvider<>(dataGenerator.getPackOutput(), fileHelper, SilentsMechanisms.MODID, JsonOps.INSTANCE, PackType.SERVER_DATA, "forge/biome_modifier", ForgeMod.ADD_FEATURES_BIOME_MODIFIER_TYPE.get(),
                Map.of(SilentsMechanisms.location("ore_gen"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biome, ores, GenerationStep.Decoration.UNDERGROUND_ORES))));
    }

    @SubscribeEvent
    public static void registerEvent(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.MENU_TYPES, ModMenus::register);
        event.register(ForgeRegistries.Keys.FLUID_TYPES, ModFluids.ALL_FLUID_TYPES::registerAll);
        event.register(ForgeRegistries.Keys.FLUIDS, ModFluids.ALL_FLUIDS::registerAll);
        event.register(ForgeRegistries.Keys.BLOCKS, ModBlocks.BLOCK_DIRECT_REGISTRY::registerAll);
        event.register(ForgeRegistries.Keys.ITEMS, ModItems::registerAllItems);
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, ModBlockEntities.DIRECT_BE_TYPES::registerAll);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent clientSetupEvent) {
        BlockEntityRenderers.register(ModBlockEntities.DRYING_RACKS, DryingRackRenderer::new);

        MenuScreens.register(ModMenus.COAL_GENERATOR_MENU, CoalGeneratorScreen::new);
        MenuScreens.register(ModMenus.BATTERY_BOX, BatteryBoxScreen::new);
    }
}