package net.silentchaos512.mechanisms.crafting.refining;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Predicate;

public class FluidIngredient implements Predicate<FluidStack> {
    private final Tag<Fluid> tag;
    private final Fluid fluid;

    public FluidIngredient(Tag<Fluid> tag) {
        this.tag = tag;
        this.fluid = null;
    }

    public FluidIngredient(Fluid fluid) {
        this.fluid = fluid;
        this.tag = null;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return (tag != null && fluidStack.getFluid().isIn(tag)) || (fluid != null && fluidStack.getFluid() == fluid);
    }

    public static FluidIngredient deserialize(JsonObject json) {
        if (json.has("tag") && json.has("fluid")) {
            throw new JsonSyntaxException("Fluid ingredient should have 'tag' or 'fluid', not both");
        }
        if (json.has("tag")) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(json, "tag"));
            return new FluidIngredient(new FluidTags.Wrapper(id));
        }
        if (json.has("fluid")) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(json, "fluid"));
            return new FluidIngredient(ForgeRegistries.FLUIDS.getValue(id));
        }
        throw new JsonSyntaxException("Fluid ingredient should have either 'tag' or 'fluid'");
    }

    public static FluidIngredient read(PacketBuffer buffer) {
        boolean isTag = buffer.readBoolean();
        ResourceLocation id = buffer.readResourceLocation();
        return isTag
                ? new FluidIngredient(new FluidTags.Wrapper(id))
                : new FluidIngredient(ForgeRegistries.FLUIDS.getValue(id));
    }

    public void write(PacketBuffer buffer) {
        boolean isTag = tag != null;
        buffer.writeBoolean(isTag);
        if (isTag)
            buffer.writeResourceLocation(tag.getId());
        else if (fluid != null)
            buffer.writeResourceLocation(Objects.requireNonNull(fluid.getRegistryName()));
        else
            buffer.writeResourceLocation(new ResourceLocation("null")); // Shouldn't happen
    }
}
