package net.silentchaos512.mechanisms.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.silentchaos512.mechanisms.JsonNames;
import net.silentchaos512.mechanisms.MechanismsUtils;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.blocks.dryingracks.DryingRackBlockEntity;
import net.silentchaos512.mechanisms.init.ModRecipeSerializers;

public class RackDryingRecipe implements Recipe<DryingRackBlockEntity> {
    public static final RecipeType<RackDryingRecipe> RECIPE_TYPE = MechanismsUtils.makeRecipeType("rack_drying");
    private final int dryingTime;
    private final Ingredient input;
    private final ItemStack output;
    private final ResourceLocation recipeId;

    public RackDryingRecipe(ResourceLocation recipeId, int dryingTime, Ingredient input, ItemStack output) {
        this.dryingTime = dryingTime;
        this.input = input;
        this.output = output;
        this.recipeId = recipeId;
    }

    public final int getDryingTime() {
        return this.dryingTime;
    }

    @Override
    public boolean matches(DryingRackBlockEntity pContainer, Level pLevel) {
        return this.input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(DryingRackBlockEntity pContainer) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.RACK_DRYING;
    }

    @Override
    public RecipeType<?> getType() {
        return RECIPE_TYPE;
    }

    public static final class Serializer extends SimpleRecipeSerializer<RackDryingRecipe> {
        private static final ResourceLocation ID = SilentsMechanisms.loc("rack_drying");

        @Override
        public RackDryingRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            int dryingTime = GsonHelper.getAsInt(json, JsonNames.DURATION);
            Ingredient ingredient = Ingredient.fromJson(json.get(JsonNames.INGREDIENT));
            ItemStack output = super.getItemStack(json);
            return new RackDryingRecipe(pRecipeId, dryingTime, ingredient, output);
        }

        @Override
        public RackDryingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int dryingTime = pBuffer.readInt();
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack output = pBuffer.readItem();
            return new RackDryingRecipe(pRecipeId, dryingTime, ingredient, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, RackDryingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.dryingTime);
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.output);
        }

        @Override
        protected ResourceLocation getSerializerId() {
            return ID;
        }
    }
}
