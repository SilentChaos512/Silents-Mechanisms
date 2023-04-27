package net.silentchaos512.mechanisms.common.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.silentchaos512.mechanisms.common.capability.energy.FlexibleEnergyStorage;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMachineBlockEntity extends BaseEnergyBlockEntity implements ImplementedContainer, TickableBlockEntity {

    protected final NonNullList<ItemStack> items;
    private LazyOptional<IItemHandler> itemHolder;

    public BaseMachineBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, FlexibleEnergyStorage energyStorage, int containerSize) {
        super(pType, pWorldPosition, pBlockState, energyStorage);
        this.items = NonNullList.withSize(containerSize, ItemStack.EMPTY);
        this.itemHolder = LazyOptional.of(() -> new InvWrapper(this) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return BaseMachineBlockEntity.this.canTakeItem(this.getInv().getItem(slot), slot) ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
            }
        });
    }

    protected boolean canTakeItem(ItemStack stack, int slot) {
        return true;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemHolder.cast();
        }

        return super.getEnergyCap(cap);
    }

    public void sendEnergyToBlocks(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));
            if (blockEntity != null) {
                LazyOptional<IEnergyStorage> energyStorageHolder = blockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
                if (energyStorageHolder.isPresent() && this.energyStorage.canExtract())
                    this.energyStorage.extractEnergy(energyStorageHolder.map(energy -> energy.canReceive() ? energy.receiveEnergy(this.getEnergySpace(), false) : 0).get(), false);
            }
        }
    }

    @SuppressWarnings("unused")
    public void receiveEnergyFromBlock(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));
            if (blockEntity != null) {
                LazyOptional<IEnergyStorage> energyHolder = blockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
                if (energyHolder.isPresent() && this.energyStorage.canReceive()) {
                    this.energyStorage.receiveEnergy(energyHolder.map(energy -> energy.canExtract() ? energy.extractEnergy(this.getEnergySpace(), false) : 0).get(), false);
                }
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        ContainerHelper.loadAllItems(pTag, this.items);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, this.items);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHolder.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.itemHolder = LazyOptional.of(() -> new InvWrapper(this));
    }

    @Override
    public NonNullList<ItemStack> getItemContainer() {
        return items;
    }
}
