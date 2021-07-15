package net.silentchaos512.mechanisms.block.infuser;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.silentchaos512.mechanisms.api.IFluidContainer;
import net.silentchaos512.mechanisms.block.AbstractFluidMachineTileEntity;
import net.silentchaos512.mechanisms.crafting.recipe.InfusingRecipe;
import net.silentchaos512.mechanisms.init.ModRecipes;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.InventoryUtils;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class InfuserTileEntity extends AbstractFluidMachineTileEntity<InfusingRecipe> {
    public static final int FIELDS_COUNT = 9;
    public static final int TANK_CAPACITY = 4000;
    public static final int ENERGY_PER_TICK = 50;

    public static final int SLOT_FLUID_CONTAINER_IN = 0;
    public static final int SLOT_ITEM_IN = 2;
    public static final int SLOT_FLUID_CONTAINER_OUT = 1;
    public static final int SLOT_ITEM_OUT = 3;

    public InfuserTileEntity() {
        super(ModTileEntities.infuser, 4, 1, TANK_CAPACITY, MachineTier.STANDARD);
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;

        tryFillTank();

        super.tick();
    }

    @Override
    protected void consumeIngredients(InfusingRecipe recipe) {
        removeItem(SLOT_ITEM_IN, 1);
    }

    private void tryFillTank() {
        // Try fill feedstock tank with fluid containers
        ItemStack input = getItem(0);
        if (input.isEmpty()) return;

        FluidStack fluidStack = IFluidContainer.getBucketOrContainerFluid(input);
        if (canAcceptFluidContainer(input, fluidStack)) {
            this.fill(fluidStack, FluidAction.EXECUTE);

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
                && this.fill(fluid, FluidAction.SIMULATE) == 1000
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return ENERGY_PER_TICK;
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[]{SLOT_ITEM_OUT};
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
    public InfusingRecipe getRecipe() {
        if (level == null) return null;
        return level.getRecipeManager().getRecipeFor(ModRecipes.Types.INFUSING, this, level).orElse(null);
    }

    @Override
    protected int getProcessTime(InfusingRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<ItemStack> getProcessResults(InfusingRecipe recipe) {
        return Collections.singletonList(recipe.assemble(this));
    }

    @Override
    protected Collection<FluidStack> getFluidResults(InfusingRecipe recipe) {
        return recipe.getFluidOutputs();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return (index == SLOT_FLUID_CONTAINER_IN && InventoryUtils.isFilledFluidContainer(stack)) || index == SLOT_ITEM_IN;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_FLUID_CONTAINER_OUT || index == SLOT_ITEM_OUT;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "infuser");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new InfuserContainer(id, player, this, this.fields);
    }
}
