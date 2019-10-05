package net.silentchaos512.mechanisms.api.crafting.recipe.fluid;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentMechanisms;

import java.util.List;
import java.util.Objects;

/**
 * An {@link IRecipe} which can use fluids as ingredients or results. Can also handle items if
 * needed. Also see {@link IFluidInventory} and {@link FluidIngredient}.
 * <p>
 * All the fluid-related input/output methods return a list. If these do not apply to the recipe
 * type, you should return an empty list.
 *
 * @param <C> The inventory type
 */
public interface IFluidRecipe<C extends IFluidInventory> extends IRecipe<C> {
    /**
     * Get the fluids which result from this recipe. Similar to {@link
     * #getCraftingResult(IFluidInventory)}.
     *
     * @param inv The inventory
     * @return A list of fluids produced
     */
    List<FluidStack> getFluidResults(C inv);

    /**
     * Get the fluids which result from this recipe, ignoring the inventory. Similar to {@link
     * #getRecipeOutput()}, needed for JEI support.
     *
     * @return A list of fluids produced
     */
    List<FluidStack> getFluidOutputs();

    /**
     * Get the fluid ingredients. Similar to {@link #getIngredients()}.
     *
     * @return A list of fluid ingredients which represent the inputs.
     */
    List<FluidIngredient> getFluidIngredients();

    @Override
    default ItemStack getCraftingResult(C inv) {
        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canFit(int width, int height) {
        return true;
    }

    /**
     * Deserialize a {@link FluidStack} from JSON, including a fluid amount.
     *
     * @param json The JSON object
     * @return A FluidStack
     * @throws JsonSyntaxException If the object cannot be parsed or the fluid does not exist.
     */
    static FluidStack deserializeFluid(JsonObject json) {
        ResourceLocation fluidId = new ResourceLocation(JSONUtils.getString(json, "fluid"));
        int amount = JSONUtils.getInt(json, "amount", 1000);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid == null) {
            throw new JsonSyntaxException("Unknown fluid: " + fluidId);
        }
        return new FluidStack(fluid, amount);
    }

    /**
     * Reads a {@link FluidStack} from a packet buffer. Use with {@link #writeFluid(PacketBuffer,
     * FluidStack)}.
     *
     * @param buffer The packet buffer
     * @return A new FluidStack
     */
    static FluidStack readFluid(PacketBuffer buffer) {
        ResourceLocation fluidId = buffer.readResourceLocation();
        int amount = buffer.readVarInt();
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid != null) {
            return new FluidStack(fluid, amount);
        } else {
            SilentMechanisms.LOGGER.error("Unknown fluid: {}", fluidId);
            return FluidStack.EMPTY;
        }
    }

    /**
     * Writes a {@link FluidStack} to a packet buffer. Use with {@link #readFluid(PacketBuffer)}.
     *
     * @param buffer The packet buffer
     * @param stack A new FluidStack
     */
    static void writeFluid(PacketBuffer buffer, FluidStack stack) {
        buffer.writeResourceLocation(Objects.requireNonNull(stack.getFluid().getRegistryName()));
        buffer.writeVarInt(stack.getAmount());
    }
}
