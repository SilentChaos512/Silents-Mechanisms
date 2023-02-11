package net.silentchaos512.mechanisms;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackRenderer;
import net.silentchaos512.mechanisms.common.blocks.generators.coalgenerator.CoalGeneratorScreen;
import net.silentchaos512.mechanisms.data.client.ModBlockStateProvider;
import net.silentchaos512.mechanisms.data.client.ModItemModelProvider;
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
    }

    public static ResourceLocation location(String loc) {
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

            final boolean includeServer = event.includeServer();

            ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(dataGenerator, SilentsMechanisms.MODID, fileHelper);
            dataGenerator.addProvider(includeServer, blockTagProvider);
            dataGenerator.addProvider(includeServer, new ModItemTags.Provider(dataGenerator, blockTagProvider, SilentsMechanisms.MODID, fileHelper));
            dataGenerator.addProvider(includeServer, new ModLootTable(dataGenerator));
            dataGenerator.addProvider(includeServer, new ModRecipeProvider(dataGenerator));

            dataGenerator.addProvider(includeServer, new ModBlockStateProvider(dataGenerator, fileHelper));
            dataGenerator.addProvider(includeServer, new ModItemModelProvider(dataGenerator, fileHelper));
        }

        @SubscribeEvent
        public static void registerEvent(RegisterEvent event) {
            event.register(ForgeRegistries.Keys.MENU_TYPES, ModMenus::register);
            event.register(ForgeRegistries.Keys.FLUID_TYPES, ModFluids.ALL_FLUID_TYPES::registerAll);
            event.register(ForgeRegistries.Keys.FLUIDS, ModFluids.ALL_FLUIDS::registerAll);
            event.register(ForgeRegistries.Keys.ITEMS, ModItems::registerAllItems);
            event.register(ForgeRegistries.Keys.BLOCKS, ModBlocks.BLOCK_DIRECT_REGISTRY::registerAll);
            event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, ModBlockEntities.DIRECT_BE_TPYES::registerAll);
            event.register(Registry.PLACED_FEATURE_REGISTRY, ModFeatures::init);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent clientSetupEvent) {
            BlockEntityRenderers.register(ModBlockEntities.DRYING_RACKS, DryingRackRenderer::new);
            MenuScreens.register(ModMenus.COAL_GENERATOR_MENU, CoalGeneratorScreen::new);
        }
    }

}
