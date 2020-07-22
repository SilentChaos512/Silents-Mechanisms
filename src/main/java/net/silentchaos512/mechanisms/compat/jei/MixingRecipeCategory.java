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
import net.minecraftforge.fluids.FluidStack;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.FluidIngredient;
import net.silentchaos512.mechanisms.block.mixer.MixerScreen;
import net.silentchaos512.mechanisms.block.mixer.MixerTileEntity;
import net.silentchaos512.mechanisms.crafting.recipe.MixingRecipe;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.item.CanisterItem;
import net.silentchaos512.mechanisms.util.Constants;
import net.silentchaos512.mechanisms.util.TextUtil;

import java.util.*;
import java.util.stream.Collectors;

public class MixingRecipeCategory implements IRecipeCategory<MixingRecipe> {
    private static final int GUI_START_X = 5;
    private static final int GUI_START_Y = 13;
    private static final int GUI_WIDTH = 148;
    private static final int GUI_HEIGHT = 65;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public MixingRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(MixerScreen.TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.MIXER));
        arrow = guiHelper.drawableBuilder(MixerScreen.TEXTURE, 176, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        localizedName = TextUtil.translate("jei", "category.mixing").getString();
    }

    @Override
    public ResourceLocation getUid() {
        return Constants.MIXING;
    }

    @Override
    public Class<? extends MixingRecipe> getRecipeClass() {
        return MixingRecipe.class;
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
    public void setIngredients(MixingRecipe recipe, IIngredients ingredients) {
        // Fluids (in tanks)
        ingredients.setInputLists(VanillaTypes.FLUID, recipe.getFluidIngredients().stream().map(FluidIngredient::getFluids).collect(Collectors.toList()));
        ingredients.setOutputLists(VanillaTypes.FLUID, recipe.getFluidOutputs().stream().map(Collections::singletonList).collect(Collectors.toList()));

        // Input fluid containers
        ImmutableList<ItemStack> emptyContainers = ImmutableList.of(new ItemStack(Items.BUCKET), new ItemStack(ModItems.EMPTY_CANISTER));
        List<ItemStack> feedstockContainers = new ArrayList<>();
        recipe.getFluidIngredients().stream()
                .flatMap(ingredient -> ingredient.getFluids().stream())
                .forEach(stack -> addFluidContainers(feedstockContainers, stack.getFluid()));
        ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(
                feedstockContainers,
                emptyContainers
        ));

        // Output fluid containers
        List<ItemStack> outputContainers = new ArrayList<>();
        recipe.getFluidOutputs().forEach(fluid -> addFluidContainers(outputContainers, fluid.getFluid()));
        ingredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(
                emptyContainers,
                outputContainers
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MixingRecipe recipe, IIngredients ingredients) {
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        final int capacity = MixerTileEntity.TANK_CAPACITY;
        fluidStacks.init(0, true, 24, 5, 12, 50, capacity, true, null);
        fluidStacks.init(1, true, 40, 5, 12, 50, capacity, true, null);
        fluidStacks.init(2, true, 56, 5, 12, 50, capacity, true, null);
        fluidStacks.init(3, true, 72, 5, 12, 50, capacity, true, null);
        fluidStacks.init(4, false, 112, 5, 12, 50, capacity, true, null);

        List<List<FluidStack>> inputs = ingredients.getInputs(VanillaTypes.FLUID);
        for (int i = 0; i < inputs.size(); ++i) {
            fluidStacks.set(i, inputs.get(i));
        }
        fluidStacks.set(4, ingredients.getOutputs(VanillaTypes.FLUID).get(0));

        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 2, 2);
        itemStacks.init(1, false, 2, 45);
        itemStacks.init(2, true, 128, 2);
        itemStacks.init(3, false, 128, 45);

        itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        itemStacks.set(2, ingredients.getInputs(VanillaTypes.ITEM).get(1));
        itemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        itemStacks.set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
    }

    @Override
    public void draw(MixingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 92 - GUI_START_X, 35 - GUI_START_Y);
    }

    private static void addFluidContainers(Collection<ItemStack> list, Fluid fluid) {
        ItemStack bucket = new ItemStack(fluid.getFilledBucket());
        if (!bucket.isEmpty()) {
            list.add(bucket);
        }
        list.add(CanisterItem.getStack(fluid));
    }
}
