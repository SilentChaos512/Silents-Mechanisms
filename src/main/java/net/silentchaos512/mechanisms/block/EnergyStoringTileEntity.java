package net.silentchaos512.mechanisms.block;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.mechanisms.capability.EnergyStorageImpl;
import net.silentchaos512.mechanisms.util.EnergyUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyStoringTileEntity extends TileEntity implements IEnergyHandler, ITickableTileEntity {
    private final EnergyStorageImpl energy;
    private final int maxReceive;
    private final int maxExtract;

    public EnergyStoringTileEntity(TileEntityType<?> tileEntityTypeIn, int capacity, int maxReceive, int maxExtract) {
        super(tileEntityTypeIn);
        this.energy = new EnergyStorageImpl(capacity, maxReceive, maxExtract, this);
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public EnergyStorageImpl getEnergyImpl() {
        return energy;
    }

    @Override
    public void tick() {
        EnergyUtils.trySendToNeighbors(world, pos, this, maxExtract);
    }

    @Override
    public void read(CompoundNBT tags) {
        super.read(tags);
        SyncVariable.Helper.readSyncVars(this, tags);
        readEnergy(tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
        writeEnergy(tags);
        return tags;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && cap == CapabilityEnergy.ENERGY) {
            return getEnergy(side).cast();
        }
        return super.getCapability(cap, side);
    }
}
