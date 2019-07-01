package net.silentchaos512.mechanisms.block.crusher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.World;
import net.silentchaos512.lib.inventory.SlotOutputOnly;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.init.ModContainers;

public class CrusherContainer extends Container {
    final CrusherTileEntity tileEntity;
    private final World world;

    public CrusherContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new CrusherTileEntity());
    }

    public CrusherContainer(int id, PlayerInventory playerInventory, CrusherTileEntity tileEntity) {
        super(ModContainers.crusher, id);
        this.tileEntity = tileEntity;
        this.world = playerInventory.player.world;

        this.addSlot(new Slot(this.tileEntity, 0, 26, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 1, 80, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 2, 98, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 3, 116, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 4, 134, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

        func_216961_a(this.tileEntity.fields);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        // FIXME: world is null?
//        return this.tileEntity.isUsableByPlayer(playerIn);
        return true;
    }
}
