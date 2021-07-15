package net.silentchaos512.mechanisms.block.alloysmelter;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.block.AbstractMachineTileEntity;
import net.silentchaos512.mechanisms.crafting.recipe.AlloySmeltingRecipe;
import net.silentchaos512.mechanisms.init.MachineType;
import net.silentchaos512.mechanisms.init.ModRecipes;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

public class AlloySmelterTileEntity extends AbstractMachineTileEntity<AlloySmeltingRecipe> {
    // Energy constant
    public static final int MAX_ENERGY = 50_000;
    public static final int MAX_RECEIVE = 500;
    public static final int ENERGY_USED_PER_TICK = 30;

    // Inventory constants
    static final int INPUT_SLOT_COUNT = 4;
    private static final int[] SLOTS_INPUT = IntStream.range(0, INPUT_SLOT_COUNT).toArray();
    private static final int[] SLOTS_OUTPUT = {INPUT_SLOT_COUNT};
    private static final int[] SLOTS_ALL = IntStream.range(0, INPUT_SLOT_COUNT + 1).toArray();

    public AlloySmelterTileEntity() {
        this(MachineTier.STANDARD);
    }

    public AlloySmelterTileEntity(MachineTier tier) {
        super(MachineType.ALLOY_SMELTER.getTileEntityType(tier), SLOTS_ALL.length, tier);
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
    protected AlloySmeltingRecipe getRecipe() {
        if (level == null) return null;
        return level.getRecipeManager().getRecipeFor(ModRecipes.Types.ALLOY_SMELTING, this, level).orElse(null);
    }

    @Override
    protected int getProcessTime(AlloySmeltingRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<ItemStack> getProcessResults(AlloySmeltingRecipe recipe) {
        return Collections.singleton(recipe.assemble(this));
    }

    @Override
    protected void consumeIngredients(AlloySmeltingRecipe recipe) {
        recipe.consumeIngredients(this);
    }

    @Override
    public int getInputSlotCount() {
        return INPUT_SLOT_COUNT;
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS_ALL;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index < INPUT_SLOT_COUNT;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == INPUT_SLOT_COUNT;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "alloy_smelter");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new AlloySmelterContainer(id, playerInventory, this, this.fields);
    }

    public static class Basic extends AlloySmelterTileEntity {
        public Basic() {
            super(MachineTier.BASIC);
        }
    }
}
