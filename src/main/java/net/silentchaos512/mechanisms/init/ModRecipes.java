package net.silentchaos512.mechanisms.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.recipes.RackDryingRecipe;

import java.util.HashMap;
import java.util.HashSet;

@Mod.EventBusSubscriber(modid = SilentsMechanisms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModRecipes {

    public static final HashSet<RecipeType<?>> ALL_RECIPE_TYPES = new HashSet<>();
    public static final HashMap<ResourceLocation, RecipeSerializer<?>> ALL_SERIALIZERS = new HashMap<>();
    public static final RecipeSerializer<RackDryingRecipe> RACK_DRYING = new RackDryingRecipe.Serializer();

    private ModRecipes() {
    }

    @SubscribeEvent
    public static void onRecipeSerializerRegistration(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper -> ALL_SERIALIZERS.forEach(helper::register));
        event.register(ForgeRegistries.Keys.RECIPE_TYPES, helper -> ALL_RECIPE_TYPES.forEach(recipeType -> helper.register(new ResourceLocation(recipeType.toString()), recipeType)));
    }
}
