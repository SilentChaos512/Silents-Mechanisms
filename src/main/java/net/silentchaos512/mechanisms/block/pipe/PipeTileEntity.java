package net.silentchaos512.mechanisms.block.pipe;

import net.minecraft.block.BlockState;
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
        if (level == null) return "world is null";

        PipeNetwork net = PipeNetworkManager.get(level, worldPosition);
        return net != null ? net.toString() : "null";
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        return super.save(compound);
    }

    @Override
    public void setRemoved() {
        if (level != null) {
            PipeNetworkManager.invalidateNetwork(level, worldPosition);
        }
        super.setRemoved();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (level != null && !remove && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side != null) {
            LazyOptional<PipeNetwork> networkOptional = PipeNetworkManager.getLazy(level, worldPosition);
            if (networkOptional.isPresent()) {
                return networkOptional.orElseThrow(IllegalStateException::new).getConnection(worldPosition, side).getLazyOptional().cast();
            }
        }
        return LazyOptional.empty();
    }
}
