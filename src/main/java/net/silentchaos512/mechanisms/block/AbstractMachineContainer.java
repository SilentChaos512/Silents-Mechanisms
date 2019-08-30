package net.silentchaos512.mechanisms.block;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.utils.EnumUtils;

public abstract class AbstractMachineContainer<T extends AbstractMachineTileEntity<?>> extends AbstractMachineBaseContainer<T> {
    protected AbstractMachineContainer(ContainerType<?> containerTypeIn, int id, T tileEntityIn, IIntArray fieldsIn) {
        super(containerTypeIn, id, tileEntityIn, fieldsIn);
    }

    public int getProgress() {
        return fields.get(2);
    }

    public int getProcessTime() {
        return fields.get(3);
    }

    @Override
    public RedstoneMode getRedstoneMode() {
        return EnumUtils.byOrdinal(fields.get(4), RedstoneMode.IGNORED);
    }

    @Override
    public void setRedstoneMode(RedstoneMode mode) {
        fields.set(4, mode.ordinal());
    }
}
