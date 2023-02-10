package net.silentchaos512.mechanisms.utls;

import net.minecraft.nbt.CompoundTag;
import net.silentchaos512.mechanisms.common.abstracts.BaseEnergyBlockEntity;

public final class CompoundTagUtils {
    private CompoundTagUtils() {
    }

    public static CompoundTag getBlockEntityTag(CompoundTag tag) {
        return tag.contains("BlockEntityTag", 10) ? tag.getCompound("BlockEntityTag") : tag;
    }

    public static boolean hasBlockEntityTag(CompoundTag tag) {
        return tag.contains("BlockEntityTag", 10);
    }

    public static void loadEnergyToBlockEntity(BaseEnergyBlockEntity energyBlockEntity, CompoundTag tag) {
        if (tag.contains("EnergyStored")) {
            int tagEnergy = tag.getInt("EnergyStored");
            energyBlockEntity.energyStorage.setEnergy(tagEnergy);
        }
    }
}
