package net.silentchaos512.mechanisms.block;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.inventory.MachineUpgradeSlot;
import net.silentchaos512.utils.EnumUtils;

public class AbstractMachineBaseContainer<T extends AbstractMachineBaseTileEntity> extends AbstractEnergyStorageContainer<T> {
    protected AbstractMachineBaseContainer(ContainerType<?> type, int id, T tileEntityIn, IIntArray fieldsIn) {
        super(type, id, tileEntityIn, fieldsIn);
    }

    protected final void addUpgradeSlots() {
        int upgradeSlots = this.tileEntity.tier.getUpgradeSlots();
        int inventorySize = this.tileEntity.getContainerSize();
        for (int i = 0; i < upgradeSlots; ++i) {
            int index = inventorySize - upgradeSlots + i;
            int xPosition = 6 + 18 * i;
            this.addSlot(new MachineUpgradeSlot(this.tileEntity, index, xPosition, -13));
        }
    }

    public RedstoneMode getRedstoneMode() {
        return EnumUtils.byOrdinal(fields.get(4), RedstoneMode.IGNORED);
    }

    public void setRedstoneMode(RedstoneMode mode) {
        fields.set(4, mode.ordinal());
    }
}
