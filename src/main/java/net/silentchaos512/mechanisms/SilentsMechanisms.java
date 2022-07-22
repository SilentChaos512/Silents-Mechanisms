package net.silentchaos512.mechanisms;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.mechanisms.blocks.dryingracks.DryingRackRenderer;
import net.silentchaos512.mechanisms.blocks.generators.coalgenerator.CoalGeneratorScreen;
import net.silentchaos512.mechanisms.data.loot.ModLootTable;
import net.silentchaos512.mechanisms.data.recipes.ModRecipeProvider;
import net.silentchaos512.mechanisms.data.tag.ModBlockTagProvider;
import net.silentchaos512.mechanisms.data.tag.ModItemTags;
import net.silentchaos512.mechanisms.init.*;
import net.silentchaos512.mechanisms.worldgen.ModFeatures;

@Mod(SilentsMechanisms.MODID)
public class SilentsMechanisms {
    public static final String MODID = "silents_mechanisms";
    public static final CreativeModeTab TAB = new CreativeModeTab("silents_mechanisms") {
        @Override
        public ItemStack makeIcon() {
            //just a placeholder for crusher, will change after it is done
            return new ItemStack(Blocks.FURNACE);
        }
    };

    public SilentsMechanisms() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.init(eventBus);
        ModItems.init(eventBus);
        ModBlockEntities.init(eventBus);
    }

    public static ResourceLocation loc(String loc) {
        return new ResourceLocation(MODID, loc);
    }

    @Mod.EventBusSubscriber(modid = SilentsMechanisms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class SilentsModEvents {
        private SilentsModEvents() {
        }

        @SubscribeEvent
        public static void onDataGeneration(GatherDataEvent event) {
            DataGenerator dataGenerator = event.getGenerator();
            ExistingFileHelper fileHelper = event.getExistingFileHelper();


            ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(dataGenerator, SilentsMechanisms.MODID, fileHelper);
            dataGenerator.addProvider(blockTagProvider);
            dataGenerator.addProvider(new ModItemTags.Provider(dataGenerator, blockTagProvider, SilentsMechanisms.MODID, fileHelper));
            dataGenerator.addProvider(new ModLootTable(dataGenerator));
            dataGenerator.addProvider(new ModRecipeProvider(dataGenerator));
        }

        @SubscribeEvent
        public static void onMenuRegistration(RegistryEvent.Register<MenuType<?>> event) {
            IForgeRegistry<MenuType<?>> registry = event.getRegistry();
            ModMenus.ALL_MENUS.forEach(registry::register);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent clientSetupEvent) {
            BlockEntityRenderers.register(ModBlockEntities.DRYING_RACKS.get(), DryingRackRenderer::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STONE_MACHINE_FRAME.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ALLOY_MACHINE_FRAME.get(), RenderType.cutout());

            MenuScreens.register(ModMenus.COAL_GENERATOR_MENU, CoalGeneratorScreen::new);
        }

        @SubscribeEvent
        public static void onFluidRegistration(RegistryEvent.Register<Fluid> event) {
            ModFluids.registerAllFluids();
        }
    }

    @Mod.EventBusSubscriber(modid = SilentsMechanisms.MODID)
    public static final class SilentsForgeEvents {
        @SubscribeEvent
        public static void onBiomeLoad(BiomeLoadingEvent event) {
            Biome.BiomeCategory category = event.getCategory();
            BiomeGenerationSettingsBuilder generationSettingsBuilder = event.getGeneration();
            if (category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND) {
                ModFeatures.VEINS.forEach(vein -> generationSettingsBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, vein.orePlacedFeature));
                generationSettingsBuilder.addFeature(GenerationStep.Decoration.LAKES, ModFeatures.OIL_LAKE_FEATURE);
            }
        }
    }

}
