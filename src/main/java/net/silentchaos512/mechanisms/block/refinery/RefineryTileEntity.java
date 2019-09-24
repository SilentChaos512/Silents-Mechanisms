package net.silentchaos512.mechanisms.block.refinery;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;
import net.silentchaos512.utils.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class RefineryTileEntity extends AbstractMachineBaseTileEntity implements IFluidHandler {
    public static final int FIELDS_COUNT = 17; // TODO
    public static final int TANK_CAPACITY = 4_000;

    private float progress;
    private int processTime;
    private final FluidTank[] tanks;
    private final LazyOptional<IFluidHandler> fluidHandlerCap;

    protected final IIntArray fields = new IIntArray() {
        @SuppressWarnings("deprecation") // Use of Registry
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
                    return (int) progress;
                case 6:
                    return processTime;
                case 7:
                    return Registry.FLUID.getId(tanks[0].getFluid().getFluid());
                case 8:
                    return tanks[0].getFluid().getAmount();
                case 9:
                    return Registry.FLUID.getId(tanks[1].getFluid().getFluid());
                case 10:
                    return tanks[1].getFluid().getAmount();
                case 11:
                    return Registry.FLUID.getId(tanks[2].getFluid().getFluid());
                case 12:
                    return tanks[2].getFluid().getAmount();
                case 13:
                    return Registry.FLUID.getId(tanks[3].getFluid().getFluid());
                case 14:
                    return tanks[3].getFluid().getAmount();
                case 15:
                    return Registry.FLUID.getId(tanks[4].getFluid().getFluid());
                case 16:
                    return tanks[4].getFluid().getAmount();
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
                    progress = value;
                    break;
                case 6:
                    processTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return FIELDS_COUNT;
        }
    };

    public RefineryTileEntity() {
        super(ModTileEntities.refinery, 2, MachineTier.STANDARD.getEnergyCapacity(), 500, 0, MachineTier.BASIC);
        tanks = IntStream.range(0, 5).mapToObj(k -> new FluidTank(TANK_CAPACITY)).toArray(FluidTank[]::new);
        fluidHandlerCap = LazyOptional.of(() -> this);
    }

    public FluidStack getFluid(int tank) {
        if (tank < 0 || tank >= tanks.length) {
            return FluidStack.EMPTY;
        }
        return tanks[tank].getFluid();
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        ItemStack input = getStackInSlot(0);
        if (!input.isEmpty() && input.getItem() instanceof BucketItem) {
            tryFillTankWithBucket(input);
        }
    }

    private void tryFillTankWithBucket(ItemStack input) {
        Fluid fluid = ((BucketItem) input.getItem()).getFluid();
        FluidStack fluidStack = new FluidStack(fluid, 1000);
        if (canAcceptFluidBucket(input, fluidStack)) {
            this.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            sendUpdate(getBlockState(), true);

            setInventorySlotContents(0, input.getContainerItem());

            // Move buckets to output slot
            ItemStack input2 = getStackInSlot(0);
            if (!input2.isEmpty()) {
                ItemStack output = getStackInSlot(1);
                if (output.isEmpty()) {
                    setInventorySlotContents(1, input2);
                    setInventorySlotContents(0, ItemStack.EMPTY);
                } else if (InventoryUtils.canItemsStack(getStackInSlot(0), output)) {
                    output.grow(1);
                    input2.shrink(1);
                }
            }
        }
    }

    private boolean canAcceptFluidBucket(ItemStack input, FluidStack fluid) {
        ItemStack output = getStackInSlot(1);
        return this.isFluidValid(0, fluid)
                && this.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == 1000
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    protected void sendUpdate(BlockState newState, boolean force) {
        if (world == null) return;
        BlockState oldState = world.getBlockState(pos);
        if (oldState != newState || force) {
            world.setBlockState(pos, newState, 3);
            world.notifyBlockUpdate(pos, oldState, newState, 3);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index == 0 && itemStackIn.getItem() instanceof BucketItem;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new RefineryContainer(id, player, this, this.fields);
    }

    @Override
    public void read(CompoundNBT tags) {
        SilentMechanisms.LOGGER.debug("read");
        for (int i = 0; i < tanks.length; ++i) {
            String key = "Tank" + i;
            if (tags.contains(key)) {
                tanks[i].setFluid(FluidStack.loadFluidStackFromNBT(tags.getCompound(key)));
            }
        }
        super.read(tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        SilentMechanisms.LOGGER.debug("write");
        for (int i = 0; i < tanks.length; ++i) {
            tags.put("Tank" + i, tanks[i].writeToNBT(new CompoundNBT()));
        }
        return super.write(tags);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        SilentMechanisms.LOGGER.debug("onDataPacket");
        super.onDataPacket(net, packet);
        CompoundNBT tags = packet.getNbtCompound();
        for (int i = 0; i < tanks.length; ++i) {
            String key = "Tank" + i;
            if (tags.contains(key)) {
                tanks[i].setFluid(FluidStack.loadFluidStackFromNBT(tags.getCompound(key)));
            }
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        SilentMechanisms.LOGGER.debug("getUpdateTag");
        CompoundNBT tags = super.getUpdateTag();
        for (int i = 0; i < tanks.length; ++i) {
            tags.put("Tank" + i, tanks[i].writeToNBT(new CompoundNBT()));
        }
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
    public void remove() {
        super.remove();
        fluidHandlerCap.invalidate();
    }

    @Override
    public int getTanks() {
        return tanks.length;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        if (tank < 0 || tank >= tanks.length) {
            return FluidStack.EMPTY;
        }
        return tanks[tank].getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        if (tank < 0 || tank >= tanks.length) {
            return 0;
        }
        return tanks[tank].getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        if (tank < 0 || tank >= tanks.length) {
            return false;
        }
        return tanks[tank].isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        FluidStack fluidInTank = tanks[0].getFluid();
        if (isFluidValid(0, resource) && (fluidInTank.isEmpty() || resource.isFluidEqual(fluidInTank))) {
            if (action != FluidAction.SIMULATE) {
                world.setBlockState(pos, getBlockState(), 3);
            }
            return tanks[0].fill(resource, action);
        }
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return FluidStack.EMPTY;
        }

        for (int i = 1; i < tanks.length; ++i) {
            if (resource.isFluidEqual(tanks[i].getFluid())) {
                return tanks[i].drain(resource, action);
            }
        }

        return FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (int i = 1; i < tanks.length; ++i) {
            if (tanks[i].getFluidAmount() > 0) {
                return tanks[i].drain(maxDrain, action);
            }
        }

        return FluidStack.EMPTY;
    }
}
