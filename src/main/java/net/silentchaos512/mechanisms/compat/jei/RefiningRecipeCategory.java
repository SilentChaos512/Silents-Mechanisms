package net.silentchaos512.mechanisms.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.silentchaos512.mechanisms.block.refinery.RefineryScreen;
import net.silentchaos512.mechanisms.block.refinery.RefineryTileEntity;
import net.silentchaos512.mechanisms.crafting.refining.RefiningRecipe;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.util.Constants;
import net.silentchaos512.mechanisms.util.TextUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RefiningRecipeCategory implements IRecipeCategory<RefiningRecipe> {
    private static final int GUI_START_X = 26;
    private static final int GUI_START_Y = 15;
    private static final int GUI_WIDTH = 131 - GUI_START_X;
    private static final int GUI_HEIGHT = 70 - GUI_START_Y;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public RefiningRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(RefineryScreen.TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.refinery));
        arrow = guiHelper.drawableBuilder(RefineryScreen.TEXTURE, 176, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        localizedName = TextUtil.translate("jei", "category.refining").getFormattedText();
    }

    @Override
    public ResourceLocation getUid() {
        return Constants.REFINING;
    }

    @Override
    public Class<? extends RefiningRecipe> getRecipeClass() {
        return RefiningRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(RefiningRecipe recipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.FLUID, Collections.singletonList(recipe.getInput().getFluids()));
        ingredients.setOutputLists(VanillaTypes.FLUID, recipe.getResults().stream().map(Collections::singletonList).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RefiningRecipe recipe, IIngredients ingredients) {
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        fluidStacks.init(0, true, 3, 3, 12, 50, RefineryTileEntity.TANK_CAPACITY, true, null);
        fluidStacks.init(1, false, 43, 3, 12, 50, RefineryTileEntity.TANK_CAPACITY, true, null);
        fluidStacks.init(2, false, 59, 3, 12, 50, RefineryTileEntity.TANK_CAPACITY, true, null);
        fluidStacks.init(3, false, 75, 3, 12, 50, RefineryTileEntity.TANK_CAPACITY, true, null);
        fluidStacks.init(4, false, 91, 3, 12, 50, RefineryTileEntity.TANK_CAPACITY, true, null);

        fluidStacks.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
        List<List<FluidStack>> outputs = ingredients.getOutputs(VanillaTypes.FLUID);
        for (int i = 0; i < outputs.size(); ++i) {
            fluidStacks.set(i + 1, outputs.get(i));
        }
    }

    @Override
    public void draw(RefiningRecipe recipe, double mouseX, double mouseY) {
        arrow.draw(43 - GUI_START_X, 35 - GUI_START_Y);
    }
}
