package net.silentchaos512.mechanisms.compat.computercraft;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.apis.ArgumentHelper;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[]{
                "getEnergy",
                "getMaxEnergy",
                "getRedstoneMode",
                "setRedstoneMode"
        };
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException, InterruptedException {
        switch(method) {
            case 0:
                return new Object[]{machine.getEnergyStored()};
            case 1:
                return new Object[]{machine.getMaxEnergyStored()};
            case 2:
                return new Object[]{machine.getRedstoneMode().name()};
            case 3:
                String name = ArgumentHelper.getString(arguments, 0);
                RedstoneMode mode = RedstoneMode.byName(name);
                if (mode == null) {
                    throw new LuaException("Unknown redstone mode: " + name);
                }
                machine.setRedstoneMode(mode);
                return new Object[]{mode.name()};
            default:
                throw new IllegalStateException("Method index out of range!");
        }
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if (other instanceof MachinePeripheral)
            return machine.equals(((MachinePeripheral) other).machine);
        return false;
    }
}
