package net.silentchaos512.mechanisms.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegisterEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.common.blocks.batterybox.BatteryBoxMenu;
import net.silentchaos512.mechanisms.common.blocks.generators.coalgenerator.CoalGeneratorMenu;

import java.util.HashMap;
import java.util.Map;

public final class ModMenus {
    public static final Map<ResourceLocation, MenuType<?>> ALL_MENUS = new HashMap<>();

    public static final MenuType<CoalGeneratorMenu> COAL_GENERATOR_MENU;
    public static final MenuType<BatteryBoxMenu> BATTERY_BOX;

    static {
        COAL_GENERATOR_MENU = register("coal_generator_menu", CoalGeneratorMenu::new);
        BATTERY_BOX = register("battery_box", BatteryBoxMenu::new);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> menu) {
        MenuType<T> menuType = new MenuType<>(menu);
        ALL_MENUS.put(SilentsMechanisms.location(name), menuType);
        return menuType;
    }

    public static void register(RegisterEvent.RegisterHelper<MenuType<?>> helper) {
        ALL_MENUS.forEach(helper::register);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType<T> menuType) {
        ALL_MENUS.put(SilentsMechanisms.location(name), menuType);
        return menuType;
    }
}
