package net.silentchaos512.mechanisms.api;

import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

@FunctionalInterface
public interface IWrenchable {
    ActionResultType onWrench(ItemUseContext context);
}
