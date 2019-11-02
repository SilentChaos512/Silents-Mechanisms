package net.silentchaos512.mechanisms.item;

import net.minecraft.item.Rarity;
import net.silentchaos512.mechanisms.SilentMechanisms;

public class BatteryItem extends EnergyStoringItem {
    private static final int MAX_ENERGY = 500_000;
    private static final int MAX_TRANSFER = 500;

    public BatteryItem() {
        super(new Properties().group(SilentMechanisms.ITEM_GROUP).maxStackSize(1).rarity(Rarity.UNCOMMON), MAX_ENERGY, MAX_TRANSFER);
    }
}
