package net.silentchaos512.mechanisms.block.generator;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.silentchaos512.mechanisms.api.IFluidContainer;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.config.Config;
import net.silentchaos512.mechanisms.util.InventoryUtils;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.utils.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractFluidFuelGeneratorTileEntity extends AbstractGeneratorTileEntity {
    public static final int FIELDS_COUNT = 9;

    protected final FluidTank tank;
    private final LazyOptional<IFluidHandler> fluidHandlerCap;

    protected final IIntArray fields = new IIntArray() {
        @SuppressWarnings("deprecation") // Use of Registry.FLUID
        @Override
        public int get(int index) {
            switch (index) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case 0:
                    // Energy lower bytes
                    return getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    return getMaxEnergyStored() & 0xFFFF;
                case 3:
                    return (getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return redstoneMode.ordinal();
                case 5:
                    return burnTime;
                case 6:
                    return totalBurnTime;
                case 7:
                    return Registry.FLUID.getId(tank.getFluid().getFluid());
                case 8:
                    return tank.getFluidAmount();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    redstoneMode = EnumUtils.byOrdinal(value, RedstoneMode.IGNORED);
                    break;
                case 5:
                    burnTime = value;
                    break;
                case 6:
                    totalBurnTime = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return FIELDS_COUNT;
        }
    };

    protected AbstractFluidFuelGeneratorTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract, FluidTank tankIn) {
        super(typeIn, inventorySize, maxEnergy, maxReceive, maxExtract, MachineTier.STANDARD);
        this.tank = tankIn;
        this.fluidHandlerCap = LazyOptional.of(() -> tank);
    }

    protected abstract int getFuelBurnTime(FluidStack fluid);

    protected void tryFillTank(ItemStack item) {
        FluidStack fluid = IFluidContainer.getBucketOrContainerFluid(item);
        if (canAcceptFluidContainer(item, fluid)) {
            tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);

            ItemStack output = getItem(1);
            if (output.isEmpty()) {
                setItem(1, item.getContainerItem());
            } else {
                output.grow(1);
            }

            item.shrink(1);
        }
    }

    private boolean canAcceptFluidContainer(ItemStack item, FluidStack fluid) {
        ItemStack output = getItem(1);
        return !fluid.isEmpty()
                && tank.isFluidValid(0, fluid)
                && tank.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == fluid.getAmount()
                && (output.isEmpty() || InventoryUtils.canItemsStack(item.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    @Override
    protected void consumeFuel() {
        FluidStack fluid = tank.drain(Config.fluidGeneratorInjectionVolume.get(), IFluidHandler.FluidAction.EXECUTE);
        burnTime = totalBurnTime = getFuelBurnTime(fluid);
    }

    @Override
    public void tick() {
        // Drain fluid containers into internal tank
        if (tank.getFluidAmount() < tank.getTankCapacity(0) - 999) {
            ItemStack stack = getItem(0);
            if (!stack.isEmpty()) {
                tryFillTank(stack);
            }
        }
        super.tick();
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        if (tags.contains("FluidTank")) {
            this.tank.setFluid(FluidStack.loadFluidStackFromNBT(tags.getCompound("FluidTank")));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        tags.put("FluidTank", this.tank.writeToNBT(new CompoundNBT()));
        return super.save(tags);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        CompoundNBT tags = packet.getTag();
        if (tags.contains("FluidTank")) {
            this.tank.setFluid(FluidStack.loadFluidStackFromNBT(tags.getCompound("FluidTank")));
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.put("FluidTank", this.tank.writeToNBT(new CompoundNBT()));
        return tags;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidHandlerCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        fluidHandlerCap.invalidate();
    }
}
