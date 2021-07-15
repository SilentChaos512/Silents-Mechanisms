package net.silentchaos512.mechanisms.init;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;
import net.silentchaos512.mechanisms.block.alloysmelter.AlloySmelterContainer;
import net.silentchaos512.mechanisms.block.alloysmelter.AlloySmelterTileEntity;
import net.silentchaos512.mechanisms.block.crusher.CrusherContainer;
import net.silentchaos512.mechanisms.block.crusher.CrusherTileEntity;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.utils.Lazy;

import java.util.Objects;
import java.util.function.Supplier;

public class MachineType<T extends AbstractMachineBaseTileEntity, B extends T, S extends T, C extends Container> {
    public static final MachineType<AlloySmelterTileEntity, AlloySmelterTileEntity.Basic, AlloySmelterTileEntity, AlloySmelterContainer> ALLOY_SMELTER = new MachineType<>(
            () -> TileEntityType.Builder.of(AlloySmelterTileEntity.Basic::new, ModBlocks.BASIC_ALLOY_SMELTER.get()),
            () -> TileEntityType.Builder.of(AlloySmelterTileEntity::new, ModBlocks.ALLOY_SMELTER.get()),
            (id, inv) -> new AlloySmelterContainer(id, inv, MachineTier.BASIC),
            (id, inv) -> new AlloySmelterContainer(id, inv, MachineTier.STANDARD)
    );
    public static final MachineType<CrusherTileEntity, CrusherTileEntity.Basic, CrusherTileEntity, CrusherContainer> CRUSHER = new MachineType<>(
            () -> TileEntityType.Builder.of(CrusherTileEntity.Basic::new, ModBlocks.BASIC_CRUSHER.get()),
            () -> TileEntityType.Builder.of(CrusherTileEntity::new, ModBlocks.CRUSHER.get()),
            (id, inv) -> new CrusherContainer(id, inv, MachineTier.BASIC),
            (id, inv) -> new CrusherContainer(id, inv, MachineTier.STANDARD)
    );

    private final Lazy<TileEntityType<B>> basicTileEntityType;
    private final Lazy<TileEntityType<S>> standardTileEntityType;
    private final Lazy<ContainerType<C>> basicContainerType;
    private final Lazy<ContainerType<C>> standardContainerType;

    public MachineType(
            Supplier<TileEntityType.Builder<B>> basic,
            Supplier<TileEntityType.Builder<S>> standard,
            ContainerType.IFactory<C> basicContainer,
            ContainerType.IFactory<C> standardContainer
    ) {
        this.basicTileEntityType = Lazy.of(() -> basic.get().build(null));
        this.standardTileEntityType = Lazy.of(() -> standard.get().build(null));
        this.basicContainerType = Lazy.of(() -> new ContainerType<>(basicContainer));
        this.standardContainerType = Lazy.of(() -> new ContainerType<>(standardContainer));
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

    public T create(MachineTier tier) {
        return Objects.requireNonNull(getTileEntityType(tier).create());
    }

    public ContainerType<C> getContainerType(MachineTier tier) {
        switch (tier) {
            case BASIC:
                return basicContainerType.get();
            case STANDARD:
                return standardContainerType.get();
            default:
                throw new IllegalArgumentException("Unknown MachineTier: " + tier);
        }
    }
}
