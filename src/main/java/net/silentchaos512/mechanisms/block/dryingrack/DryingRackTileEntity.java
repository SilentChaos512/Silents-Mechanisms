package net.silentchaos512.mechanisms.block.dryingrack;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.silentchaos512.lib.util.PlayerUtils;
import net.silentchaos512.mechanisms.crafting.recipe.DryingRecipe;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.ParticleUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DryingRackTileEntity extends TileEntity implements IInventory, ITickableTileEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private int processTime;

    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> new InvWrapper(this));

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
            setInventorySlotContents(0, ItemStack.EMPTY);
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
        if (this.world == null || this.world.isRemote || isEmpty()) return;

        DryingRecipe recipe = this.world.getRecipeManager().getRecipe(DryingRecipe.RECIPE_TYPE, this, this.world).orElse(null);
        if (recipe != null && canWork()) {
            ++processTime;
            if (processTime >= recipe.getProcessTime()) {
                setInventorySlotContents(0, recipe.getCraftingResult(this));
                processTime = 0;
            }
            if (processTime % 10 == 0) {
                ParticleUtils.spawn(world, ParticleTypes.SMOKE, pos, 1, 0.1, 0.1, 0.1, 0.01);
            }
        } else {
            processTime = 0;
        }
    }

    private boolean canWork() {
        return world != null && !world.getBlockState(pos).get(DryingRackBlock.WATERLOGGED);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        sendUpdate();
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
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return isEmpty();
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
        ItemStack result = ItemStackHelper.getAndSplit(this.items, index, count);
        this.markDirty();
        return result;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack result = ItemStackHelper.getAndRemove(this.items, index);
        this.markDirty();
        return result;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.items.set(0, stack);
        this.markDirty();
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
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
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
            this.items.set(0, ItemStack.read(pkt.getNbtCompound().getCompound("Item")));
        } else {
            this.items.set(0, ItemStack.EMPTY);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandlerCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        itemHandlerCap.invalidate();
    }
}
