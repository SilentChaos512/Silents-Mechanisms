package net.silentchaos512.mechanisms.item;

import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.utils.Lazy;
import net.silentchaos512.utils.TextUtils;

import javax.annotation.Nullable;

public enum CraftingItems implements IItemProvider {
    REDSTONE_ALLOY_INGOT,
    REDSTONE_ALLOY_NUGGET,
    REDSTONE_ALLOY_DUST,
    REFINED_IRON_INGOT,
    COMPRESSED_IRON_INGOT,
    CIRCUIT_BOARD,
    IRON_CHUNKS,
    IRON_DUST,
    GOLD_CHUNKS,
    GOLD_DUST,
    COPPER_CHUNKS,
    COPPER_DUST,
    COPPER_INGOT,
    TIN_CHUNKS,
    TIN_DUST,
    TIN_INGOT,
    SILVER_CHUNKS,
    SILVER_DUST,
    SILVER_INGOT,
    LEAD_CHUNKS,
    LEAD_DUST,
    LEAD_INGOT,
    NICKEL_CHUNKS,
    NICKEL_DUST,
    NICKEL_INGOT,
    BRONZE_INGOT,
    BRONZE_DUST,
    INVAR_INGOT,
    INVAR_DUST,
    ZOMBIE_LEATHER,
    BEEF_JERKY(Foods.COOKED_BEEF),
    PORK_JERKY(Foods.COOKED_PORKCHOP),
    CHICKEN_JERKY(Foods.COOKED_CHICKEN),
    MUTTON_JERKY(Foods.COOKED_MUTTON),
    RABBIT_JERKY(Foods.COOKED_RABBIT),
    COD_JERKY(Foods.COOKED_COD),
    SALMON_JERKY(Foods.SALMON),
    ;

    private final Lazy<Item> item;

    CraftingItems() {
        this(null);
    }

    CraftingItems(@Nullable Food food) {
        this.item = Lazy.of(() -> new Item(createProperties(food)));
    }

    private static Item.Properties createProperties(@Nullable Food food) {
        Item.Properties result = new Item.Properties().group(SilentMechanisms.ITEM_GROUP);
        if (food != null) {
            result.food(food);
        }
        return result;
    }

    public String getName() {
        return TextUtils.lower(name());
    }

    @Override
    public Item asItem() {
        return item.get();
    }
}
