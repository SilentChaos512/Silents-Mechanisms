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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FluidIngredient implements Predicate<FluidStack> {
    @Nullable private final Tag<Fluid> tag;
    @Nullable private final Fluid fluid;

    public FluidIngredient(@Nonnull Tag<Fluid> tag) {
        this.tag = tag;
        this.fluid = null;
    }

    public FluidIngredient(@Nonnull Fluid fluid) {
        this.fluid = fluid;
        this.tag = null;
    }

    @Nullable
    public Tag<Fluid> getTag() {
        return tag;
    }

    public List<FluidStack> getFluids() {
        if (tag != null) {
            return tag.getAllElements().stream().map(f -> new FluidStack(f, 1000)).collect(Collectors.toList());
        }
        if (fluid != null) {
            return Collections.singletonList(new FluidStack(fluid, 1000));
        }
        return Collections.emptyList();
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
