package net.silentchaos512.mechanisms.common.blocks.dryingracks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.silentchaos512.lib.util.PlayerUtils;
import net.silentchaos512.mechanisms.common.abstracts.TickableBlockEntity;
import net.silentchaos512.mechanisms.init.ModBlockEntities;
import net.silentchaos512.mechanisms.recipes.RackDryingRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DryingRackBlockEntity extends BlockEntity implements TickableBlockEntity, Container {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> new InvWrapper(this));
    int dryingDuration;
    int dryingTime;

    public DryingRackBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.DRYING_RACKS, pWorldPosition, pBlockState);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (!this.items.get(0).isEmpty() && !state.getValue(DryingRackBlock.WATERLOGGED)) {
            RackDryingRecipe recipe = level.getRecipeManager().getRecipeFor(RackDryingRecipe.RECIPE_TYPE, this, level).orElse(null);
            if (recipe != null) {
                this.dryingDuration = recipe.getDryingTime();

                if (this.dryingTime >= this.dryingDuration) {
                    setItem(0, recipe.assemble(this));
                    this.dryingTime = 0;
                } else this.dryingTime++;
            }
        } else {
            this.dryingTime = 0;
        }

        setChanged(level, pos, state);
        sendUpdate(level);
    }

    public boolean attemptInteract(Player player) {
        ItemStack itemOnRack = this.items.get(0);
        ItemStack handItem = player.getMainHandItem();

        if (!handItem.isEmpty() && itemOnRack.isEmpty()) {
            setItem(0, new ItemStack(handItem.getItem()));
            handItem.shrink(1);
            return true;
        }

        if (!itemOnRack.isEmpty()) {
            PlayerUtils.giveItem(player, itemOnRack);
            setItem(0, ItemStack.EMPTY);
            dryingDuration = 0;
            dryingTime = 0;
            return true;
        }

        return false;
    }

    protected ItemStack getItemOnRack() {
        return this.items.get(0);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        sendUpdate(this.level);
    }

    void sendUpdate(@Nullable Level level) {
        if (level != null) {
            BlockState state = level.getBlockState(this.worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return items.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(this.items, pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.items, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        items.set(pSlot, pStack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!remove && side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemHandlerCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("duration", this.dryingDuration);
        pTag.putInt("time", this.dryingTime);
        ContainerHelper.saveAllItems(pTag, this.items, false);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ContainerHelper.loadAllItems(pTag, items);
        this.dryingDuration = pTag.getInt("duration");
        this.dryingTime = pTag.getInt("time");
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerCap.invalidate();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, be -> saveWithoutMetadata());
    }

    /* Removed implementation due to having issues with renderer
        still keep this here as a note to not implement this ... in most cases

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        ContainerHelper.loadAllItems(pkt.getTag(), this.items);
    }*/
}
