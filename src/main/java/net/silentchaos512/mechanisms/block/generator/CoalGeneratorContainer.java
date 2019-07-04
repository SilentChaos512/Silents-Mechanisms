package net.silentchaos512.mechanisms.block.generator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.init.ModContainers;

public class CoalGeneratorContainer extends Container {
    final CoalGeneratorTileEntity tileEntity;

    public CoalGeneratorContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new CoalGeneratorTileEntity());
    }

    public CoalGeneratorContainer(int id, PlayerInventory playerInventory, CoalGeneratorTileEntity tileEntity) {
        super(ModContainers.coalGenerator, id);
        this.tileEntity = tileEntity;

        this.addSlot(new Slot(this.tileEntity, 0, 80, 33));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

        func_216961_a(this.tileEntity.fields);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
