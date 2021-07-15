package net.silentchaos512.mechanisms.item;

import net.minecraft.item.Rarity;
import net.silentchaos512.mechanisms.SilentMechanisms;

import net.minecraft.item.Item.Properties;

public class BatteryItem extends EnergyStoringItem {
    private static final int MAX_ENERGY = 500_000;
    private static final int MAX_TRANSFER = 500;

    public BatteryItem() {
        super(new Properties().tab(SilentMechanisms.ITEM_GROUP).stacksTo(1).rarity(Rarity.UNCOMMON), MAX_ENERGY, MAX_TRANSFER);
    }
}
