package net.silentchaos512.mechanisms.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.IEnergyHandler;

public final class EnergyUtils {
    private EnergyUtils() {throw new IllegalAccessError("Utility class");}

    public static void trySendToNeighbors(IBlockReader world, BlockPos pos, IEnergyHandler energyHandler, int maxSend) {
        for (Direction side : Direction.values()) {
            if (energyHandler.getEnergyStored() == 0) {
                return;
            }
            trySendTo(world, pos, energyHandler, maxSend, side);
        }
    }

    public static void trySendTo(IBlockReader world, BlockPos pos, IEnergyHandler energyHandler, int maxSend, Direction side) {
        TileEntity tileEntity = world.getTileEntity(pos.offset(side));
        if (tileEntity != null) {
            IEnergyStorage energy = energyHandler.getEnergy(side).orElseThrow(IllegalStateException::new);
            tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).ifPresent(other -> {
                int toSend = energy.extractEnergy(maxSend, true);
                int sent = other.receiveEnergy(toSend, false);
                energy.extractEnergy(sent, false);
                if (sent > 0) {
                    SilentMechanisms.LOGGER.debug("send {} from {} -> {} (e={}, o={})",
                            sent, world.getTileEntity(pos), tileEntity, energy.getClass(), other.getClass());
                    SilentMechanisms.LOGGER.debug("{}, {}",
                            energy.getEnergyStored(), other.getEnergyStored());
                }
            });
        }
    }
}
