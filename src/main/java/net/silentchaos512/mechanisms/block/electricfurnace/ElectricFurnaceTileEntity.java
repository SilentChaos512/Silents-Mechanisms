package net.silentchaos512.mechanisms.block.electricfurnace;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.block.AbstractMachineTileEntity;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ElectricFurnaceTileEntity extends AbstractMachineTileEntity<AbstractCookingRecipe> {
    // Energy constant
    public static final int MAX_ENERGY = 50_000;
    public static final int MAX_RECEIVE = 500;
    public static final int ENERGY_USED_PER_TICK = 30;

    // Inventory constants
    private static final int INVENTORY_SIZE = 2;
    private static final int[] SLOTS_INPUT = {0};
    private static final int[] SLOTS_OUTPUT = {1};
    private static final int[] SLOTS_ALL = {0, 1};

    public ElectricFurnaceTileEntity() {
        super(ModTileEntities.electricFurnace, INVENTORY_SIZE, MachineTier.STANDARD);
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return ENERGY_USED_PER_TICK;
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    protected int[] getOutputSlots() {
        return SLOTS_OUTPUT;
    }

    @Override
    @Nullable
    protected AbstractCookingRecipe getRecipe() {
        if (world == null) return null;

        RecipeManager recipeManager = world.getRecipeManager();
        Optional<BlastingRecipe> optional = recipeManager.getRecipe(IRecipeType.BLASTING, this, world);
        if (optional.isPresent()) return optional.get();

        Optional<FurnaceRecipe> optional1 = recipeManager.getRecipe(IRecipeType.SMELTING, this, world);
        return optional1.orElse(null);
    }

    @Override
    protected int getProcessTime(AbstractCookingRecipe recipe) {
        return recipe.getCookTime();
    }

    @Override
    protected Collection<ItemStack> getProcessResults(AbstractCookingRecipe recipe) {
        return Collections.singleton(recipe.getCraftingResult(this));
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS_ALL;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "electric_furnace");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new ElectricFurnaceContainer(id, playerInventory, this, this.fields);
    }

    List<String> getDebugText() {
        return ImmutableList.of(
                "progress = " + progress,
                "processTime = " + processTime,
                "energy = " + getEnergyStored() + " FE / " + getMaxEnergyStored() + " FE",
                "ENERGY_USED_PER_TICK = " + ENERGY_USED_PER_TICK,
                "MAX_RECEIVE = " + MAX_RECEIVE
        );
    }
}
