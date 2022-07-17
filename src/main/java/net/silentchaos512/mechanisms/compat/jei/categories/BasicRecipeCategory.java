package net.silentchaos512.mechanisms.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.silentchaos512.mechanisms.compat.jei.BackgroundHelper;

import java.util.List;

@SuppressWarnings("removal")
public abstract class BasicRecipeCategory<T> implements IRecipeCategory<T> {
    protected final RecipeType<T> recipeType;
    protected final Component title;
    protected final List<? extends ItemLike> catalystIcons;
    protected final ResourceLocation guiLocation;
    protected final Font font = Minecraft.getInstance().font;
    private final IDrawable icon;
    private final IDrawable background;


    public BasicRecipeCategory(IGuiHelper guiHelper, RecipeType<T> recipeType, Component title, List<? extends ItemLike> catalystIcons, BackgroundHelper backgroundHelper) {
        this.recipeType = recipeType;
        this.title = title;
        this.catalystIcons = catalystIcons;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, makeStack(catalystIcons.get(0)));
        this.background = backgroundHelper.createBackground(guiHelper);
        this.guiLocation = backgroundHelper.textureLocation();
    }

    public BasicRecipeCategory(IGuiHelper guiHelper, RecipeType<T> recipeType, Component title, ItemLike catalystIcon, BackgroundHelper backgroundHelper) {
        this(guiHelper, recipeType, title, List.of(catalystIcon), backgroundHelper);
    }

    protected final ItemStack makeStack(ItemLike item) {
        return new ItemStack(item);
    }

    @Override
    public final Component getTitle() {
        return this.title;
    }

    @Override
    public final IDrawable getBackground() {
        return this.background;
    }

    @Override
    public final IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public final ResourceLocation getUid() {
        return this.recipeType.getUid();
    }

    @Override
    public final Class<? extends T> getRecipeClass() {
        return this.recipeType.getRecipeClass();
    }

    public abstract void addRecipes(IRecipeRegistration registration, RecipeManager recipeManager);

    public void addRecipeCatalysts(IRecipeCatalystRegistration registration) {
        this.catalystIcons.forEach(catalystIcon -> registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, makeStack(catalystIcon), this.recipeType));
    }
}
