package net.silentchaos512.mechanisms.common.capability.energy;

import net.minecraftforge.energy.EnergyStorage;

public class LoadableEnergyStorage extends EnergyStorage implements FlexibleEnergyStorage {

    public LoadableEnergyStorage(int cap, int maxReceive, int maxExtract) {
        super(cap, maxReceive, maxExtract);
    }

    @Override
    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
