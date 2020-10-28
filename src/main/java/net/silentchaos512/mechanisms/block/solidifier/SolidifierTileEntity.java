package net.silentchaos512.mechanisms.block.solidifier;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.silentchaos512.mechanisms.api.IFluidContainer;
import net.silentchaos512.mechanisms.block.AbstractFluidMachineTileEntity;
import net.silentchaos512.mechanisms.crafting.recipe.SolidifyingRecipe;
import net.silentchaos512.mechanisms.init.ModRecipes;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.InventoryUtils;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

public class SolidifierTileEntity extends AbstractFluidMachineTileEntity<SolidifyingRecipe> {
    public static final int FIELDS_COUNT = 9;
    public static final int TANK_CAPACITY = 4000;
    public static final int ENERGY_PER_TICK = 50;

    public SolidifierTileEntity() {
        super(ModTileEntities.solidifier, 3, 1, TANK_CAPACITY, MachineTier.STANDARD);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        tryFillTank();

        super.tick();
    }

    @Override
    protected void consumeIngredients(SolidifyingRecipe recipe) {
        // NO-OP
    }

    private void tryFillTank() {
        // Try fill feedstock tank with fluid containers
        ItemStack input = getStackInSlot(0);
        if (input.isEmpty()) return;

        FluidStack fluidStack = IFluidContainer.getBucketOrContainerFluid(input);
        if (canAcceptFluidContainer(input, fluidStack)) {
            this.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

            ItemStack containerItem = input.getContainerItem();
            input.shrink(1);

            ItemStack output = getStackInSlot(1);
            if (output.isEmpty()) {
                setInventorySlotContents(1, containerItem);
            } else {
                output.grow(1);
            }
        }
    }

    private boolean canAcceptFluidContainer(ItemStack input, FluidStack fluid) {
        ItemStack output = getStackInSlot(1);
        return !fluid.isEmpty()
                && this.isFluidValid(0, fluid)
                && this.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == 1000
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return ENERGY_PER_TICK;
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[]{2};
    }

    @Override
    protected int getInputTanks() {
        return 1;
    }

    @Override
    protected int getOutputTanks() {
        return 0;
    }

    @Nullable
    @Override
    public SolidifyingRecipe getRecipe() {
        if (world == null) return null;
        return world.getRecipeManager().getRecipe(ModRecipes.Types.SOLIDIFYING, this, world).orElse(null);
    }

    @Override
    protected int getProcessTime(SolidifyingRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<ItemStack> getProcessResults(SolidifyingRecipe recipe) {
        return Collections.singletonList(recipe.getCraftingResult(this));
    }

    @Override
    protected Collection<FluidStack> getFluidResults(SolidifyingRecipe recipe) {
        return recipe.getFluidOutputs();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 0 && InventoryUtils.isFilledFluidContainer(stack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1 || index == 2;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "solidifier");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new SolidifierContainer(id, player, this, this.fields);
    }
}
