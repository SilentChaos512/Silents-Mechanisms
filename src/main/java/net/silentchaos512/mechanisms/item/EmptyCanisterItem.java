package net.silentchaos512.mechanisms.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import net.minecraft.item.Item.Properties;

public class EmptyCanisterItem extends CanisterItem {
    public EmptyCanisterItem(Properties properties) {
        super(properties);
    }

    @Override
    public FluidStack getFluid(ItemStack stack) {
        return FluidStack.EMPTY;
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            items.add(new ItemStack(this));
        }
    }
}
