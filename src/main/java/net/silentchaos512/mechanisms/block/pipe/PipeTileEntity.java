package net.silentchaos512.mechanisms.block.pipe;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.silentchaos512.mechanisms.init.ModTileEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PipeTileEntity extends TileEntity {
    public PipeTileEntity() {
        super(ModTileEntities.pipe);
    }

    public String getPipeNetworkData() {
        if (world == null) return "world is null";

        PipeNetwork net = PipeNetworkManager.get(world, pos);
        return net != null ? net.toString() : "null";
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Override
    public void remove() {
        if (world != null) {
            PipeNetworkManager.invalidateNetwork(world, pos);
        }
        super.remove();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (world != null && !removed && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side != null) {
            LazyOptional<PipeNetwork> networkOptional = PipeNetworkManager.getLazy(world, pos);
            if (networkOptional.isPresent()) {
                return networkOptional.orElseThrow(IllegalStateException::new).getConnection(pos, side).getLazyOptional().cast();
            }
        }
        return LazyOptional.empty();
    }
}
