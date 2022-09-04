package net.silentchaos512.mechanisms.common.capability.energy;

import net.minecraftforge.energy.EnergyStorage;

/**
 * This is a type of {@link EnergyStorage} that allows users to restrict the energy storage to be only extracted or only receive,
 * otherwise, use forge's {@link EnergyStorage} for no restriction
 */
public class RestrictedEnergyStorage extends EnergyStorage implements FlexibleEnergyStorage {
    private final RestrictionType restriction;
    public RestrictedEnergyStorage(int capacity, int maxReceive, int maxExtract, RestrictionType restriction) {
        super(capacity, maxReceive, maxExtract);
        this.restriction = restriction;
    }

    /**
     * Use for implementation of {@link GeneratorEnergyStorage}. Do not use this if your energy storage is not for energy generation
     */
    public void generateEnergy(int maxGenerate) {
    }

    @Override
    public boolean canReceive() {
        return restriction.canReceive();
    }

    @Override
    public boolean canExtract() {
        return restriction.canExtract();
    }

    @Override
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public enum RestrictionType {
        NO_EXTRACT,
        NO_RECEIVE;

        public boolean canExtract() {
            return this != NO_EXTRACT;
        }

        public boolean canReceive() {
            return this != NO_RECEIVE;
        }
    }
}
