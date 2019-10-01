package net.silentchaos512.mechanisms;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silentchaos512.lib.event.Greetings;
import net.silentchaos512.lib.util.generator.ModelGenerator;
import net.silentchaos512.mechanisms.compat.computercraft.SMechComputerCraftCompat;
import net.silentchaos512.mechanisms.config.Config;
import net.silentchaos512.mechanisms.init.*;
import net.silentchaos512.mechanisms.item.CraftingItems;
import net.silentchaos512.mechanisms.network.Network;
import net.silentchaos512.mechanisms.world.SMWorldFeatures;

import java.util.Arrays;

class SideProxy implements IProxy {
    private MinecraftServer server = null;

    SideProxy() {
        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        // Add listeners for registry events
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, ModBlocks::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, ModContainers::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Fluid.class, ModFluids::registerFluids);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, ModItems::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, ModTileEntities::registerAll);

        // Other events
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);

        Config.init();
        Network.init();

        ModRecipes.init();

        Greetings.addMessage(p -> new StringTextComponent("Thanks for trying Silent's Mechanisms! This mod is early in development, expect bugs and changes."));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(SMWorldFeatures::addFeaturesToBiomes);

        if (ModList.get().isLoaded("computercraft")) {
            SMechComputerCraftCompat.init();
        }

        if (SilentMechanisms.isDevBuild()) {
            Arrays.stream(CraftingItems.values()).forEach(c -> ModelGenerator.create(c.asItem()));
            Arrays.stream(Metals.values()).forEach(m -> ModelGenerator.create(m.asBlock()));
        }
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
    }

    private void imcProcess(InterModProcessEvent event) {
    }

    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        server = event.getServer();
        IReloadableResourceManager resourceManager = server.getResourceManager();
//        resourceManager.addReloadListener(RefiningRecipeManager.INSTANCE);
    }

    @Override
    public MinecraftServer getServer() {
        return server;
    }

    static class Client extends SideProxy {
        Client() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ModItems::registerItemColors);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        }

        private void clientSetup(FMLClientSetupEvent event) {
            ModContainers.registerScreens(event);
            ModTileEntities.registerRenderers(event);
        }
    }

    static class Server extends SideProxy {
        Server() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
        }
    }
}
