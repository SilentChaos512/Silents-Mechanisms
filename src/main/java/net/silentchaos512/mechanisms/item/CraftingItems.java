package net.silentchaos512.mechanisms.item;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.utils.Lazy;
import net.silentchaos512.utils.TextUtils;

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
    NICKEL_INGOT;

    private final Lazy<Item> item;

    CraftingItems() {
        this.item = Lazy.of(() -> new Item(new Item.Properties().group(SilentMechanisms.ITEM_GROUP)));
    }

    public String getName() {
        return TextUtils.lower(name());
    }

    @Override
    public Item asItem() {
        return item.get();
    }
}
