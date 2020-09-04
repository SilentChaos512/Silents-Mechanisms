package net.silentchaos512.mechanisms.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
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
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.IFluidInventory;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.IFluidRecipe;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.utils.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

public abstract class AbstractFluidMachineTileEntity<R extends IFluidRecipe<?>> extends AbstractMachineTileEntity<R> implements IFluidInventory {
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

    protected AbstractFluidMachineTileEntity(TileEntityType<?> typeIn, int inventorySize, int tankCount, int tankCapacity, MachineTier tier) {
        super(typeIn, inventorySize, tier);
        this.tanks = IntStream.range(0, tankCount).mapToObj(k -> new FluidTank(tankCapacity)).toArray(FluidTank[]::new);
        this.fluidHandlerCap = LazyOptional.of(() -> this);
    }

    protected abstract int getInputTanks();

    protected abstract int getOutputTanks();

    protected abstract Collection<FluidStack> getFluidResults(R recipe);

    /**
     * Get all possible results of processing this recipe. Override if recipes can contain a
     * variable number of outputs.
     *
     * @param recipe The recipe
     * @return All possible results of the processing operation
     */
    protected Collection<FluidStack> getPossibleFluidResults(R recipe) {
        return getFluidResults(recipe);
    }

    @Override
    protected Collection<ItemStack> getProcessResults(R recipe) {
        return Collections.emptyList();
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[0];
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
                getFluidResults(recipe).forEach(this::storeResultFluid);
                getProcessResults(recipe).forEach(this::storeResultItem);
                consumeFeedstock(recipe);
                consumeIngredients(recipe);
                progress = 0;

                if (getRecipe() == null) {
                    setInactiveState();
                }
            } else {
                sendUpdate(getActiveState(world.getBlockState(pos)));
            }
        } else {
            if (recipe == null) {
                progress = 0;
            }
            setInactiveState();
        }
    }

    private boolean canMachineRun(R recipe) {
        return world != null
                && getEnergyStored() >= getEnergyUsedPerTick()
                && hasRoomInOutputTank(getPossibleFluidResults(recipe))
                && hasRoomInOutput(getPossibleProcessResult(recipe))
                && redstoneMode.shouldRun(world.getRedstonePowerFromNeighbors(pos) > 0);
    }

    private boolean hasRoomInOutputTank(Iterable<FluidStack> results) {
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

    private void consumeFeedstock(R recipe) {
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
    protected abstract void consumeIngredients(R recipe);

    @Override
    public void remove() {
        super.remove();
        fluidHandlerCap.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(cap, fluidHandlerCap.cast());
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void read(BlockState state, CompoundNBT tags) {
        ListNBT list = tags.getList("Tanks", 10);
        for (int i = 0; i < tanks.length && i < list.size(); ++i) {
            INBT nbt = list.get(i);
            tanks[i].setFluid(FluidStack.loadFluidStackFromNBT((CompoundNBT) nbt));
        }
        super.read(state, tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        ListNBT list = new ListNBT();
        for (FluidTank tank : tanks) {
            list.add(tank.writeToNBT(new CompoundNBT()));
        }
        tags.put("Tanks", list);
        return super.write(tags);
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
        for (int i = 0; i < getInputTanks(); ++i) {
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

        for (int i = getInputTanks(); i < getInputTanks() + getOutputTanks(); ++i) {
            if (resource.isFluidEqual(tanks[i].getFluid())) {
                return tanks[i].drain(resource, action);
            }
        }

        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (int i = getInputTanks(); i < getInputTanks() + getOutputTanks(); ++i) {
            if (tanks[i].getFluidAmount() > 0) {
                return tanks[i].drain(maxDrain, action);
            }
        }

        return FluidStack.EMPTY;
    }
}
