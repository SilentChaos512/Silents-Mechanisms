package net.silentchaos512.mechanisms.block.generator.lava;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.TextUtil;
import net.silentchaos512.utils.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LavaGeneratorTileEntity extends AbstractMachineBaseTileEntity {
    // Energy constants
    public static final int MAX_ENERGY = 50_000;
    public static final int MAX_SEND = 500;
    public static final int ENERGY_CREATED_PER_TICK = 100;
    public static final int TICKS_PER_MILLIBUCKET = 5;

    private final FluidTank tank = new FluidTank(4000, s -> s.getFluid().isIn(FluidTags.LAVA));
    private final LazyOptional<IFluidHandler> fluidHandlerCap = LazyOptional.of(() -> tank);
    private int burnTime;
    private int totalBurnTime;

    private final IIntArray fields = new IIntArray() {
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
                    return redstoneMode.ordinal();
                case 3:
                    return burnTime;
                case 4:
                    return tank.getFluidAmount();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2:
                    redstoneMode = EnumUtils.byOrdinal(value, RedstoneMode.IGNORED);
                    break;
                case 3:
                    burnTime = value;
                    break;
                case 4:
                    tank.setFluid(new FluidStack(Fluids.LAVA, value));
                    break;
            }
        }

        @Override
        public int size() {
            return 5;
        }
    };

    public LavaGeneratorTileEntity() {
        super(ModTileEntities.lavaGenerator, 2, MAX_ENERGY, 0, MAX_SEND);
    }

    public IFluidHandler getTank() {
        return tank;
    }

    @Override
    public void tick() {
        // Drain buckets into internal tank
        ItemStack input = getStackInSlot(0);
        if (!input.isEmpty() && input.getItem() instanceof BucketItem) {
            tryFillTankWithBucket(input);
        }

        if (canRun()) {
            // Generate energy from fuel
            if (burnTime <= 0 && tank.getFluidInTank(0).getAmount() > 0) {
                tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                burnTime = TICKS_PER_MILLIBUCKET;
                sendUpdate(getBlockState(), true);
            }
            if (burnTime > 0) {
                --burnTime;
                energy.createEnergy(ENERGY_CREATED_PER_TICK);
            } else {
                // TODO: Set inactive state
            }
        }

        super.tick();
    }

    private void tryFillTankWithBucket(ItemStack input) {
        Fluid fluid = ((BucketItem) input.getItem()).getFluid();
        FluidStack fluidStack = new FluidStack(fluid, 1000);
        if (tank.isFluidValid(0, fluidStack) && tank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) == 1000) {
            tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            sendUpdate(getBlockState(), true);
        }
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

    private boolean canRun() {
        return world != null
                && redstoneMode.shouldRun(world.isBlockPowered(pos))
                && getEnergyStored() < getMaxEnergyStored();
    }

    private void sendUpdate(BlockState newState, boolean force) {
        if (world == null) return;
        BlockState oldState = world.getBlockState(pos);
        if (oldState != newState || force) {
            world.setBlockState(pos, newState, 3);
            world.notifyBlockUpdate(pos, oldState, newState, 3);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 0
                && stack.getItem() instanceof BucketItem
                && ((BucketItem) stack.getItem()).getFluid().isIn(FluidTags.LAVA);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "lava_generator");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new LavaGeneratorContainer(id, playerInventory, this, this.fields);
    }

    @Override
    public void read(CompoundNBT tags) {
        super.read(tags);
        if (tags.contains("FluidTank")) {
            this.tank.setFluid(FluidStack.loadFluidStackFromNBT(tags.getCompound("FluidTank")));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        tags.put("FluidTank", this.tank.writeToNBT(new CompoundNBT()));
        return super.write(tags);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        CompoundNBT tags = packet.getNbtCompound();
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
    public void remove() {
        super.remove();
        fluidHandlerCap.invalidate();
    }
}
