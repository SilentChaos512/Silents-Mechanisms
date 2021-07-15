package net.silentchaos512.mechanisms.block.refinery;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.silentchaos512.mechanisms.block.AbstractFluidMachineTileEntity;
import net.silentchaos512.mechanisms.api.IFluidContainer;
import net.silentchaos512.mechanisms.crafting.recipe.RefiningRecipe;
import net.silentchaos512.mechanisms.init.ModRecipes;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.InventoryUtils;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.Collection;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class RefineryTileEntity extends AbstractFluidMachineTileEntity<RefiningRecipe> {
    public static final int FIELDS_COUNT = 17;
    public static final int TANK_CAPACITY = 4_000;
    public static final int ENERGY_PER_TICK = 100;

    public RefineryTileEntity() {
        super(ModTileEntities.refinery, 4, 5, TANK_CAPACITY, MachineTier.STANDARD);
    }

    @Nullable
    @Override
    public RefiningRecipe getRecipe() {
        if (level == null) return null;
        return level.getRecipeManager().getRecipeFor(ModRecipes.Types.REFINING, this, level).orElse(null);
    }

    @Override
    protected int getProcessTime(RefiningRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<FluidStack> getFluidResults(RefiningRecipe recipe) {
        return recipe.getFluidResults(this);
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return ENERGY_PER_TICK;
    }

    @Override
    protected int getInputTanks() {
        return 1;
    }

    @Override
    protected int getOutputTanks() {
        return 4;
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;

        tryFillTank();
        tryFillFluidContainer();

        super.tick();
    }

    @Override
    protected void consumeIngredients(RefiningRecipe recipe) {
        // NO-OP
    }

    private void tryFillTank() {
        // Try fill feedstock tank with fluid containers
        ItemStack input = getItem(0);
        if (input.isEmpty()) return;

        FluidStack fluidStack = IFluidContainer.getBucketOrContainerFluid(input);
        if (canAcceptFluidContainer(input, fluidStack)) {
            this.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

            ItemStack containerItem = input.getContainerItem();
            input.shrink(1);

            ItemStack output = getItem(1);
            if (output.isEmpty()) {
                setItem(1, containerItem);
            } else {
                output.grow(1);
            }
        }
    }

    private boolean canAcceptFluidContainer(ItemStack input, FluidStack fluid) {
        ItemStack output = getItem(1);
        return !fluid.isEmpty()
                && this.isFluidValid(0, fluid)
                && this.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == 1000
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    private void tryFillFluidContainer() {
        // Fill empty fluid containers with output fluids
        ItemStack input = getItem(2);
        if (input.isEmpty()) return;

        FluidStack fluidInInput = IFluidContainer.getBucketOrContainerFluid(input);
        if (!fluidInInput.isEmpty()) return;

        for (int i = 1; i < 5; ++i) {
            FluidStack fluidInTank = getFluidInTank(i);
            if (fluidInTank.getAmount() >= 1000) {
                ItemStack filled = IFluidContainer.fillBucketOrFluidContainer(input, fluidInTank);
                if (!filled.isEmpty() && InventoryUtils.mergeItem(this, filled, 3)) {
                    tanks[i].drain(1000, FluidAction.EXECUTE);
                    input.shrink(1);
                    return;
                }
            }
        }
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
        return TextUtil.translate("container", "refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new RefineryContainer(id, player, this, this.fields);
    }
}
