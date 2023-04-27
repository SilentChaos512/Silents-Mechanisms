package net.silentchaos512.mechanisms.recipes;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;

import java.util.List;

public class SMRecipeCategories {
    public static final RecipeBookType DRYING = RecipeBookType.create("drying");
    public static final RecipeBookCategories DRYING_RACKS = RecipeBookCategories.create("drying", DryingRackBlock.ALL_RACKS.stream().map(ItemStack::new).toArray(ItemStack[]::new));

    public static void registerEvent(RegisterRecipeBookCategoriesEvent event) {
        event.registerRecipeCategoryFinder(RackDryingRecipe.RECIPE_TYPE, r -> DRYING_RACKS);
        event.registerBookCategories(DRYING, List.of(DRYING_RACKS));
    }

    //This method is used to initialize static constants and add the event listener at the same time
    public static void registerBus(IEventBus bus) {
        bus.addListener(SMRecipeCategories::registerEvent);
    }
}
