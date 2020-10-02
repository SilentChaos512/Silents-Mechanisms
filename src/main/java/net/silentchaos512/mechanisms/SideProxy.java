package net.silentchaos512.mechanisms;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silentchaos512.lib.event.Greetings;
import net.silentchaos512.mechanisms.client.ModModelProperties;
import net.silentchaos512.mechanisms.compat.computercraft.SMechComputerCraftCompat;
import net.silentchaos512.mechanisms.config.Config;
import net.silentchaos512.mechanisms.data.DataGenerators;
import net.silentchaos512.mechanisms.init.*;
import net.silentchaos512.mechanisms.network.Network;

import javax.annotation.Nullable;

class SideProxy implements IProxy {
    private MinecraftServer server = null;

    SideProxy() {
        Config.init();
        Network.init();
        Registration.register();

        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerators::gatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        // Add listeners for registry events
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, ModContainers::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Fluid.class, ModFluids::registerFluids);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, ModTileEntities::registerAll);

        // Other events
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);


        ModRecipes.init();

        Greetings.addMessage(SideProxy::getBetaWelcomeMessage);
        Greetings.addMessage(ModBlocks::checkForMissingLootTables);
    }

    @Nullable
    private static ITextComponent getBetaWelcomeMessage(PlayerEntity p) {
        return Config.showBetaWelcomeMessage.get()
                ? new StringTextComponent("Thanks for trying Silent's Mechanisms! This mod is early in development, expect bugs and changes.")
                : null;
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("computercraft")) {
            SMechComputerCraftCompat.init();
        }
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
    }

    private void imcProcess(InterModProcessEvent event) {
    }

    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        server = event.getServer();
    }

    @Override
    public MinecraftServer getServer() {
        return server;
    }

    static class Client extends SideProxy {
        Client() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ModItems::registerItemColors);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

            MinecraftForge.EVENT_BUS.addListener(this::setFog);
        }

        private void clientSetup(FMLClientSetupEvent event) {
            ModBlocks.registerRenderTypes(event);
            ModContainers.registerScreens(event);
            ModTileEntities.registerRenderers(event);
            ModModelProperties.register(event);
        }

        public void setFog(EntityViewRenderEvent.FogColors fog) {
            World w = fog.getInfo().getRenderViewEntity().getEntityWorld();
            BlockPos pos = fog.getInfo().getBlockPos();
            BlockState bs = w.getBlockState(pos);
            Block b = bs.getBlock();

            if (b.equals(ModBlocks.OIL)) {
                float red = 0.02F;
                float green = 0.02F;
                float blue = 0.02F;
                fog.setRed(red);
                fog.setGreen(green);
                fog.setBlue(blue);
            }

            if (b.equals(ModBlocks.DIESEL)) {
                float red = 0.9F;
                float green = 0.9F;
                float blue = 0.02F;
                fog.setRed(red);
                fog.setGreen(green);
                fog.setBlue(blue);
            }
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
