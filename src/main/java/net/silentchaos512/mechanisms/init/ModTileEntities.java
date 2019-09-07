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
import net.silentchaos512.mechanisms.block.crusher.CrusherTileEntity;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackBlock;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackTileEntity;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackTileEntityRenderer;
import net.silentchaos512.mechanisms.block.electricfurnace.ElectricFurnaceTileEntity;
import net.silentchaos512.mechanisms.block.generator.coal.CoalGeneratorTileEntity;
import net.silentchaos512.mechanisms.block.generator.lava.LavaGeneratorTileEntity;
import net.silentchaos512.mechanisms.block.wire.WireTileEntity;

import java.util.function.Supplier;

public class ModTileEntities {
    public static TileEntityType<BatteryBoxTileEntity> batteryBox;
    public static TileEntityType<CoalGeneratorTileEntity> coalGenerator;
    public static TileEntityType<CompressorTileEntity> compressor;
    public static TileEntityType<CrusherTileEntity> crusher;
    public static TileEntityType<DryingRackTileEntity> dryingRack;
    public static TileEntityType<ElectricFurnaceTileEntity> electricFurnace;
    public static TileEntityType<LavaGeneratorTileEntity> lavaGenerator;
    public static TileEntityType<WireTileEntity> wire;

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        register("basic_alloy_smelter", MachineType.ALLOY_SMELTER.getBasicTileEntityType());
        register("alloy_smelter", MachineType.ALLOY_SMELTER.getStandardTileEntityType());
        batteryBox = register("battery_box", BatteryBoxTileEntity::new, ModBlocks.batteryBox);
        coalGenerator = register("coal_generator", CoalGeneratorTileEntity::new, ModBlocks.coalGenerator);
        compressor = register("compressor", CompressorTileEntity::new, ModBlocks.compressor);
        crusher = register("crusher", CrusherTileEntity::new, ModBlocks.crusher);
        dryingRack = register("drying_rack", DryingRackTileEntity::new, ModBlocks.DRYING_RACKS.toArray(new DryingRackBlock[0]));
        electricFurnace = register("electric_furnace", ElectricFurnaceTileEntity::new, ModBlocks.electricFurnace);
        lavaGenerator = register("lava_generator", LavaGeneratorTileEntity::new, ModBlocks.lavaGenerator);
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
        ClientRegistry.bindTileEntitySpecialRenderer(DryingRackTileEntity.class, new DryingRackTileEntityRenderer());
    }
}
