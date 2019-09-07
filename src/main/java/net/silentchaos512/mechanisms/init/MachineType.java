package net.silentchaos512.mechanisms.init;

import net.minecraft.tileentity.TileEntityType;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;
import net.silentchaos512.mechanisms.block.alloysmelter.AlloySmelterTileEntity;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.utils.Lazy;

import java.util.function.Supplier;

public class MachineType<T extends AbstractMachineBaseTileEntity, B extends T, S extends T> {
    public static final MachineType<AlloySmelterTileEntity, AlloySmelterTileEntity.Basic, AlloySmelterTileEntity> ALLOY_SMELTER = new MachineType<>(
            () -> TileEntityType.Builder.create(AlloySmelterTileEntity.Basic::new, ModBlocks.basicAlloySmelter),
            () -> TileEntityType.Builder.create(AlloySmelterTileEntity::new, ModBlocks.alloySmelter)
    );

    private final Lazy<TileEntityType<B>> basicTileEntityType;
    private final Lazy<TileEntityType<S>> standardTileEntityType;

    public MachineType(Supplier<TileEntityType.Builder<B>> basic, Supplier<TileEntityType.Builder<S>> standard) {
        this.basicTileEntityType = Lazy.of(() -> basic.get().build(null));
        this.standardTileEntityType = Lazy.of(() -> standard.get().build(null));
    }

    public TileEntityType<? extends T> getTileEntityType(MachineTier tier) {
        switch (tier) {
            case BASIC:
                return basicTileEntityType.get();
            case STANDARD:
                return standardTileEntityType.get();
            default:
                throw new IllegalArgumentException("Unknown MachineTier: " + tier);
        }
    }

    public TileEntityType<B> getBasicTileEntityType() {
        return basicTileEntityType.get();
    }

    public TileEntityType<S> getStandardTileEntityType() {
        return standardTileEntityType.get();
    }
}
