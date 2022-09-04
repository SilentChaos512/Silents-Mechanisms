package net.silentchaos512.mechanisms.common.capability.energy;

/**
 * <pre>
 *     This Type of {@link RestrictedEnergyStorage} is mainly used for Generators, it can only receive energy to itself (or generating energy)
 *     Any type of energy that has similar functionality can use this implementation
 * </pre>
 */
public class GeneratorEnergyStorage extends RestrictedEnergyStorage {
    public GeneratorEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, RestrictionType.NO_RECEIVE);
    }

    @Override
    public void generateEnergy(int maxGenerate) {
        if (this.getEnergySpace() > 0) {
            int energy = Math.min(getEnergySpace(), maxGenerate);
            super.energy += energy;
        }
    }

    public int getEnergySpace() {
        return this.getMaxEnergyStored() - this.getEnergyStored();
    }
}
