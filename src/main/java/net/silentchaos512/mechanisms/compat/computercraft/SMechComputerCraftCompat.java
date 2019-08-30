package net.silentchaos512.mechanisms.compat.computercraft;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.mechanisms.block.AbstractEnergyInventoryTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SMechComputerCraftCompat {
    private SMechComputerCraftCompat() {}

    public static void init() {
        ComputerCraftAPI.registerPeripheralProvider(SMechComputerCraftCompat::getPeripheral);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    @Nullable
    private static IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction side) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof AbstractEnergyInventoryTileEntity) {
            return new MachinePeripheral((AbstractEnergyInventoryTileEntity) tileEntity);
        }
        return null;
    }
}
