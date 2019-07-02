package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.crusher.CrusherTileEntity;
import net.silentchaos512.mechanisms.block.generator.CoalGeneratorTileEntity;
import net.silentchaos512.mechanisms.block.wire.WireTileEntity;

import java.util.function.Supplier;

public class ModTileEntities {
    public static TileEntityType<CoalGeneratorTileEntity> coalGenerator;
    public static TileEntityType<CrusherTileEntity> crusher;
    public static TileEntityType<WireTileEntity> wire;

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        coalGenerator = register("coal_generator", CoalGeneratorTileEntity::new, ModBlocks.coalGenerator);
        crusher = register("crusher", CrusherTileEntity::new, ModBlocks.crusher);
        wire = register("wire", WireTileEntity::new, ModBlocks.wire);
    }

    private static <T extends TileEntity> TileEntityType<T> register(String name, Supplier<T> tileFactory, Block... blocks) {
        TileEntityType<T> type = TileEntityType.Builder.create(tileFactory, blocks).build(null);
        type.setRegistryName(SilentMechanisms.getId(name));
        ForgeRegistries.TILE_ENTITIES.register(type);
        return type;
    }
}
