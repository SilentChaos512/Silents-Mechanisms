package net.silentchaos512.mechanisms.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public abstract class AbstractMachineContainer<T extends AbstractMachineTileEntity<?>> extends AbstractEnergyStorageContainer {
    protected final T tileEntity;

    protected AbstractMachineContainer(ContainerType<?> containerTypeIn, int id, T tileEntityIn, IIntArray fieldsIn) {
        super(containerTypeIn, id, fieldsIn);
        this.tileEntity = tileEntityIn;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        // TODO
        return true;
    }

    public T getTileEntity() {
        return tileEntity;
    }

    public int getProgress() {
        return fields.get(0);
    }

    public int getProcessTime() {
        return fields.get(1);
    }

    @Override
    public int getEnergyStored() {
        return fields.get(2);
    }
}
