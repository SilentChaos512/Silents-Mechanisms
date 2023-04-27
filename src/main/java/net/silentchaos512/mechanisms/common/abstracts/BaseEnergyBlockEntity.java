package net.silentchaos512.mechanisms.common.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.silentchaos512.mechanisms.common.capability.energy.FlexibleEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * NOTE ! This block entity can only store energy, {@link IItemHandler} not included.
 * For IItemHandler version, see {@link BaseMachineBlockEntity}
 */
public abstract class BaseEnergyBlockEntity extends BlockEntity {
    public final FlexibleEnergyStorage energyStorage;
    protected LazyOptional<IEnergyStorage> energyHolder;
    private final ContainerData energyData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return pIndex == 0 ? energyStorage.getEnergyStored() : energyStorage.getMaxEnergyStored();
        }

        @Override
        public void set(int pIndex, int pValue) {

        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public BaseEnergyBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, FlexibleEnergyStorage energyStorage) {
        super(pType, pWorldPosition, pBlockState);
        this.energyStorage = energyStorage;
        this.energyHolder = LazyOptional.of(() -> this.energyStorage);
    }

    protected int getEnergySpace() {
        return energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
    }

    @Nonnull
    public abstract <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side);

    public <T> LazyOptional<T> getEnergyCap(Capability<T> cap) {
        return ForgeCapabilities.ENERGY.orEmpty(cap, this.energyHolder);
    }

    //Just leave it here with final modify, so I won't accidentally override it w/o any intention
    @NotNull
    @Override
    public final <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return super.getCapability(cap);
    }

    public IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyHolder.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("energy", energyStorage.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        energyStorage.deserializeNBT(pTag.get("energy"));
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.energyHolder = LazyOptional.of(() -> this.energyStorage);
    }
}
