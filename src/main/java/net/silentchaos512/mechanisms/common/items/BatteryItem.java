package net.silentchaos512.mechanisms.common.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import org.jetbrains.annotations.Nullable;

public class BatteryItem extends Item {
    public static final int DEFAULT_CAPACITY = 250_000;
    public BatteryItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemEnergyProvider(stack, DEFAULT_CAPACITY);
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }
}
