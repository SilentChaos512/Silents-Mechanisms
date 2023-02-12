package net.silentchaos512.mechanisms.common.capability.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.mechanisms.common.abstracts.BaseEnergyBlockEntity;
import net.silentchaos512.mechanisms.utls.CompoundTagUtils;

/**
 * A special type of {@link net.minecraftforge.energy.EnergyStorage} that allows user to modify the amount of energy stored
 * Mainly used for tag loading.
 *
 * @see CompoundTagUtils#loadEnergyToBlockEntity(BaseEnergyBlockEntity, CompoundTag)
 */
public interface FlexibleEnergyStorage extends IEnergyStorage, INBTSerializable<Tag> {
    void setEnergy(int energy);
}
