package net.silentchaos512.mechanisms.api.crafting.recipe.fluid;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Just extends {@link IInventory} and {@link IFluidHandler}, to make {@link IFluidRecipe}
 * implementation a bit cleaner.
 */
public interface IFluidInventory extends IInventory, IFluidHandler {
}
