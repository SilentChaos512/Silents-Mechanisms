package net.silentchaos512.mechanisms.crafting.refining;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentMechanisms;

import java.util.List;
import java.util.Objects;

public class RefiningRecipe {
    private final ResourceLocation recipeId;
    private int processTime;
    private FluidIngredient input;
    private final List<FluidStack> outputs = NonNullList.create();

    public RefiningRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public ResourceLocation getId() {
        return recipeId;
    }

    public int getProcessTime() {
        return processTime;
    }

    public boolean matches(IFluidTank inputTank) {
        return matches(inputTank.getFluid());
    }

    public boolean matches(FluidStack fluid) {
        return this.input.test(fluid);
    }

    public FluidIngredient getInput() {
        return input;
    }

    public List<FluidStack> getResults() {
        List<FluidStack> results = NonNullList.create();
        outputs.forEach(s -> results.add(s.copy()));
        return results;
    }

    public static RefiningRecipe deserialize(ResourceLocation id, JsonObject json) {
        RefiningRecipe recipe = new RefiningRecipe(id);
        recipe.processTime = JSONUtils.getInt(json, "process_time");
        recipe.input = FluidIngredient.deserialize(json.getAsJsonObject("input"));
        for (JsonElement je : JSONUtils.getJsonArray(json, "outputs")) {
            FluidStack stack = deserializeFluid(je.getAsJsonObject());
            if (!stack.isEmpty()) {
                recipe.outputs.add(stack);
            }
        }
        return recipe;
    }

    public static RefiningRecipe read(PacketBuffer buffer) {
        RefiningRecipe recipe = new RefiningRecipe(buffer.readResourceLocation());
        recipe.processTime = buffer.readVarInt();
        recipe.input = FluidIngredient.read(buffer);
        int count = buffer.readByte();
        for (int i = 0; i < count; ++i) {
            ResourceLocation fluidId = buffer.readResourceLocation();
            int amount = buffer.readVarInt();
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
            if (fluid != null) {
                recipe.outputs.add(new FluidStack(fluid, amount));
            } else {
                SilentMechanisms.LOGGER.error("Unknown fluid: {}", fluidId);
            }
        }
        return recipe;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeResourceLocation(this.recipeId);
        buffer.writeVarInt(this.processTime);
        this.input.write(buffer);
        buffer.writeByte(this.outputs.size());
        this.outputs.forEach(s -> {
            buffer.writeResourceLocation(Objects.requireNonNull(s.getFluid().getRegistryName()));
            buffer.writeVarInt(s.getAmount());
        });
    }

    private static FluidStack deserializeFluid(JsonObject json) {
        ResourceLocation fluidId = new ResourceLocation(JSONUtils.getString(json, "fluid"));
        int amount = JSONUtils.getInt(json, "amount", 1000);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid == null) {
            throw new JsonSyntaxException("Unknown fluid: " + fluidId);
        }
        return new FluidStack(fluid, amount);
    }
}
