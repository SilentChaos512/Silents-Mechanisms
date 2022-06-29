package net.silentchaos512.mechanisms;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.silentchaos512.mechanisms.data.tag.ModBlockTagProvider;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.worldgen.ModOreFeatures;

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
        //ModOreFeatures.init();
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

            dataGenerator.addProvider(new ModBlockTagProvider(dataGenerator, SilentsMechanisms.MODID, fileHelper));
        }


    }

    @Mod.EventBusSubscriber(modid = SilentsMechanisms.MODID)
    public static final class SilentsForgeEvents {
        @SubscribeEvent
        public static void onBiomeLoad(BiomeLoadingEvent event) {
            Biome.BiomeCategory category = event.getCategory();
            if (category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND) {
                ModOreFeatures.VEINS.forEach(vein -> event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, vein.orePlacedFeature));
            }
        }
    }

}