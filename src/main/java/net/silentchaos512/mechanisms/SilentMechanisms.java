package net.silentchaos512.mechanisms;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.mechanisms.init.ModBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Random;

@Mod(SilentMechanisms.MOD_ID)
public final class SilentMechanisms {
    public static final String MOD_ID = "silents_mechanisms";
    public static final String MOD_NAME = "Silent's Mechanisms";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final Random RANDOM = new Random();

    public static SilentMechanisms INSTANCE;
    public static IProxy PROXY;

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.CRUSHER);
        }
    };

    public SilentMechanisms() {
        INSTANCE = this;
        PROXY = DistExecutor.safeRunForDist(() -> SideProxy.Client::new, () -> SideProxy.Server::new);
    }

    public static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MOD_ID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "NONE";
    }

    public static boolean isDevBuild() {
        return "NONE".equals(getVersion());
    }


    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
