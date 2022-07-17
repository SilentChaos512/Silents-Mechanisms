package net.silentchaos512.mechanisms.abstracts;

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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.silentchaos512.mechanisms.capability.RestrictedEnergyStorage;
import org.jetbrains.annotations.NotNull;

public abstract class BaseMachineBlockEntity extends BaseEnergyBlockEntity implements ImplementedContainer, TickableBlockEntity {

    protected final NonNullList<ItemStack> items;
    private LazyOptional<IItemHandler> itemHolder;

    /**
     * @param pType          - Respective Block Entity type
     * @param pWorldPosition - Block Position
     * @param pBlockState    - Block State
     * @param energyStorage  - Energy Storage
     * @param containerSize  - This should include the numbers of available slots for container <b>AND 4 UPGRADE SLOTS whose indexes are in the last</b>
     */
    public BaseMachineBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, RestrictedEnergyStorage energyStorage, int containerSize) {
        super(pType, pWorldPosition, pBlockState, energyStorage);
        this.items = NonNullList.withSize(containerSize, ItemStack.EMPTY);
        this.itemHolder = LazyOptional.of(() -> new InvWrapper(this));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.itemHolder.cast();
        }

        return super.getEnergyCap(cap);
    }

    public void sendEnergyToBlocks(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));
            if (blockEntity != null) {
                LazyOptional<IEnergyStorage> energyStorageHolder = blockEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
                if (energyStorageHolder.isPresent() && this.energyStorage.canExtract())
                    this.energyStorage.extractEnergy(energyStorageHolder.map(energy -> energy.canReceive() ? energy.receiveEnergy(this.getEnergySpace(), false) : 0).get(), false);
            }
        }
    }

    public void receiveEnergyFromBlock(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));
            if (blockEntity != null) {
                LazyOptional<IEnergyStorage> energyHolder = blockEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
                if (energyHolder.isPresent() && this.energyStorage.canReceive()) {
                    this.energyStorage.receiveEnergy(energyHolder.map(energy -> energy.canExtract() ? energy.extractEnergy(this.getEnergySpace(), false) : 0).get(), false);
                }
            }
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ContainerHelper.loadAllItems(pTag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
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

    @Override
    public RestrictedEnergyStorage getEnergyStorage() {
        return (RestrictedEnergyStorage) super.getEnergyStorage();
    }
}
