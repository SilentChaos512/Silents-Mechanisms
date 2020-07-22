package net.silentchaos512.mechanisms.client;

import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.mechanisms.init.ModItems;
import net.silentchaos512.mechanisms.item.EnergyStoringItem;

public final class ModModelProperties {
    private ModModelProperties() {}

    public static void register(FMLClientSetupEvent event) {
        register(ModItems.BATTERY, EnergyStoringItem.CHARGE, (stack, world, entity) ->
                EnergyStoringItem.getChargeRatio(stack));
    }

    private static void register(IItemProvider item, ResourceLocation property, IItemPropertyGetter getter) {
        ItemModelsProperties.func_239418_a_(item.asItem(), property, getter);
    }
}
