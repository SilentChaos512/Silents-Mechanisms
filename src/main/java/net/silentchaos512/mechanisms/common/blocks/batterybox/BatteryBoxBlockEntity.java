package net.silentchaos512.mechanisms.common.blocks.batterybox;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.mechanisms.common.abstracts.BaseMachineBlockEntity;
import net.silentchaos512.mechanisms.common.abstracts.TickableBlockEntity;
import net.silentchaos512.mechanisms.common.capability.energy.FlexibleEnergyStorage;
import net.silentchaos512.mechanisms.init.ModBlockEntities;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BatteryBoxBlockEntity extends BaseMachineBlockEntity implements TickableBlockEntity {
    public static final int CONTAINER_SIZE = 6;
    public static final int ENERGY_CAP = 500_000;
    public static final int IO_RATE = 750;
    private final NonNullConsumer<IEnergyStorage> batteryRechargeLogic;
    private final ContainerData containerData;

    public BatteryBoxBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.BATTERY_BOX, pWorldPosition, pBlockState, new BatteryBoxEnergyStorage(), CONTAINER_SIZE);
        this.batteryRechargeLogic = (batteryEnergy) -> {
            if (batteryEnergy.canReceive() && batteryEnergy.receiveEnergy(IO_RATE, true) > 0) {
                batteryEnergy.receiveEnergy(this.getEnergyStorage().extractEnergy(IO_RATE, false), false);
            }
        };

        this.getEnergyStorage().setContainer(this);

        this.containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return pIndex == 0 ? energyStorage.getEnergyStored() : energyStorage.getMaxEnergyStored();
            }

            @Override
            public void set(int pIndex, int pValue) {

            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }



    @Override
    public BatteryBoxEnergyStorage getEnergyStorage() {
        return (BatteryBoxEnergyStorage) super.getEnergyStorage();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return this.isEmpty() ? LazyOptional.empty() : super.getCapability(cap, side);
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return pStack.is(ModItems.BATTERY);
    }

    @Override
    protected boolean canTakeItem(ItemStack stack, int slot) {
        return stack.is(ModItems.BATTERY);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (!this.isEmpty()) {
            for (ItemStack item : this.items) {
                if (item.is(ModItems.BATTERY)) {
                    LazyOptional<IEnergyStorage> batteryEnergyHolder = item.getCapability(ForgeCapabilities.ENERGY);
                    if (!this.energyStorage.isEmpty()) {
                        batteryEnergyHolder.ifPresent(batteryRechargeLogic);
                    }
                }
            }
        }

        state = state.setValue(BatteryBox.BATTERIES, this.getBatteryCount());
        level.setBlock(pos, state, 3);
        setChanged(level, pos, state);
    }


    @Nonnull
    @Override
    public Component getDisplayName() {
        return ModBlocks.BATTERY_BOX.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new BatteryBoxMenu(pContainerId, pPlayerInventory, this, this.containerData, ContainerLevelAccess.create(pPlayer.level, this.worldPosition));
    }

    protected final int getBatteryCount() {
        return (int) this.getItemContainer().stream().filter(item -> item.is(ModItems.BATTERY)).count();
    }
}