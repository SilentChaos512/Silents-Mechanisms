package net.silentchaos512.mechanisms.block.refinery;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseContainer;
import net.silentchaos512.mechanisms.init.ModContainers;

public class RefineryContainer extends AbstractMachineBaseContainer<RefineryTileEntity> {
    public RefineryContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new RefineryTileEntity(), new IntArray(RefineryTileEntity.FIELDS_COUNT));
    }

    public RefineryContainer(int id, PlayerInventory playerInventory, RefineryTileEntity tileEntity, IIntArray fieldsIn) {
        super(ModContainers.refinery, id, tileEntity, fieldsIn);

        this.addSlot(new Slot(this.tileEntity, 0, 8, 16));
        this.addSlot(new Slot(this.tileEntity, 1, 8, 59));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

        this.addUpgradeSlots();
    }
}
