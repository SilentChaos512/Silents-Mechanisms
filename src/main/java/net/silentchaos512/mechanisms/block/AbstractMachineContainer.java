package net.silentchaos512.mechanisms.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public abstract class AbstractMachineContainer<T extends AbstractMachineTileEntity<?>> extends Container {
    protected final T tileEntity;
    protected final IIntArray fields;

    protected AbstractMachineContainer(ContainerType<?> containerTypeIn, int id, T tileEntityIn, IIntArray fieldsIn) {
        super(containerTypeIn, id);
        this.tileEntity = tileEntityIn;
        this.fields = fieldsIn;

        func_216961_a(fieldsIn);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        // TODO
        return true;
    }

    public T getTileEntity() {
        return tileEntity;
    }

    public IIntArray getFields() {
        return fields;
    }

    public int getProgress() {
        return fields.get(0);
    }

    public int getProcessTime() {
        return fields.get(1);
    }
}
