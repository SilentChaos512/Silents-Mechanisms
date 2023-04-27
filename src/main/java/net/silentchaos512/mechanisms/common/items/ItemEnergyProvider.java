package net.silentchaos512.mechanisms.common.items;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.silentchaos512.mechanisms.common.capability.energy.TagBasedEnergyStorage;
import net.silentchaos512.mechanisms.utls.ModUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemEnergyProvider implements ICapabilityProvider {
    private final TagBasedEnergyStorage energyStorage;
    private final LazyOptional<EnergyStorage> holder;

    public ItemEnergyProvider(ItemStack stack, int defaultCapacity) {
        this.energyStorage = ModUtils.getEnergyStorageForItem(stack.getOrCreateTag(), defaultCapacity);
        this.holder = LazyOptional.of(() -> this.energyStorage);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.ENERGY.orEmpty(cap, this.holder.cast()).cast();
    }
}
