package net.silentchaos512.mechanisms.compat.jei;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.mechanisms.block.infuser.InfuserScreen;
import net.silentchaos512.mechanisms.block.infuser.InfuserTileEntity;
import net.silentchaos512.mechanisms.crafting.recipe.InfusingRecipe;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.item.CanisterItem;
import net.silentchaos512.mechanisms.util.Constants;
import net.silentchaos512.mechanisms.util.TextUtil;

import java.util.*;
import java.util.stream.Collectors;

public class InfusingRecipeCategory implements IRecipeCategory<InfusingRecipe> {
    private static final int GUI_START_X = 7;
    private static final int GUI_START_Y = 15;
    private static final int GUI_WIDTH = 136 - GUI_START_X;
    private static final int GUI_HEIGHT = 75 - GUI_START_Y;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public InfusingRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(InfuserScreen.TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.INFUSER));
        arrow = guiHelper.drawableBuilder(InfuserScreen.TEXTURE, 176, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        localizedName = TextUtil.translate("jei", "category.infusing").getString();
    }

    @Override
    public ResourceLocation getUid() {
        return Constants.INFUSING;
    }

    @Override
    public Class<? extends InfusingRecipe> getRecipeClass() {
        return InfusingRecipe.class;
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
    public void setIngredients(InfusingRecipe recipe, IIngredients ingredients) {
        // Fluids (in tanks)
        ingredients.setInputLists(VanillaTypes.FLUID, Collections.singletonList(recipe.getFluidIngredient().getFluids()));
        ingredients.setOutputLists(VanillaTypes.FLUID, recipe.getFluidOutputs().stream().map(Collections::singletonList).collect(Collectors.toList()));

        // Input fluid containers and recipe ingredient
        ImmutableList<ItemStack> emptyContainers = ImmutableList.of(new ItemStack(Items.BUCKET), new ItemStack(ModItems.EMPTY_CANISTER));
        List<ItemStack> feedstockContainers = new ArrayList<>();
        recipe.getFluidIngredient().getFluids().forEach(fluid -> addFluidContainers(feedstockContainers, fluid.getFluid()));
        ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(
                feedstockContainers,
                Arrays.asList(recipe.getIngredient().getItems())
        ));

        // Output fluid containers and recipe result
        ingredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(
                emptyContainers,
                Collections.singletonList(recipe.getResultItem())
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InfusingRecipe recipe, IIngredients ingredients) {
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        final int capacity = InfuserTileEntity.TANK_CAPACITY;
        fluidStacks.init(0, true, 25, 5, 12, 50, capacity, true, null);

        fluidStacks.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));

        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 8 - GUI_START_X, 8 - GUI_START_Y);
        itemStacks.init(1, false, 8 - GUI_START_X, 59 - GUI_START_Y);
        itemStacks.init(2, true, 54 - GUI_START_X, 35 - GUI_START_Y);
        itemStacks.init(3, false, 116 - GUI_START_X, 35 - GUI_START_Y);

        itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        itemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        itemStacks.set(2, ingredients.getInputs(VanillaTypes.ITEM).get(1));
        itemStacks.set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
    }

    @Override
    public void draw(InfusingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 79 - GUI_START_X, 35 - GUI_START_Y);
    }

    private static void addFluidContainers(Collection<ItemStack> list, Fluid fluid) {
        ItemStack bucket = new ItemStack(fluid.getBucket());
        if (!bucket.isEmpty()) {
            list.add(bucket);
        }
        list.add(CanisterItem.getStack(fluid));
    }
}
