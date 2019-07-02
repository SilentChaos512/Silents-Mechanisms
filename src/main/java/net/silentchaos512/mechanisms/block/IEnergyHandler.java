package net.silentchaos512.mechanisms.block;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.mechanisms.capability.EnergyStorageImpl;

import javax.annotation.Nullable;

public interface IEnergyHandler {
    EnergyStorageImpl getEnergyImpl();

    default LazyOptional<IEnergyStorage> getEnergy(@Nullable Direction side) {
        return getEnergyImpl().getCapability(CapabilityEnergy.ENERGY, side);
    }

    default int getEnergyStored() {
        return getEnergy(null).orElseThrow(IllegalStateException::new).getEnergyStored();
    }

    default int getMaxEnergyStored() {
        return getEnergy(null).orElseThrow(IllegalStateException::new).getMaxEnergyStored();
    }

    default void setEnergyStoredDirectly(int value) {
        getEnergy(null).ifPresent(e -> {
            if (e instanceof EnergyStorageImpl) {
                ((EnergyStorageImpl) e).setEnergyDirectly(value);
            }
        });
    }

    default void readEnergy(CompoundNBT tags) {
        setEnergyStoredDirectly(tags.getInt("Energy"));
    }

    default void writeEnergy(CompoundNBT tags) {
        tags.putInt("Energy", getEnergyStored());
    }
}
