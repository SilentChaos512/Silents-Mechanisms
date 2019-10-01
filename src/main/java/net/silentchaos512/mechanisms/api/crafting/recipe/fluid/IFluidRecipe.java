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

public interface IFluidRecipe<C extends IFluidInventory> extends IRecipe<C> {
    List<FluidStack> getFluidResults(C inv);

    List<FluidStack> getFluidOutputs();

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

    static FluidStack deserializeFluid(JsonObject json) {
        ResourceLocation fluidId = new ResourceLocation(JSONUtils.getString(json, "fluid"));
        int amount = JSONUtils.getInt(json, "amount", 1000);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid == null) {
            throw new JsonSyntaxException("Unknown fluid: " + fluidId);
        }
        return new FluidStack(fluid, amount);
    }

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

    static void writeFluid(PacketBuffer buffer, FluidStack stack) {
        buffer.writeResourceLocation(Objects.requireNonNull(stack.getFluid().getRegistryName()));
        buffer.writeVarInt(stack.getAmount());
    }
}
