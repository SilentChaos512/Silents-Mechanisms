package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.batterybox.BatteryBoxTileEntity;
import net.silentchaos512.mechanisms.block.compressor.CompressorTileEntity;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackBlock;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackTileEntity;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackTileEntityRenderer;
import net.silentchaos512.mechanisms.block.electricfurnace.ElectricFurnaceTileEntity;
import net.silentchaos512.mechanisms.block.generator.coal.CoalGeneratorTileEntity;
import net.silentchaos512.mechanisms.block.generator.diesel.DieselGeneratorTileEntity;
import net.silentchaos512.mechanisms.block.generator.lava.LavaGeneratorTileEntity;
import net.silentchaos512.mechanisms.block.mixer.MixerTileEntity;
import net.silentchaos512.mechanisms.block.pipe.PipeTileEntity;
import net.silentchaos512.mechanisms.block.pump.PumpTileEntity;
import net.silentchaos512.mechanisms.block.refinery.RefineryTileEntity;
import net.silentchaos512.mechanisms.block.solidifier.SolidifierTileEntity;
import net.silentchaos512.mechanisms.block.wire.WireTileEntity;

import java.util.function.Supplier;

public final class ModTileEntities {
    public static TileEntityType<BatteryBoxTileEntity> batteryBox;
    public static TileEntityType<CoalGeneratorTileEntity> coalGenerator;
    public static TileEntityType<CompressorTileEntity> compressor;
    public static TileEntityType<DieselGeneratorTileEntity> dieselGenerator;
    public static TileEntityType<DryingRackTileEntity> dryingRack;
    public static TileEntityType<ElectricFurnaceTileEntity> electricFurnace;
    public static TileEntityType<LavaGeneratorTileEntity> lavaGenerator;
    public static TileEntityType<MixerTileEntity> mixer;
    public static TileEntityType<PipeTileEntity> pipe;
    public static TileEntityType<PumpTileEntity> pump;
    public static TileEntityType<RefineryTileEntity> refinery;
    public static TileEntityType<SolidifierTileEntity> solidifier;
    public static TileEntityType<WireTileEntity> wire;

    private ModTileEntities() {}

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        register("basic_alloy_smelter", MachineType.ALLOY_SMELTER.getBasicTileEntityType());
        register("alloy_smelter", MachineType.ALLOY_SMELTER.getStandardTileEntityType());
        register("basic_crusher", MachineType.CRUSHER.getBasicTileEntityType());
        register("crusher", MachineType.CRUSHER.getStandardTileEntityType());
        batteryBox = register("battery_box", BatteryBoxTileEntity::new, ModBlocks.batteryBox);
        coalGenerator = register("coal_generator", CoalGeneratorTileEntity::new, ModBlocks.coalGenerator);
        compressor = register("compressor", CompressorTileEntity::new, ModBlocks.compressor);
        dieselGenerator = register("diesel_generator", DieselGeneratorTileEntity::new, ModBlocks.dieselGenerator);
        dryingRack = register("drying_rack", DryingRackTileEntity::new, ModBlocks.DRYING_RACKS.toArray(new DryingRackBlock[0]));
        electricFurnace = register("electric_furnace", ElectricFurnaceTileEntity::new, ModBlocks.electricFurnace);
        lavaGenerator = register("lava_generator", LavaGeneratorTileEntity::new, ModBlocks.lavaGenerator);
        mixer = register("mixer", MixerTileEntity::new, ModBlocks.mixer);
        pipe = register("pipe", PipeTileEntity::new, ModBlocks.pipe);
        pump = register("pump", PumpTileEntity::new, ModBlocks.pump);
        refinery = register("refinery", RefineryTileEntity::new, ModBlocks.refinery);
        solidifier = register("solidifier", SolidifierTileEntity::new, ModBlocks.solidifier);
        wire = register("wire", WireTileEntity::new, ModBlocks.wire);
    }

    private static <T extends TileEntity> TileEntityType<T> register(String name, Supplier<T> tileFactory, Block... blocks) {
        TileEntityType<T> type = TileEntityType.Builder.create(tileFactory, blocks).build(null);
        return register(name, type);
    }

    private static <T extends TileEntity> TileEntityType<T> register(String name, TileEntityType<T> type) {
        if (type.getRegistryName() == null) {
            type.setRegistryName(SilentMechanisms.getId(name));
        }
        ForgeRegistries.TILE_ENTITIES.register(type);
        return type;
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(dryingRack, DryingRackTileEntityRenderer::new);
    }
}
