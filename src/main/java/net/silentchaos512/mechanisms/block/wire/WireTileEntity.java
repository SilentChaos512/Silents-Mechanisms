package net.silentchaos512.mechanisms.block.wire;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.silentchaos512.mechanisms.init.ModTileEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WireTileEntity extends TileEntity implements ITickableTileEntity {
    public WireTileEntity() {
        super(ModTileEntities.wire);
    }

    @Override
    public void tick() {
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        // TODO: Probably need to cache the wire network, instead of constructing them constantly
        if (this.world != null && !this.removed && cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> WireNetwork.buildNetwork(this.world, this.pos)).cast();
        return LazyOptional.empty();
    }
}
