package net.silentchaos512.mechanisms.common.capability.energy;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.registries.ForgeRegistries;

public class TagBasedEnergyStorage extends EnergyStorage {
    private final CompoundTag stackTag;

    public TagBasedEnergyStorage(int capacity, ItemStack energyStack) {
        this(capacity, energyStack.getOrCreateTag());
    }

    public TagBasedEnergyStorage(int capacity, CompoundTag stackTag) {
        super(capacity);
        this.stackTag = stackTag;
        this.energy = this.stackTag.getInt("energy");
    }

    public static TagBasedEnergyStorage of(ItemStack stack) {
        final CompoundTag tag = stack.getOrCreateTag();
        Preconditions.checkState(!tag.isEmpty() && tag.contains("energy", Tag.TAG_INT) && tag.contains("capacity", Tag.TAG_INT), "No energy data for " + ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        return new TagBasedEnergyStorage(tag.getInt("capacity"), tag);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        final int i = super.receiveEnergy(maxReceive, simulate);
        this.stackTag.putInt("energy", this.energy);
        return i;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        final int i = super.extractEnergy(maxExtract, simulate);
        this.stackTag.putInt("energy", this.energy);
        return i;
    }
}