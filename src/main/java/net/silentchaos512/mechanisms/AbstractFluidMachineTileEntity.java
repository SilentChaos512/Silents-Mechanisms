package net.silentchaos512.mechanisms;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
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
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.FluidIngredient;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.IFluidRecipe;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;
import net.silentchaos512.mechanisms.item.MachineUpgrades;
import net.silentchaos512.mechanisms.util.Constants;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.utils.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.IntStream;

public abstract class AbstractFluidMachineTileEntity<R extends IFluidRecipe<?>> extends AbstractMachineBaseTileEntity implements IFluidHandler {
    protected float progress;
    protected int processTime;
    protected final FluidTank[] tanks;
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
                default:
                    int tankIndex = (index - 7) / 2;
                    if (tankIndex >= tanks.length) {
                        return 0;
                    } else if (index % 2 == 1) {
                        return Registry.FLUID.getId(tanks[tankIndex].getFluid().getFluid());
                    } else {
                        return tanks[tankIndex].getFluidAmount();
                    }
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
            return 7 + 2 * tanks.length;
        }
    };

    protected AbstractFluidMachineTileEntity(TileEntityType<?> typeIn, int inventorySize, int tankCount, int tankCapacity, int maxEnergy, int maxReceive, int maxExtract, MachineTier tier) {
        super(typeIn, inventorySize, maxEnergy, maxReceive, maxExtract, tier);
        this.tanks = IntStream.range(0, tankCount).mapToObj(k -> new FluidTank(tankCapacity)).toArray(FluidTank[]::new);
        this.fluidHandlerCap = LazyOptional.of(() -> this);
    }

    protected abstract int getEnergyUsedPerTick();

    protected abstract int getInputTanks();

    protected abstract int getOutputTanks();

    @Nullable
    public abstract R getRecipe();

    protected abstract int getProcessTime(R recipe);

    /**
     * Get the processing speed. This is added to processing progress every tick. A speed of 1 would
     * process a 200 tick recipe in 200 ticks, speed 2 would be 100 ticks. Should account for speed
     * upgrades.
     *
     * @return The processing speed
     */
    protected float getProcessSpeed() {
        int speedUpgrades = getUpgradeCount(MachineUpgrades.PROCESSING_SPEED);
        return tier.getProcessingSpeed() + speedUpgrades * Constants.UPGRADE_PROCESSING_SPEED_AMOUNT;
    }

    protected abstract Collection<FluidStack> getProcessResults(R recipe);

    /**
     * Get all possible results of processing this recipe. Override if recipes can contain a
     * variable number of outputs.
     *
     * @param recipe The recipe
     * @return All possible results of the processing operation
     */
    protected Collection<FluidStack> getPossibleProcessResult(R recipe) {
        return getProcessResults(recipe);
    }

    protected BlockState getActiveState(BlockState currentState) {
        return currentState.with(AbstractFurnaceBlock.LIT, true);
    }

    protected BlockState getInactiveState(BlockState currentState) {
        return currentState.with(AbstractFurnaceBlock.LIT, false);
    }

    protected void sendUpdate(BlockState newState) {
        if (world == null) return;
        BlockState oldState = world.getBlockState(pos);
        if (oldState != newState) {
            world.setBlockState(pos, newState, 3);
            world.notifyBlockUpdate(pos, oldState, newState, 3);
        }
    }

    protected void setInactiveState() {
        if (world == null) return;
        progress = 0;
        sendUpdate(getInactiveState(world.getBlockState(pos)));
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        R recipe = getRecipe();
        if (recipe != null && canMachineRun(recipe)) {
            // Process
            processTime = getProcessTime(recipe);
            progress += getProcessSpeed();
            energy.consumeEnergy((int) (getEnergyUsedPerTick() * getUpgradesEnergyMultiplier()));

            if (progress >= processTime) {
                // Create result
                getProcessResults(recipe).forEach(this::storeResultFluid);
                consumeFeedstock(recipe);

                if (getRecipe() == null) {
                    // Nothing left to process
                    setInactiveState();
                } else {
                    // Continue processing next output
                    progress = 0;
                }
            } else {
                sendUpdate(getActiveState(world.getBlockState(pos)));
            }
        } else {
            setInactiveState();
        }
    }

    private boolean canMachineRun(R recipe) {
        return world != null
                && getEnergyStored() >= getEnergyUsedPerTick()
                && hasRoomInOutput(getPossibleProcessResult(recipe))
                && redstoneMode.shouldRun(world.getRedstonePowerFromNeighbors(pos) > 0);
    }

    private boolean hasRoomInOutput(Iterable<FluidStack> results) {
        for (FluidStack stack : results) {
            if (!hasRoomForOutputFluid(stack)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasRoomForOutputFluid(FluidStack stack) {
        final int inputs = getInputTanks();
        final int outputs = getOutputTanks();
        for (int i = inputs; i < inputs + outputs; ++i) {
            if (tanks[i].fill(stack, FluidAction.SIMULATE) == stack.getAmount()) {
                return true;
            }
        }
        return false;
    }

    private void storeResultFluid(FluidStack stack) {
        final int inputs = getInputTanks();
        final int outputs = getOutputTanks();
        // Merge the fluid into any output tank it can fit in
        for (int i = inputs; i < inputs + outputs; ++i) {
            if (tanks[i].fill(stack, FluidAction.SIMULATE) == stack.getAmount()) {
                tanks[i].fill(stack, FluidAction.EXECUTE);
                break;
            }
        }
    }

    protected void consumeFeedstock(R recipe) {
        for (FluidIngredient ingredient : recipe.getFluidIngredients()) {
            for (int i = 0; i < getInputTanks(); ++i) {
                if (ingredient.test(getFluidInTank(i))) {
                    tanks[i].drain(ingredient.getAmount(), FluidAction.EXECUTE);
                    break;
                }
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        fluidHandlerCap.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidHandlerCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void read(CompoundNBT tags) {
        ListNBT list = tags.getList("Tanks", 10);
        for (int i = 0; i < tanks.length && i < list.size(); ++i) {
            INBT nbt = list.get(i);
            tanks[i].setFluid(FluidStack.loadFluidStackFromNBT((CompoundNBT) nbt));
        }
        progress = tags.getInt("Progress");
        processTime = tags.getInt("ProcessTime");
        super.read(tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        ListNBT list = new ListNBT();
        for (FluidTank tank : tanks) {
            list.add(tank.writeToNBT(new CompoundNBT()));
        }
        tags.put("Tanks", list);
        tags.putInt("Progess", (int) progress);
        tags.putInt("ProcessTime", processTime);
        return super.write(tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("Progess", (int) progress);
        tags.putInt("ProcessTime", processTime);
        return tags;
    }

    @Override
    public int getTanks() {
        return tanks.length;
    }

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
        for (int i = 0; i < tanks.length; ++i) {
            FluidStack fluidInTank = tanks[i].getFluid();
            if (isFluidValid(i, resource) && (fluidInTank.isEmpty() || resource.isFluidEqual(fluidInTank))) {
                return tanks[i].fill(resource, action);
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return FluidStack.EMPTY;
        }

        for (int i = 0; i < tanks.length; ++i) {
            if (resource.isFluidEqual(tanks[i].getFluid())) {
                return tanks[i].drain(resource, action);
            }
        }

        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (int i = 0; i < tanks.length; ++i) {
            if (tanks[i].getFluidAmount() > 0) {
                return tanks[i].drain(maxDrain, action);
            }
        }

        return FluidStack.EMPTY;
    }
}
