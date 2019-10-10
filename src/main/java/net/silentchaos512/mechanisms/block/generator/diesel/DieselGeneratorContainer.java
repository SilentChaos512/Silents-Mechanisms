package net.silentchaos512.mechanisms.block.generator.diesel;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.silentchaos512.mechanisms.api.IFluidContainer;
import net.silentchaos512.mechanisms.block.generator.AbstractFluidFuelGeneratorTileEntity;
import net.silentchaos512.mechanisms.block.generator.AbstractFluidGeneratorContainer;
import net.silentchaos512.mechanisms.init.ModContainers;
import net.silentchaos512.mechanisms.util.InventoryUtils;

public class DieselGeneratorContainer extends AbstractFluidGeneratorContainer<DieselGeneratorTileEntity> {
    public DieselGeneratorContainer(int id, PlayerInventory player) {
        this(id, player, new DieselGeneratorTileEntity(), new IntArray(AbstractFluidFuelGeneratorTileEntity.FIELDS_COUNT));
    }

    public DieselGeneratorContainer(int id, PlayerInventory player, DieselGeneratorTileEntity tileEntity, IIntArray fields) {
        super(ModContainers.dieselGenerator, id, tileEntity, fields);

        this.addSlot(new Slot(this.tileEntity, 0, 80, 16));
        this.addSlot(new Slot(this.tileEntity, 1, 80, 59));

        net.silentchaos512.lib.util.InventoryUtils.createPlayerSlots(player, 8, 84).forEach(this::addSlot);

        this.addUpgradeSlots();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    protected boolean isFuel(ItemStack stack) {
        return InventoryUtils.isFilledFluidContainer(stack)
                && IFluidContainer.getBucketOrContainerFluid(stack).getFluid().isIn(DieselGeneratorTileEntity.FUEL_TAG);
    }
}
