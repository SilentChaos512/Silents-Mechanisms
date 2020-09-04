package net.silentchaos512.mechanisms.api.crafting.recipe.fluid;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An {@code Ingredient}-equivalent for fluids. Can match fluids or fluid tags. Can also consider
 * fluid amount.
 */
public class FluidIngredient implements Predicate<FluidStack> {
    public static final FluidIngredient EMPTY = new FluidIngredient();

    @Nullable private final ITag.INamedTag<Fluid> tag;
    @Nullable private final Fluid fluid;
    private final int amount;

    public FluidIngredient(@Nonnull ITag.INamedTag<Fluid> tag) {
        this(tag, 1000);
    }

    public FluidIngredient(@Nonnull ITag.INamedTag<Fluid> tag, int amount) {
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
    public ITag<Fluid> getTag() {
        return tag;
    }

    /**
     * Get a list of all {@link FluidStack}s which match this ingredient. Used for JEI support.
     *
     * @return A list of matching fluids
     */
    public List<FluidStack> getFluids() {
        if (tag != null) {
            return tag.getAllElements().stream().map(f -> new FluidStack(f, this.amount)).collect(Collectors.toList());
        }
        if (fluid != null) {
            return Collections.singletonList(new FluidStack(fluid, this.amount));
        }
        return Collections.emptyList();
    }

    public int getAmount() {
        return amount;
    }

    /**
     * Test for a match. Also considers the fluid amount, use {@link #testIgnoreAmount(FluidStack)}
     * to ignore the amount.
     *
     * @param stack The fluid
     * @return True if the fluid matches the ingredient and has the same amount of fluid or more
     */
    @Override
    public boolean test(FluidStack stack) {
        return stack.getAmount() >= amount && testIgnoreAmount(stack);
    }

    /**
     * Test for a match without considering the amount of fluid in the stack
     *
     * @param stack The fluid
     * @return True if the fluid matches the ingredient, ignoring amount
     */
    public boolean testIgnoreAmount(FluidStack stack) {
        return (tag != null && stack.getFluid().isIn(tag)) || (fluid != null && stack.getFluid() == fluid);
    }

    /**
     * Deserialize a {@link FluidIngredient} from JSON.
     *
     * @param json The JSON object
     * @return A new FluidIngredient
     * @throws JsonSyntaxException If the JSON cannot be parsed
     */
    public static FluidIngredient deserialize(JsonObject json) {
        if (json.has("tag") && json.has("fluid")) {
            throw new JsonSyntaxException("Fluid ingredient should have 'tag' or 'fluid', not both");
        }

        int amount = JSONUtils.getInt(json, "amount", 1000);

        if (json.has("tag")) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(json, "tag"));
            return new FluidIngredient(FluidTags.makeWrapperTag(id.toString()), amount);
        }
        if (json.has("fluid")) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(json, "fluid"));
            Fluid fluid = Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(id));
            return new FluidIngredient(fluid, amount);
        }
        throw new JsonSyntaxException("Fluid ingredient should have either 'tag' or 'fluid'");
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();

        if (this.tag != null) {
            json.addProperty("tag", this.tag.getName().toString());
        } else if (this.fluid != null) {
            json.addProperty("fluid", NameUtils.from(this.fluid).toString());
        } else {
            throw new IllegalStateException("Fluid ingredient is missing both tag and fluid");
        }

        json.addProperty("amount", this.amount);

        return json;
    }

    /**
     * Reads a {@link FluidIngredient} from a packet buffer. Use with {@link #write(PacketBuffer)}.
     *
     * @param buffer The packet buffer
     * @return A new FluidIngredient
     */
    public static FluidIngredient read(PacketBuffer buffer) {
        boolean isTag = buffer.readBoolean();
        ResourceLocation id = buffer.readResourceLocation();
        int amount = buffer.readVarInt();
        return isTag
                ? new FluidIngredient(FluidTags.makeWrapperTag(id.toString()), amount)
                : new FluidIngredient(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(id)), amount);
    }

    /**
     * Writes the ingredient to a packet buffer. Use with {@link #read(PacketBuffer)}.
     *
     * @param buffer The packet buffer
     */
    public void write(PacketBuffer buffer) {
        boolean isTag = tag != null;
        buffer.writeBoolean(isTag);
        if (isTag)
            buffer.writeResourceLocation(tag.getName());
        else if (fluid != null)
            buffer.writeResourceLocation(Objects.requireNonNull(fluid.getRegistryName()));
        else
            buffer.writeResourceLocation(new ResourceLocation("null"));
        buffer.writeVarInt(this.amount);
    }
}
