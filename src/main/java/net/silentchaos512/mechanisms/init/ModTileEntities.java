package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.crusher.CrusherTileEntity;

import java.util.function.Supplier;

public class ModTileEntities {
    public static TileEntityType<CrusherTileEntity> crusher;

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        crusher = register("crusher", CrusherTileEntity::new, ModBlocks.crusher);
    }

    private static <T extends TileEntity> TileEntityType<T> register(String name, Supplier<T> tileFactory, Block... blocks) {
        TileEntityType<T> type = TileEntityType.Builder.create(tileFactory, blocks).build(null);
        type.setRegistryName(SilentMechanisms.getId(name));
        ForgeRegistries.TILE_ENTITIES.register(type);
        return type;
    }
}
