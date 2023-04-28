package net.silentchaos512.mechanisms;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silentchaos512.mechanisms.data.ModWorldGen;
import net.silentchaos512.mechanisms.init.SMCreativeTab;
import net.silentchaos512.mechanisms.recipes.SMRecipeCategories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SilentsMechanisms.MODID)
public class SilentsMechanisms {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "silents_mechanisms";

    public SilentsMechanisms() {
        MinecraftForge.EVENT_BUS.register(this);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(SMCreativeTab::registerTab);
        bus.addListener(ModWorldGen::register);
        SMRecipeCategories.registerBus(bus);
    }

    public static ResourceLocation location(String loc) {
        return new ResourceLocation(MODID, loc);
    }
}
