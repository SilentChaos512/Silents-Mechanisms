package net.silentchaos512.mechanisms.api.crafting.recipe.fluid;

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
    public static final FluidIngredient EMPTY = new FluidIngredient();

    @Nullable private final Tag<Fluid> tag;
    @Nullable private final Fluid fluid;
    private final int amount;

    public FluidIngredient(@Nonnull Tag<Fluid> tag) {
        this(tag, 1000);
    }

    public FluidIngredient(@Nonnull Tag<Fluid> tag, int amount) {
        this.tag = tag;
        this.fluid = null;
        this.amount = amount;
    }

    public FluidIngredient(@Nonnull Fluid fluid) {
        this(fluid, 1000);
    }

    public FluidIngredient(@Nonnull Fluid fluid, int amount) {
        this.fluid = fluid;
        this.tag = null;
        this.amount = amount;
    }

    private FluidIngredient() {
        this.tag = null;
        this.fluid = null;
        this.amount = 1000;
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

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean test(FluidStack stack) {
        return stack.getAmount() >= amount && testIgnoreAmount(stack);
    }

    /**
     * Test for a match without considering the amount of fluid in the st
     *
     * @param stack The fluid
     * @return True if the fluid matches the ingredient, ignoring amount
     */
    public boolean testIgnoreAmount(FluidStack stack) {
        return (tag != null && stack.getFluid().isIn(tag)) || (fluid != null && stack.getFluid() == fluid);
    }

    public static FluidIngredient deserialize(JsonObject json) {
        if (json.has("tag") && json.has("fluid")) {
            throw new JsonSyntaxException("Fluid ingredient should have 'tag' or 'fluid', not both");
        }

        int amount = JSONUtils.getInt(json, "amount", 1000);

        if (json.has("tag")) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(json, "tag"));
            return new FluidIngredient(new FluidTags.Wrapper(id), amount);
        }
        if (json.has("fluid")) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(json, "fluid"));
            Fluid fluid = Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(id));
            return new FluidIngredient(fluid, amount);
        }
        throw new JsonSyntaxException("Fluid ingredient should have either 'tag' or 'fluid'");
    }

    public static FluidIngredient read(PacketBuffer buffer) {
        boolean isTag = buffer.readBoolean();
        ResourceLocation id = buffer.readResourceLocation();
        return isTag
                ? new FluidIngredient(new FluidTags.Wrapper(id))
                : new FluidIngredient(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(id)));
    }

    public void write(PacketBuffer buffer) {
        boolean isTag = tag != null;
        buffer.writeBoolean(isTag);
        if (isTag)
            buffer.writeResourceLocation(tag.getId());
        else if (fluid != null)
            buffer.writeResourceLocation(Objects.requireNonNull(fluid.getRegistryName()));
        else
            buffer.writeResourceLocation(new ResourceLocation("null"));
    }
}
