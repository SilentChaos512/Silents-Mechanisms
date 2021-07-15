package net.silentchaos512.mechanisms.block.mixer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.silentchaos512.mechanisms.api.IFluidContainer;
import net.silentchaos512.mechanisms.block.AbstractFluidMachineTileEntity;
import net.silentchaos512.mechanisms.crafting.recipe.MixingRecipe;
import net.silentchaos512.mechanisms.init.ModRecipes;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.InventoryUtils;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.Collection;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class MixerTileEntity extends AbstractFluidMachineTileEntity<MixingRecipe> {
    public static final int FIELDS_COUNT = 17;
    public static final int TANK_CAPACITY = 4_000;
    public static final int ENERGY_PER_TICK = 100;

    public MixerTileEntity() {
        super(ModTileEntities.mixer, 4, 5, TANK_CAPACITY, MachineTier.STANDARD);
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;

        tryFillTanks();
        tryFillFluidContainer();

        super.tick();
    }

    @Override
    protected void consumeIngredients(MixingRecipe recipe) {
        // NO-OP
    }

    private void tryFillTanks() {
        // Try fill feedstock tanks with fluid containers
        ItemStack input = getItem(0);
        if (input.isEmpty()) return;

        FluidStack fluidStack = IFluidContainer.getBucketOrContainerFluid(input);
        int tankIndex = findTankWithFluid(fluidStack);
        if (tankIndex > -1) {
            if (canAcceptFluidContainer(input, fluidStack, tankIndex)) {
                fillTankWithContainer(input, fluidStack, tanks[tankIndex]);
            }
        } else {
            for (int i = 0; i < getInputTanks(); ++i) {
                if (canAcceptFluidContainer(input, fluidStack, i)) {
                    fillTankWithContainer(input, fluidStack, tanks[i]);
                    break;
                }
            }
        }
    }

    private int findTankWithFluid(FluidStack fluidStack) {
        for (int i = 0; i < getInputTanks(); ++i) {
            FluidStack fluidInTank = getFluidInTank(i);
            if (!fluidInTank.isEmpty() && fluidInTank.getFluid() == fluidStack.getFluid()) {
                return i;
            }
        }
        return -1;
    }

    private void fillTankWithContainer(ItemStack input, FluidStack fluidStack, IFluidHandler tank) {
        tank.fill(fluidStack, FluidAction.EXECUTE);

        ItemStack containerItem = input.getContainerItem();
        input.shrink(1);

        ItemStack output = getItem(1);
        if (output.isEmpty()) {
            setItem(1, containerItem);
        } else {
            output.grow(1);
        }
    }

    private boolean canAcceptFluidContainer(ItemStack input, FluidStack fluid, int tank) {
        ItemStack output = getItem(1);
        return !fluid.isEmpty()
                && this.isFluidValid(tank, fluid)
                && tanks[tank].fill(fluid, IFluidHandler.FluidAction.SIMULATE) == fluid.getAmount()
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    private void tryFillFluidContainer() {
        // Fill empty fluid containers with output fluids
        ItemStack input = getItem(2);
        if (input.isEmpty()) return;

        FluidStack fluidInInput = IFluidContainer.getBucketOrContainerFluid(input);
        if (!fluidInInput.isEmpty()) return;

        FluidStack fluidInTank = getFluidInTank(4);
        if (fluidInTank.getAmount() >= 1000) {
            ItemStack filled = IFluidContainer.fillBucketOrFluidContainer(input, fluidInTank);
            if (!filled.isEmpty() && InventoryUtils.mergeItem(this, filled, 3)) {
                tanks[4].drain(1000, FluidAction.EXECUTE);
                input.shrink(1);
            }
        }
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return ENERGY_PER_TICK;
    }

    @Override
    protected int getInputTanks() {
        return 4;
    }

    @Override
    protected int getOutputTanks() {
        return 1;
    }

    @Nullable
    @Override
    public MixingRecipe getRecipe() {
        if (level == null) return null;
        return level.getRecipeManager().getRecipeFor(ModRecipes.Types.MIXING, this, level).orElse(null);
    }

    @Override
    protected int getProcessTime(MixingRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<FluidStack> getFluidResults(MixingRecipe recipe) {
        return recipe.getFluidResults(this);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return (index == 0 && InventoryUtils.isFilledFluidContainer(stack)) || (index == 2 && InventoryUtils.isEmptyFluidContainer(stack));
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1 || index == 3;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "mixer");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new MixerContainer(id, player, this, this.fields);
    }
}
