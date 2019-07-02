package net.silentchaos512.mechanisms.block.batterybox;

import net.silentchaos512.mechanisms.block.EnergyStoringTileEntity;
import net.silentchaos512.mechanisms.init.ModTileEntities;

public class BatteryBoxTileEntity extends EnergyStoringTileEntity {
    public static final int MAX_ENERGY = 1_000_000;
    public static final int MAX_RECEIVE = 1_000;
    public static final int MAX_SEND = 1_000;

    public BatteryBoxTileEntity() {
        super(ModTileEntities.batteryBox, MAX_ENERGY, MAX_RECEIVE, MAX_SEND);
    }
}
