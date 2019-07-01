package net.silentchaos512.mechanisms;

import net.silentchaos512.mechanisms.init.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

class SideProxy {
    SideProxy() {
        SilentMechanisms.LOGGER.debug("SideProxy init");

        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        // Add listeners for registry events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModBlocks::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModItems::registerAll);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        SilentMechanisms.LOGGER.debug("SideProxy commonSetup");
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
        SilentMechanisms.LOGGER.debug("SideProxy imcEnqueue");
    }

    private void imcProcess(InterModProcessEvent event) {
        SilentMechanisms.LOGGER.debug("SideProxy imcProcess");
    }

    static class Client extends SideProxy {
        Client() {
            SilentMechanisms.LOGGER.debug("SideProxy.Client init");
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        }

        private void clientSetup(FMLClientSetupEvent event) {
            SilentMechanisms.LOGGER.debug("SideProxy.Client clientSetup");
        }
    }

    static class Server extends SideProxy {
        Server() {
            SilentMechanisms.LOGGER.debug("SideProxy.Server init");
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
            SilentMechanisms.LOGGER.debug("SideProxy.Server serverSetup");
        }
    }
}
