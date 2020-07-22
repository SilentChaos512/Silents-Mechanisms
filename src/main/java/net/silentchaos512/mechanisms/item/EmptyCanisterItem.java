package net.silentchaos512.mechanisms.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class EmptyCanisterItem extends CanisterItem {
    public EmptyCanisterItem(Properties properties) {
        super(properties);
    }

    @Override
    public FluidStack getFluid(ItemStack stack) {
        return FluidStack.EMPTY;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(new ItemStack(this));
        }
    }
}
