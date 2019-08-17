package net.silentchaos512.mechanisms.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.lib.util.TextRenderUtils;
import net.silentchaos512.mechanisms.block.compressor.CompressorScreen;
import net.silentchaos512.mechanisms.crafting.recipe.DryingRecipe;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.util.Constants;
import net.silentchaos512.mechanisms.util.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DryingRecipeCategoryJei implements IRecipeCategory<DryingRecipe> {
    private static final int GUI_START_X = 55;
    private static final int GUI_START_Y = 30;
    private static final int GUI_WIDTH = 137 - GUI_START_X;
    private static final int GUI_HEIGHT = 56 - GUI_START_Y;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public DryingRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(CompressorScreen.TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.DRYING_RACKS.get(0)));
        arrow = guiHelper.drawableBuilder(CompressorScreen.TEXTURE, 176, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        localizedName = TextUtil.translate("jei", "category.drying").getFormattedText();
    }

    @Override
    public ResourceLocation getUid() {
        return Constants.DRYING;
    }

    @Override
    public Class<? extends DryingRecipe> getRecipeClass() {
        return DryingRecipe.class;
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
    public void setIngredients(DryingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(recipe.getIngredient()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DryingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 55 - GUI_START_X, 34 - GUI_START_Y);
        itemStacks.init(1, false, 115 - GUI_START_X, 34 - GUI_START_Y);

        itemStacks.set(0, new ArrayList<>(Arrays.asList(recipe.getIngredient().getMatchingStacks())));
        itemStacks.set(1, recipe.getRecipeOutput());
    }

    @Override
    public void draw(DryingRecipe recipe, double mouseX, double mouseY) {
        arrow.draw(79 - GUI_START_X, 35 - GUI_START_Y);
        FontRenderer font = Minecraft.getInstance().fontRenderer;
        ITextComponent text = TextUtil.translate("misc", "timeInSeconds", recipe.getProcessTime() / 20);
        TextRenderUtils.renderScaled(font, text.getFormattedText(), 24, 20, 0.67f, 0xFFFFFF, true);
    }
}
