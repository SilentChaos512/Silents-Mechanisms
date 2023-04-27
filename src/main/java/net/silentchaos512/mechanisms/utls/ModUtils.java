package net.silentchaos512.mechanisms.utls;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.common.capability.energy.TagBasedEnergyStorage;
import net.silentchaos512.mechanisms.init.ModRecipes;

public class ModUtils {
    private ModUtils() {
        throw new IllegalStateException("Can't Initialize Utility Class");
    }

    public static <T extends Recipe<?>> RecipeType<T> makeRecipeType(String name) {
        RecipeType<T> recipeType = RecipeType.simple(SilentsMechanisms.location(name));
        ModRecipes.ALL_RECIPE_TYPES.add(recipeType);
        return recipeType;
    }

    public static <T> ResourceLocation getRegistryName(IForgeRegistry<T> registry, T object) {
        return registry.getKey(object);
    }

    public static TagBasedEnergyStorage getEnergyStorageForItem(CompoundTag tag, int capacity) {
        if (tag.contains("energy", Tag.TAG_INT) && tag.contains("capacity", Tag.TAG_INT)) {
            int capacityTag = tag.getInt("capacity");
            return new TagBasedEnergyStorage(capacityTag, tag);
        }

        tag.putInt("capacity", capacity);
        tag.putInt("energy", 0);
        return new TagBasedEnergyStorage(capacity, tag);
    }

    private static final RandomSource RANDOM_SOURCE = RandomSource.create();

    public static RandomSource getRandomSource() {
        return RANDOM_SOURCE;
    }
}
