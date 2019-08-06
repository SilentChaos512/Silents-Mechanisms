package net.silentchaos512.mechanisms.block.dryingrack;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.silentchaos512.lib.util.PlayerUtils;
import net.silentchaos512.mechanisms.crafting.recipe.DryingRecipe;
import net.silentchaos512.mechanisms.init.ModTileEntities;

import javax.annotation.Nullable;

public class DryingRackTileEntity extends TileEntity implements IInventory, ITickableTileEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private int processTime;

    public DryingRackTileEntity() {
        super(ModTileEntities.dryingRack);
    }

    public ItemStack getItem() {
        return items.get(0);
    }

    public boolean interact(PlayerEntity player) {
        ItemStack stack = getStackInSlot(0);
        if (!stack.isEmpty()) {
            // Remove hanging item
            PlayerUtils.giveItem(player, stack);
            decrStackSize(0, 1);
            return true;
        } else {
            // Hang item on rack
            ItemStack heldItem = player.getHeldItemMainhand();
            if (!heldItem.isEmpty()) {
                setInventorySlotContents(0, heldItem.split(1));
                return true;
            }
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.world == null || isEmpty()) return;

        DryingRecipe recipe = this.world.getRecipeManager().getRecipe(DryingRecipe.RECIPE_TYPE, this, this.world).orElse(null);
        if (recipe != null) {
            ++processTime;
            if (processTime >= recipe.getProcessTime()) {
                setInventorySlotContents(0, recipe.getCraftingResult(this));
                processTime = 0;
                sendUpdate();
            }
        } else {
            processTime = 0;
        }
    }

    private void sendUpdate() {
        if (this.world != null) {
            BlockState state = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 3);
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(0);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.items.set(0, stack);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("Item")) {
            setInventorySlotContents(0, ItemStack.read(compound.getCompound("Item")));
        }
        this.processTime = compound.getInt("ProcessTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            compound.put("Item", stack.write(new CompoundNBT()));
        }
        compound.putInt("ProcessTime", this.processTime);
        return compound;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            nbt.put("Item", stack.write(new CompoundNBT()));
        }
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (pkt.getNbtCompound().contains("Item")) {
            setInventorySlotContents(0, ItemStack.read(pkt.getNbtCompound().getCompound("Item")));
        }
    }
}
