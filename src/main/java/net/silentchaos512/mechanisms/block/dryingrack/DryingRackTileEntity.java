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
import net.silentchaos512.mechanisms.init.ModRecipes;
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
        ItemStack stack = getItem(0);
        if (!stack.isEmpty()) {
            // Remove hanging item
            PlayerUtils.giveItem(player, stack);
            setItem(0, ItemStack.EMPTY);
            return true;
        } else {
            // Hang item on rack
            ItemStack heldItem = player.getMainHandItem();
            if (!heldItem.isEmpty()) {
                setItem(0, heldItem.split(1));
                return true;
            }
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide || isEmpty()) return;

        DryingRecipe recipe = this.level.getRecipeManager().getRecipeFor(ModRecipes.Types.DRYING, this, this.level).orElse(null);
        if (recipe != null && canWork()) {
            ++processTime;
            if (processTime >= recipe.getProcessTime()) {
                setItem(0, recipe.assemble(this));
                processTime = 0;
            }
            if (processTime % 10 == 0) {
                ParticleUtils.spawn(level, ParticleTypes.SMOKE, worldPosition, 1, 0.1, 0.1, 0.1, 0.01);
            }
        } else {
            processTime = 0;
        }
    }

    private boolean canWork() {
        return level != null && !level.getBlockState(worldPosition).getValue(DryingRackBlock.WATERLOGGED);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        sendUpdate();
    }

    private void sendUpdate() {
        if (this.level != null) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(0);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = ItemStackHelper.removeItem(this.items, index, count);
        this.setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack result = ItemStackHelper.takeItem(this.items, index);
        this.setChanged();
        return result;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(0, stack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        if (compound.contains("Item")) {
            setItem(0, ItemStack.of(compound.getCompound("Item")));
        }
        this.processTime = compound.getInt("ProcessTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            compound.put("Item", stack.save(new CompoundNBT()));
        }
        compound.putInt("ProcessTime", this.processTime);
        return compound;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            nbt.put("Item", stack.save(new CompoundNBT()));
        }
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (pkt.getTag().contains("Item")) {
            this.items.set(0, ItemStack.of(pkt.getTag().getCompound("Item")));
        } else {
            this.items.set(0, ItemStack.EMPTY);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandlerCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        itemHandlerCap.invalidate();
    }
}
