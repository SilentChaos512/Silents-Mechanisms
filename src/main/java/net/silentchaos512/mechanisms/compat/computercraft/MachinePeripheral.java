package net.silentchaos512.mechanisms.compat.computercraft;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MachinePeripheral implements IPeripheral {
    private static final String TYPE = SilentMechanisms.getId("machine").toString();

    private final AbstractMachineBaseTileEntity machine;

    public MachinePeripheral(AbstractMachineBaseTileEntity machine) {
        this.machine = machine;
    }

    @Nonnull
    @Override
    public String getType() {
        return TYPE;
    }

    @LuaFunction
    public final int getEnergy() {
        return machine.getEnergyStored();
    }

    @LuaFunction
    public final int getMaxEnergy() {
        return machine.getMaxEnergyStored();
    }

    @LuaFunction
    public final String getRedstoneMode() {
        return machine.getRedstoneMode().toString();
    }

    @LuaFunction
    public final void setRedstoneMode(String mode) throws LuaException {
        RedstoneMode redstoneMode = RedstoneMode.byName(mode);
        if (redstoneMode == null) {
            String validModes = Arrays.stream(RedstoneMode.values()).map(RedstoneMode::toString).collect(Collectors.joining(", "));
            throw new LuaException(String.format("Unknown redstone mode: %s (valid modes: %s)", mode, validModes));
        }
        machine.setRedstoneMode(redstoneMode);
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if (other instanceof MachinePeripheral)
            return machine.equals(((MachinePeripheral) other).machine);
        return false;
    }
}
