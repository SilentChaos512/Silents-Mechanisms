package net.silentchaos512.mechanisms.block;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.mechanisms.capability.EnergyStorageImpl;
import net.silentchaos512.mechanisms.util.EnergyUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractEnergyInventoryTileEntity extends LockableSidedInventoryTileEntity implements IEnergyHandler, ITickableTileEntity {
    protected final EnergyStorageImpl energy;
    private final int maxExtract;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case 0:
                    // Energy lower bytes
                    return AbstractEnergyInventoryTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (AbstractEnergyInventoryTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return AbstractEnergyInventoryTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (AbstractEnergyInventoryTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            getEnergyImpl().setEnergyDirectly(value);
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    protected AbstractEnergyInventoryTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract) {
        super(typeIn, inventorySize);
        this.energy = new EnergyStorageImpl(maxEnergy, maxReceive, maxExtract, this);
        this.maxExtract = maxExtract;
    }

    @Override
    public EnergyStorageImpl getEnergyImpl() {
        return energy;
    }

    public IIntArray getFields() {
        return fields;
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;

        if (maxExtract > 0) {
            EnergyUtils.trySendToNeighbors(level, worldPosition, this, maxExtract);
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        SyncVariable.Helper.readSyncVars(this, tags);
        readEnergy(tags);
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
        writeEnergy(tags);
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        SyncVariable.Helper.readSyncVars(this, packet.getTag());
        readEnergy(packet.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
        writeEnergy(tags);
        return tags;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == CapabilityEnergy.ENERGY) {
            return getEnergy(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
    }
}
