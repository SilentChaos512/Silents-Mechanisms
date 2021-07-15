package net.silentchaos512.mechanisms.capability;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyStorageWithBatteries<T extends TileEntity & IInventory> extends EnergyStorageImpl {
    private final IInventory inventory;
    private final LazyOptional<EnergyStorageWithBatteries> lazy;

    protected int energyInternal;
    protected int capacityInternal;
    protected int maxReceive;
    protected int maxExtract;

    public EnergyStorageWithBatteries(T tileEntity, int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, tileEntity);
        this.inventory = tileEntity;
        this.capacityInternal = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.lazy = LazyOptional.of(() -> this);
    }

    public int getInternalEnergyStored() {
        return energyInternal;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private static boolean isBattery(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    private int getBatteryCount() {
        int count = 0;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            if (isBattery(inventory.getItem(i))) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) return 0;

        int batteryCount = getBatteryCount();
        int left = maxReceive;
        if (batteryCount > 0) {
            int perBattery = left / batteryCount;

            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack stack = inventory.getItem(i);
                LazyOptional<IEnergyStorage> optional = stack.getCapability(CapabilityEnergy.ENERGY);

                if (optional.isPresent()) {
                    left -= optional.orElseThrow(IllegalStateException::new).receiveEnergy(perBattery, simulate);
                }
            }
        }

        int internalReceive = Math.min(capacityInternal - energyInternal, Math.min(this.maxReceive, left));
        if (!simulate)
            energyInternal += internalReceive;
        return maxReceive - (left - internalReceive);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) return 0;

        int internalExtract = Math.min(energyInternal, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energyInternal -= internalExtract;
        if (internalExtract >= maxExtract)
            return internalExtract;

        int batteryCount = getBatteryCount();
        int extracted = internalExtract;
        if (batteryCount > 0) {
            int perBattery = (maxExtract - internalExtract) / batteryCount;

            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack stack = inventory.getItem(i);
                LazyOptional<IEnergyStorage> optional = stack.getCapability(CapabilityEnergy.ENERGY);

                if (optional.isPresent()) {
                    extracted += optional.orElseThrow(IllegalStateException::new).extractEnergy(perBattery, simulate);
                }
            }
        }

        return extracted;
    }

    public int extractInternalEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) return 0;

        int internalExtract = Math.min(energyInternal, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energyInternal -= internalExtract;
        return internalExtract;
    }

    @Override
    public int getEnergyStored() {
        int ret = energyInternal;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            LazyOptional<IEnergyStorage> optional = stack.getCapability(CapabilityEnergy.ENERGY);

            if (optional.isPresent()) {
                ret += optional.orElseThrow(IllegalStateException::new).getEnergyStored();
            }
        }
        return ret;
    }

    @Override
    public int getMaxEnergyStored() {
        int ret = capacityInternal;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            LazyOptional<IEnergyStorage> optional = stack.getCapability(CapabilityEnergy.ENERGY);

            if (optional.isPresent()) {
                ret += optional.orElseThrow(IllegalStateException::new).getMaxEnergyStored();
            }
        }
        return ret;
    }

    @Override
    public void setEnergyDirectly(int amount) {
        this.energyInternal = amount;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Nonnull
    @Override
    public <S> LazyOptional<S> getCapability(@Nonnull Capability<S> cap, @Nullable Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, lazy.cast());
    }
}
