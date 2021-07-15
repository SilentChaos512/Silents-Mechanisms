package net.silentchaos512.mechanisms.block.compressor;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.block.AbstractMachineTileEntity;
import net.silentchaos512.mechanisms.crafting.recipe.CompressingRecipe;
import net.silentchaos512.mechanisms.init.ModRecipes;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CompressorTileEntity extends AbstractMachineTileEntity<CompressingRecipe> {
    // Energy constant
    public static final int MAX_ENERGY = 50_000;
    public static final int MAX_RECEIVE = 500;
    public static final int ENERGY_USED_PER_TICK = 30;

    // Inventory constants
    private static final int[] SLOTS_INPUT = {0};
    private static final int[] SLOTS_OUTPUT = {1};
    private static final int[] SLOTS_ALL = {0, 1};

    public CompressorTileEntity() {
        super(ModTileEntities.compressor, 2, MachineTier.STANDARD);
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

    @Nullable
    @Override
    protected CompressingRecipe getRecipe() {
        if (level == null) return null;
        return level.getRecipeManager().getRecipeFor(ModRecipes.Types.COMPRESSING, this, level).orElse(null);
    }

    @Override
    protected int getProcessTime(CompressingRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<ItemStack> getProcessResults(CompressingRecipe recipe) {
        return Collections.singleton(recipe.assemble(this));
    }

    @Override
    protected void consumeIngredients(CompressingRecipe recipe) {
        removeItem(0, recipe.getIngredientCount());
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS_ALL;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index == 0;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "compressor");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new CompressorContainer(id, playerInventory, this, this.fields);
    }

    List<String> getDebugText() {
        return ImmutableList.of(
                "progress = " + this.fields.get(0),
                "processTime = " + this.fields.get(1),
                "energy = " + this.fields.get(2) + " FE / " + getMaxEnergyStored() + " FE",
                "ENERGY_USED_PER_TICK = " + ENERGY_USED_PER_TICK,
                "MAX_RECEIVE = " + MAX_RECEIVE
        );
    }
}
