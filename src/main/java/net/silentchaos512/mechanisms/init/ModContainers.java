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
import net.silentchaos512.mechanisms.block.alloysmelter.AlloySmelterScreen;
import net.silentchaos512.mechanisms.block.batterybox.BatteryBoxContainer;
import net.silentchaos512.mechanisms.block.batterybox.BatteryBoxScreen;
import net.silentchaos512.mechanisms.block.compressor.CompressorContainer;
import net.silentchaos512.mechanisms.block.compressor.CompressorScreen;
import net.silentchaos512.mechanisms.block.crusher.CrusherScreen;
import net.silentchaos512.mechanisms.block.electricfurnace.ElectricFurnaceContainer;
import net.silentchaos512.mechanisms.block.electricfurnace.ElectricFurnaceScreen;
import net.silentchaos512.mechanisms.block.generator.coal.CoalGeneratorContainer;
import net.silentchaos512.mechanisms.block.generator.coal.CoalGeneratorScreen;
import net.silentchaos512.mechanisms.block.generator.lava.LavaGeneratorContainer;
import net.silentchaos512.mechanisms.block.generator.lava.LavaGeneratorScreen;
import net.silentchaos512.mechanisms.block.mixer.MixerContainer;
import net.silentchaos512.mechanisms.block.mixer.MixerScreen;
import net.silentchaos512.mechanisms.block.pump.PumpContainer;
import net.silentchaos512.mechanisms.block.pump.PumpScreen;
import net.silentchaos512.mechanisms.block.refinery.RefineryContainer;
import net.silentchaos512.mechanisms.block.refinery.RefineryScreen;
import net.silentchaos512.mechanisms.util.MachineTier;

public final class ModContainers {
    public static ContainerType<BatteryBoxContainer> batteryBox;
    public static ContainerType<CoalGeneratorContainer> coalGenerator;
    public static ContainerType<CompressorContainer> compressor;
    public static ContainerType<ElectricFurnaceContainer> electricFurnace;
    public static ContainerType<LavaGeneratorContainer> lavaGenerator;
    public static ContainerType<MixerContainer> mixer;
    public static ContainerType<PumpContainer> pump;
    public static ContainerType<RefineryContainer> refinery;

    private ModContainers() {}

    public static void registerAll(RegistryEvent.Register<ContainerType<?>> event) {
        register("basic_alloy_smelter", MachineType.ALLOY_SMELTER.getContainerType(MachineTier.BASIC));
        register("alloy_smelter", MachineType.ALLOY_SMELTER.getContainerType(MachineTier.STANDARD));
        batteryBox = register("battery_box", BatteryBoxContainer::new);
        coalGenerator = register("coal_generator", CoalGeneratorContainer::new);
        compressor = register("compressor", CompressorContainer::new);
        register("basic_crusher", MachineType.CRUSHER.getContainerType(MachineTier.BASIC));
        register("crusher", MachineType.CRUSHER.getContainerType(MachineTier.STANDARD));
        electricFurnace = register("electric_furnace", ElectricFurnaceContainer::new);
        lavaGenerator = register("lava_generator", LavaGeneratorContainer::new);
        mixer = register("mixer", MixerContainer::new);
        pump = register("pump", PumpContainer::new);
        refinery = register("refinery", RefineryContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(MachineType.ALLOY_SMELTER.getContainerType(MachineTier.BASIC), AlloySmelterScreen::new);
        ScreenManager.registerFactory(MachineType.ALLOY_SMELTER.getContainerType(MachineTier.STANDARD), AlloySmelterScreen::new);
        ScreenManager.registerFactory(batteryBox, BatteryBoxScreen::new);
        ScreenManager.registerFactory(coalGenerator, CoalGeneratorScreen::new);
        ScreenManager.registerFactory(compressor, CompressorScreen::new);
        ScreenManager.registerFactory(MachineType.CRUSHER.getContainerType(MachineTier.BASIC), CrusherScreen::new);
        ScreenManager.registerFactory(MachineType.CRUSHER.getContainerType(MachineTier.STANDARD), CrusherScreen::new);
        ScreenManager.registerFactory(electricFurnace, ElectricFurnaceScreen::new);
        ScreenManager.registerFactory(lavaGenerator, LavaGeneratorScreen::new);
        ScreenManager.registerFactory(mixer, MixerScreen::new);
        ScreenManager.registerFactory(pump, PumpScreen::new);
        ScreenManager.registerFactory(refinery, RefineryScreen::new);
    }

    private static <C extends Container> ContainerType<C> register(String name, ContainerType.IFactory<C> containerFactory) {
        ContainerType<C> type = new ContainerType<>(containerFactory);
        return register(name, type);
    }

    private static <C extends Container> ContainerType<C> register(String name, ContainerType<C> containerType) {
        containerType.setRegistryName(SilentMechanisms.getId(name));
        ForgeRegistries.CONTAINERS.register(containerType);
        return containerType;
    }
}
