package net.silentchaos512.mechanisms.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public final class EnergyUtils {
    private EnergyUtils() {throw new IllegalAccessError("Utility class");}

    public static void trySendToNeighbors(IBlockReader world, BlockPos pos, IEnergyStorage energy, int maxSend) {
        for (Direction side : Direction.values()) {
            if (energy.getEnergyStored() == 0) {
                return;
            }
            trySendTo(world, pos, energy, maxSend, side);
        }
    }

    public static void trySendTo(IBlockReader world, BlockPos pos, IEnergyStorage energy, int maxSend, Direction side) {
        TileEntity tileEntity = world.getTileEntity(pos.offset(side));
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).ifPresent(other -> {
                int toSend = energy.extractEnergy(maxSend, true);
                int sent = other.receiveEnergy(toSend, false);
                energy.extractEnergy(sent, false);
            });
        }
    }
}
