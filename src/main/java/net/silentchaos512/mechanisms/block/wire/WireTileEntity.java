package net.silentchaos512.mechanisms.block.wire;

import net.minecraft.nbt.CompoundNBT;
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
    int energyStored;

    public WireTileEntity() {
        super(ModTileEntities.wire);
    }

    public String getWireNetworkData() {
        if (world == null) return "world is null";

        WireNetwork net = WireNetworkManager.get(world, pos);
        return net != null ? net.toString() : "null";
    }

    @Override
    public void tick() {
    }

    @Override
    public void read(CompoundNBT compound) {
        this.energyStored = compound.getInt("EnergyStored");
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("EnergyStored", energyStored);
        return super.write(compound);
    }

    @Override
    public void remove() {
        if (world != null) {
            WireNetworkManager.invalidateNetwork(world, pos);
        }
        super.remove();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (world != null && !removed && cap == CapabilityEnergy.ENERGY && side != null) {
            LazyOptional<WireNetwork> networkOptional = WireNetworkManager.getLazy(world, pos);
            if (networkOptional.isPresent()) {
                return networkOptional.orElseThrow(IllegalStateException::new).getConnection(pos, side).getLazyOptional().cast();
            }
        }
        return LazyOptional.empty();
    }
}
