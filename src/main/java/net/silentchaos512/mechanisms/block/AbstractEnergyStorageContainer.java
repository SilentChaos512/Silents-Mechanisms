package net.silentchaos512.mechanisms.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public class AbstractEnergyStorageContainer extends Container {
    protected final IIntArray fields;

    protected AbstractEnergyStorageContainer(ContainerType<?> type, int id, IIntArray fieldsIn) {
        super(type, id);
        this.fields = fieldsIn;

        func_216961_a(this.fields);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        // TODO
        return true;
    }

    public IIntArray getFields() {
        return fields;
    }

    public int getEnergyStored() {
        return fields.get(0);
    }
}
