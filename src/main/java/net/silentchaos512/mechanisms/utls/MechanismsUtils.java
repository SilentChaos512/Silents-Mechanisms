package net.silentchaos512.mechanisms.utls;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class MechanismsUtils {
    private MechanismsUtils() {
        throw new IllegalStateException("Can't Initialize Utility Class");
    }

    public static <T extends Recipe<?>> RecipeType<T> makeRecipeType(String name) {
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return name;
            }
        };
    }
}
