package net.silentchaos512.mechanisms.init;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.abstracts.BaseMenuContainer;
import net.silentchaos512.mechanisms.blocks.generators.coalgenerator.CoalGeneratorMenu;

import java.util.HashSet;
import java.util.Set;

public final class ModMenus {
    public static final Set<MenuType<?>> ALL_MENUS = new HashSet<>();

    public static final MenuType<CoalGeneratorMenu> COAL_GENERATOR_MENU;

    static {
        COAL_GENERATOR_MENU = register("coal_generator_menu", CoalGeneratorMenu::new);
    }

    static <T extends BaseMenuContainer> MenuType<T> register(String name, MenuType.MenuSupplier<T> menu) {
        MenuType<T> menuType = new MenuType<>(menu);
        ALL_MENUS.add(menuType.setRegistryName(SilentsMechanisms.loc(name)));
        return menuType;
    }
}
