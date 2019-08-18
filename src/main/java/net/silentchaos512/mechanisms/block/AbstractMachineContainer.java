package net.silentchaos512.mechanisms.block;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public abstract class AbstractMachineContainer<T extends AbstractMachineTileEntity<?>> extends AbstractEnergyStorageContainer<T> {
    protected AbstractMachineContainer(ContainerType<?> containerTypeIn, int id, T tileEntityIn, IIntArray fieldsIn) {
        super(containerTypeIn, id, tileEntityIn, fieldsIn);
    }

    public int getProgress() {
        return fields.get(2);
    }

    public int getProcessTime() {
        return fields.get(3);
    }
}
