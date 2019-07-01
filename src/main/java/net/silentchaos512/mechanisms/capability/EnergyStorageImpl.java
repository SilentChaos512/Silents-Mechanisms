package net.silentchaos512.mechanisms.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyStorageImpl extends EnergyStorage implements ICapabilityProvider {
    public EnergyStorageImpl(int capacity) {
        super(capacity);
    }

    public EnergyStorageImpl(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyStorageImpl(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public EnergyStorageImpl(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(() -> this));
    }

    /**
     * Sets energy directly. Should only be used for syncing data from server to client.
     *
     * @param amount The new amount of stored energy
     */
    public void setEnergyDirectly(int amount) {
        this.energy = amount;
    }
}
