package net.silentchaos512.mechanisms.block.generator.lava;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseContainer;
import net.silentchaos512.mechanisms.block.generator.AbstractFluidFuelGeneratorTileEntity;
import net.silentchaos512.mechanisms.init.ModContainers;

public class LavaGeneratorContainer extends AbstractMachineBaseContainer<LavaGeneratorTileEntity> {
    final LavaGeneratorTileEntity tileEntity;
    private final FluidTank tank = new FluidTank(4000);

    public LavaGeneratorContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new LavaGeneratorTileEntity(), new IntArray(AbstractFluidFuelGeneratorTileEntity.FIELDS_COUNT));
    }

    public LavaGeneratorContainer(int id, PlayerInventory playerInventory, LavaGeneratorTileEntity tileEntity, IIntArray fieldsIn) {
        super(ModContainers.lavaGenerator, id, tileEntity, fieldsIn);
        this.tileEntity = tileEntity;

        this.addSlot(new Slot(this.tileEntity, 0, 80, 16));
        this.addSlot(new Slot(this.tileEntity, 1, 80, 59));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    public int getBurnTime() {
        return fields.get(3);
    }

    public int getFluidAmount() {
        return fields.get(5);
    }

    public IFluidHandler getTank() {
        // TODO: Need to figure out a way to pass the actual fluid to the client. Not relevant for
        //  lava generators, but it needs to be solved eventually.
        tank.setFluid(new FluidStack(Fluids.LAVA, getFluidAmount()));
        return tank;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = 1;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index != 0) {
                if (this.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerInventoryEnd) {
                    if (!this.mergeItemStack(itemstack1, playerInventoryEnd, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerHotbarEnd && !this.mergeItemStack(itemstack1, inventorySize, playerInventoryEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean isFuel(ItemStack stack) {
        return stack.getItem() instanceof BucketItem && ((BucketItem) stack.getItem()).getFluid().isIn(FluidTags.LAVA);
    }
}
