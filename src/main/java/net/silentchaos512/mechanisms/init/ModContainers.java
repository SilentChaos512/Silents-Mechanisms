package net.silentchaos512.mechanisms.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.crusher.CrusherContainer;
import net.silentchaos512.mechanisms.block.crusher.CrusherScreen;

public class ModContainers {
    public static ContainerType<CrusherContainer> crusher;

    public static void registerAll(RegistryEvent.Register<ContainerType<?>> event) {
        crusher = register("crusher", CrusherContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(crusher, CrusherScreen::new);
    }

    private static <C extends Container> ContainerType<C> register(String name, ContainerType.IFactory<C> containerFactory) {
        ContainerType<C> type = new ContainerType<>(containerFactory);
        type.setRegistryName(SilentMechanisms.getId(name));
        ForgeRegistries.CONTAINERS.register(type);
        return type;
    }
}
