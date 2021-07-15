package net.silentchaos512.mechanisms.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.FluidIngredient;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.IFluidInventory;
import net.silentchaos512.mechanisms.api.crafting.recipe.fluid.IFluidRecipe;
import net.silentchaos512.mechanisms.block.infuser.InfuserTileEntity;
import net.silentchaos512.mechanisms.init.ModRecipes;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class InfusingRecipe implements IFluidRecipe<IFluidInventory> {
    private final ResourceLocation recipeId;
    private final  int processTime;
    private final Ingredient ingredient;
    private final FluidIngredient fluid;
    private final ItemStack result;

    public InfusingRecipe(ResourceLocation recipeId, int processTime, Ingredient ingredient, FluidIngredient fluid, ItemStack result) {
        this.recipeId = recipeId;
        this.processTime = processTime;
        this.ingredient = ingredient;
        this.fluid = fluid;
        this.result = result;
    }

    public int getProcessTime() {
        return processTime;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public FluidIngredient getFluidIngredient() {
        return this.fluid;
    }

    @Override
    public List<FluidStack> getFluidResults(IFluidInventory inv) {
        return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return Collections.emptyList();
    }

    @Override
    public List<FluidIngredient> getFluidIngredients() {
        return Collections.singletonList(this.fluid);
    }

    @Override
    public boolean matches(IFluidInventory inv, World worldIn) {
        FluidStack fluidInTank = inv.getFluidInTank(0);
        ItemStack input = inv.getItem(InfuserTileEntity.SLOT_ITEM_IN);
        return this.fluid.test(fluidInTank) && this.ingredient.test(input);
    }

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.INFUSING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.Types.INFUSING;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfusingRecipe> {
        @Override
        public InfusingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            int processTime = JSONUtils.getAsInt(json, "process_time");
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            FluidIngredient fluid = FluidIngredient.deserialize(json.getAsJsonObject("fluid"));
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new InfusingRecipe(recipeId, processTime, ingredient, fluid, result);
        }

        @Nullable
        @Override
        public InfusingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            int processTime = buffer.readVarInt();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            FluidIngredient fluid = FluidIngredient.read(buffer);
            ItemStack result = buffer.readItem();
            return new InfusingRecipe(recipeId, processTime, ingredient, fluid, result);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, InfusingRecipe recipe) {
            buffer.writeVarInt(recipe.processTime);
            recipe.ingredient.toNetwork(buffer);
            recipe.fluid.write(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
