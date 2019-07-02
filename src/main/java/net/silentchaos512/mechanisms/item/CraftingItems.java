package net.silentchaos512.mechanisms.item;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.utils.Lazy;
import net.silentchaos512.utils.TextUtils;

public enum CraftingItems implements IItemProvider {
    IRON_DUST,
    GOLD_DUST;

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
