package net.silentchaos512.mechanisms.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.mechanisms.block.IEnergyHandler;

import javax.annotation.Nullable;

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
            tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).ifPresent(other -> trySendEnergy(maxSend, energy, other));
        }
    }

    private static void trySendEnergy(int maxSend, IEnergyStorage energy, IEnergyStorage other) {
        if (other.canReceive()) {
            int toSend = energy.extractEnergy(maxSend, true);
            int sent = other.receiveEnergy(toSend, false);
            if (sent > 0) {
                energy.extractEnergy(sent, false);
            }
        }
    }

    /**
     * Gets the energy capability for the block at the given position. If it does not have an energy
     * capability, or the block is not a tile entity, this returns null.
     *
     * @param world The world
     * @param pos   The position to check
     * @return The energy capability, or null if not present
     */
    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static IEnergyStorage getEnergy(IWorldReader world, BlockPos pos) {
        if (!world.isAreaLoaded(pos, 1)) return null;
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null ? tileEntity.getCapability(CapabilityEnergy.ENERGY).orElse(null) : null;
    }

    /**
     * Gets the energy capability of the object (item, etc), or null if it does not have one.
     *
     * @param provider The capability provider
     * @return The energy capability, or null if not present
     */
    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static IEnergyStorage getEnergy(ICapabilityProvider provider) {
        return provider.getCapability(CapabilityEnergy.ENERGY).orElse(null);
    }
}
