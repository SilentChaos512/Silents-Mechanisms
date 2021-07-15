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
import net.silentchaos512.mechanisms.block.generator.diesel.DieselGeneratorContainer;
import net.silentchaos512.mechanisms.block.generator.diesel.DieselGeneratorScreen;
import net.silentchaos512.mechanisms.block.generator.lava.LavaGeneratorContainer;
import net.silentchaos512.mechanisms.block.generator.lava.LavaGeneratorScreen;
import net.silentchaos512.mechanisms.block.infuser.InfuserContainer;
import net.silentchaos512.mechanisms.block.infuser.InfuserScreen;
import net.silentchaos512.mechanisms.block.mixer.MixerContainer;
import net.silentchaos512.mechanisms.block.mixer.MixerScreen;
import net.silentchaos512.mechanisms.block.pump.PumpContainer;
import net.silentchaos512.mechanisms.block.pump.PumpScreen;
import net.silentchaos512.mechanisms.block.refinery.RefineryContainer;
import net.silentchaos512.mechanisms.block.refinery.RefineryScreen;
import net.silentchaos512.mechanisms.block.solidifier.SolidifierContainer;
import net.silentchaos512.mechanisms.block.solidifier.SolidifierScreen;
import net.silentchaos512.mechanisms.util.MachineTier;

public final class ModContainers {
    public static ContainerType<BatteryBoxContainer> batteryBox;
    public static ContainerType<CoalGeneratorContainer> coalGenerator;
    public static ContainerType<CompressorContainer> compressor;
    public static ContainerType<DieselGeneratorContainer> dieselGenerator;
    public static ContainerType<ElectricFurnaceContainer> electricFurnace;
    public static ContainerType<InfuserContainer> infuser;
    public static ContainerType<LavaGeneratorContainer> lavaGenerator;
    public static ContainerType<MixerContainer> mixer;
    public static ContainerType<PumpContainer> pump;
    public static ContainerType<RefineryContainer> refinery;
    public static ContainerType<SolidifierContainer> solidifier;

    private ModContainers() {}

    public static void registerAll(RegistryEvent.Register<ContainerType<?>> event) {
        register("basic_alloy_smelter", MachineType.ALLOY_SMELTER.getContainerType(MachineTier.BASIC));
        register("alloy_smelter", MachineType.ALLOY_SMELTER.getContainerType(MachineTier.STANDARD));
        batteryBox = register("battery_box", BatteryBoxContainer::new);
        coalGenerator = register("coal_generator", CoalGeneratorContainer::new);
        compressor = register("compressor", CompressorContainer::new);
        register("basic_crusher", MachineType.CRUSHER.getContainerType(MachineTier.BASIC));
        register("crusher", MachineType.CRUSHER.getContainerType(MachineTier.STANDARD));
        dieselGenerator = register("diesel_generator", DieselGeneratorContainer::new);
        electricFurnace = register("electric_furnace", ElectricFurnaceContainer::new);
        infuser = register("infuser", InfuserContainer::new);
        lavaGenerator = register("lava_generator", LavaGeneratorContainer::new);
        mixer = register("mixer", MixerContainer::new);
        pump = register("pump", PumpContainer::new);
        refinery = register("refinery", RefineryContainer::new);
        solidifier = register("solidifier", SolidifierContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.register(MachineType.ALLOY_SMELTER.getContainerType(MachineTier.BASIC), AlloySmelterScreen::new);
        ScreenManager.register(MachineType.ALLOY_SMELTER.getContainerType(MachineTier.STANDARD), AlloySmelterScreen::new);
        ScreenManager.register(batteryBox, BatteryBoxScreen::new);
        ScreenManager.register(coalGenerator, CoalGeneratorScreen::new);
        ScreenManager.register(compressor, CompressorScreen::new);
        ScreenManager.register(MachineType.CRUSHER.getContainerType(MachineTier.BASIC), CrusherScreen::new);
        ScreenManager.register(MachineType.CRUSHER.getContainerType(MachineTier.STANDARD), CrusherScreen::new);
        ScreenManager.register(dieselGenerator, DieselGeneratorScreen::new);
        ScreenManager.register(electricFurnace, ElectricFurnaceScreen::new);
        ScreenManager.register(infuser, InfuserScreen::new);
        ScreenManager.register(lavaGenerator, LavaGeneratorScreen::new);
        ScreenManager.register(mixer, MixerScreen::new);
        ScreenManager.register(pump, PumpScreen::new);
        ScreenManager.register(refinery, RefineryScreen::new);
        ScreenManager.register(solidifier, SolidifierScreen::new);
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
