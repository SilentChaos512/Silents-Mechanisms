package net.silentchaos512.mechanisms.block.wire;

import net.silentchaos512.mechanisms.block.EnergyStoringTileEntity;
import net.silentchaos512.mechanisms.init.ModTileEntities;

public class WireTileEntity extends EnergyStoringTileEntity {
    public static final int MAX_ENERGY = 10_000;
    public static final int MAX_RECEIVE = 1_000;
    public static final int MAX_SEND = 500;

    public WireTileEntity() {
        super(ModTileEntities.wire, MAX_ENERGY, MAX_RECEIVE, MAX_SEND);
    }
}
