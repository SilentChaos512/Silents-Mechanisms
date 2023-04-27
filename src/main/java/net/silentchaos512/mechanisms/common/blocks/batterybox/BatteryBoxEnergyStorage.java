package net.silentchaos512.mechanisms.common.blocks.batterybox;

import net.silentchaos512.mechanisms.common.capability.energy.LoadableEnergyStorage;
import net.silentchaos512.mechanisms.common.items.BatteryItem;

public class BatteryBoxEnergyStorage extends LoadableEnergyStorage {
    private BatteryBoxBlockEntity container;

    public BatteryBoxEnergyStorage() {
        super(BatteryBoxBlockEntity.ENERGY_CAP, BatteryBoxBlockEntity.IO_RATE, BatteryBoxBlockEntity.IO_RATE);
    }

    public void setContainer(BatteryBoxBlockEntity container) {
        this.container = container;
    }

    public int getDisplayEnergyCapacity() {
        return this.container.getBatteryCount() * BatteryItem.DEFAULT_CAPACITY + super.capacity;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return super.receiveEnergy(maxReceive, simulate);
    }
}
