package net.silentchaos512.mechanisms.init;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.recipes.RackDryingRecipe;

@Mod.EventBusSubscriber(modid = SilentsMechanisms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModRecipeSerializers {

    private ModRecipeSerializers() {}

    public static final RecipeSerializer<RackDryingRecipe> RACK_DRYING = new RackDryingRecipe.Serializer();

    @SubscribeEvent
    public static void onRecipeSerializerRegistration(RegistryEvent.Register<RecipeSerializer<?>> event) {
        IForgeRegistry<RecipeSerializer<?>> registration = event.getRegistry();
        registerRecipe(registration, RACK_DRYING, RackDryingRecipe.RECIPE_TYPE);
    }

    public static <T extends Recipe<?>> void registerRecipe(IForgeRegistry<RecipeSerializer<?>> registration, RecipeSerializer<T> recipeSerializer, RecipeType<T> recipeType) {
        registration.register(recipeSerializer);
        Registry.register(Registry.RECIPE_TYPE, SilentsMechanisms.loc(recipeType.toString()), recipeType);
    }
}
