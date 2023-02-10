package net.silentchaos512.mechanisms.compat.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.silentchaos512.lib.util.TextRenderUtils;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.compat.jei.BackgroundHelper;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.recipes.RackDryingRecipe;

public class RackDryingCategory extends BasicRecipeCategory<RackDryingRecipe> {
    public static final ResourceLocation RECIPE_TYPE_UID = SilentsMechanisms.location("rack_drying");

    private final IDrawableAnimated arrow;

    public RackDryingCategory(IGuiHelper helper) {
        super(helper,
                new RecipeType<>(RECIPE_TYPE_UID, RackDryingRecipe.class),
                Component.translatable("jei.silents_mechanisms.category.drying"),
                DryingRackBlock.ALL_RACKS,
                new BackgroundHelper(BackgroundHelper.makeGui("drying_category"), 114, 38));

        var arrowStatic = helper.createDrawable(guiLocation, 114, 0, 50, 16);
        this.arrow = helper.createAnimatedDrawable(arrowStatic, 40, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RackDryingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 9).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 88, 9).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(RackDryingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        arrow.draw(stack, 32, 9);
        TextRenderUtils.renderScaled(stack, super.font, Component.translatable("misc.silents_mechanisms.timeInSeconds", recipe.getDryingTime() / 20).getVisualOrderText(), 0, 26, 1f, 0xffffff, true);
    }

    @Override
    public void addRecipes(IRecipeRegistration registration, RecipeManager recipeManager) {
        registration.addRecipes(this.recipeType, recipeManager.getAllRecipesFor(RackDryingRecipe.RECIPE_TYPE));
    }
}
