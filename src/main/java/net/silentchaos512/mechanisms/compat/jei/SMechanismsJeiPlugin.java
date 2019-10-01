package net.silentchaos512.mechanisms.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.alloysmelter.AlloySmelterContainer;
import net.silentchaos512.mechanisms.block.alloysmelter.AlloySmelterScreen;
import net.silentchaos512.mechanisms.block.compressor.CompressorContainer;
import net.silentchaos512.mechanisms.block.compressor.CompressorScreen;
import net.silentchaos512.mechanisms.block.crusher.CrusherContainer;
import net.silentchaos512.mechanisms.block.crusher.CrusherScreen;
import net.silentchaos512.mechanisms.block.electricfurnace.ElectricFurnaceContainer;
import net.silentchaos512.mechanisms.block.electricfurnace.ElectricFurnaceScreen;
import net.silentchaos512.mechanisms.block.refinery.RefineryScreen;
import net.silentchaos512.mechanisms.crafting.recipe.RefiningRecipe;
import net.silentchaos512.mechanisms.crafting.recipe.AlloySmeltingRecipe;
import net.silentchaos512.mechanisms.crafting.recipe.CompressingRecipe;
import net.silentchaos512.mechanisms.crafting.recipe.CrushingRecipe;
import net.silentchaos512.mechanisms.crafting.recipe.DryingRecipe;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.util.Constants;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class SMechanismsJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = SilentMechanisms.getId("plugin/main");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new AlloySmeltingRecipeCategoryJei(guiHelper));
        registration.addRecipeCategories(new CompressingRecipeCategoryJei(guiHelper));
        registration.addRecipeCategories(new CrushingRecipeCategoryJei(guiHelper));
        registration.addRecipeCategories(new DryingRecipeCategoryJei(guiHelper));
        registration.addRecipeCategories(new RefiningRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(getRecipesOfType(AlloySmeltingRecipe.RECIPE_TYPE), Constants.ALLOY_SMELTING);
        registration.addRecipes(getRecipesOfType(CompressingRecipe.RECIPE_TYPE), Constants.COMPRESSING);
        registration.addRecipes(getRecipesOfType(CrushingRecipe.RECIPE_TYPE), Constants.CRUSHING);
        registration.addRecipes(getRecipesOfType(DryingRecipe.RECIPE_TYPE), Constants.DRYING);
        registration.addRecipes(getRecipesOfType(RefiningRecipe.RECIPE_TYPE), Constants.REFINING);
    }

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> recipeType) {
        return Minecraft.getInstance().world.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getType() == recipeType)
                .collect(Collectors.toList());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlloySmelterScreen.class, 90, 32, 28, 23, Constants.ALLOY_SMELTING);
        registration.addRecipeClickArea(CompressorScreen.class, 78, 32, 28, 23, Constants.COMPRESSING);
        registration.addRecipeClickArea(CrusherScreen.class, 45, 32, 28, 23, Constants.CRUSHING);
        registration.addRecipeClickArea(ElectricFurnaceScreen.class, 78, 32, 28, 23,
                VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeClickArea(RefineryScreen.class, 43, 31, 24, 23, Constants.REFINING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(AlloySmelterContainer.class, Constants.ALLOY_SMELTING, 0, 4, 5, 36);
        registration.addRecipeTransferHandler(CompressorContainer.class, Constants.COMPRESSING, 0, 1, 2, 36);
        registration.addRecipeTransferHandler(CrusherContainer.class, Constants.CRUSHING, 0, 1, 5, 36);
        registration.addRecipeTransferHandler(ElectricFurnaceContainer.class, Constants.REFINING, 0, 1, 2, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.alloySmelter), Constants.ALLOY_SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.basicAlloySmelter), Constants.ALLOY_SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.compressor), Constants.COMPRESSING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.crusher), Constants.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.basicCrusher), Constants.CRUSHING);
        ModBlocks.DRYING_RACKS.forEach(block -> registration.addRecipeCatalyst(new ItemStack(block), Constants.DRYING));
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.electricFurnace),
                VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.refinery), Constants.REFINING);
    }
}
