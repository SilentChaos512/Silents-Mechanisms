package net.silentchaos512.mechanisms.block.generator;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.utils.EnumUtils;

public abstract class AbstractGeneratorTileEntity extends AbstractMachineBaseTileEntity {
    public static final int FIELDS_COUNT = 7;

    protected int burnTime;
    protected int totalBurnTime;

    protected final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case 0:
                    // Energy lower bytes
                    return AbstractGeneratorTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (AbstractGeneratorTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return AbstractGeneratorTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (AbstractGeneratorTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return redstoneMode.ordinal();
                case 5:
                    return burnTime;
                case 6:
                    return totalBurnTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    redstoneMode = EnumUtils.byOrdinal(value, RedstoneMode.IGNORED);
                    break;
                case 5:
                    burnTime = value;
                    break;
                case 6:
                    totalBurnTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return FIELDS_COUNT;
        }
    };

    protected AbstractGeneratorTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract, MachineTier tier) {
        super(typeIn, inventorySize, maxEnergy, maxReceive, maxExtract, tier);
    }

    protected abstract boolean hasFuel();

    protected abstract void consumeFuel();

    protected abstract int getEnergyCreatedPerTick();

    protected BlockState getActiveState() {
        return getBlockState().with(AbstractFurnaceBlock.LIT, true);
    }

    protected BlockState getInactiveState() {
        return getBlockState().with(AbstractFurnaceBlock.LIT, false);
    };

    @Override
    public void tick() {
        if (canRun()) {
            if (burnTime <= 0 && hasFuel()) {
                consumeFuel();
                sendUpdate(getActiveState(), true);
            }
        }

        if (burnTime > 0) {
            --burnTime;
            energy.createEnergy(getEnergyCreatedPerTick());
        } else {
            sendUpdate(getInactiveState(), false);
        }

        super.tick();
    }

    protected boolean canRun() {
        return world != null
                && redstoneMode.shouldRun(world.isBlockPowered(pos))
                && getEnergyStored() < getMaxEnergyStored();
    }

    protected void sendUpdate(BlockState newState, boolean force) {
        if (world == null) return;
        BlockState oldState = world.getBlockState(pos);
        if (oldState != newState || force) {
            world.setBlockState(pos, newState, 3);
            world.notifyBlockUpdate(pos, oldState, newState, 3);
        }
    }

    @Override
    public void read(CompoundNBT tags) {
        super.read(tags);
        this.burnTime = tags.getInt("BurnTime");
        this.totalBurnTime = tags.getInt("TotalBurnTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        tags.putInt("BurnTime", this.burnTime);
        tags.putInt("TotalBurnTime", this.totalBurnTime);
        return super.write(tags);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        CompoundNBT tags = packet.getNbtCompound();
        this.burnTime = tags.getInt("BurnTime");
        this.totalBurnTime = tags.getInt("TotalBurnTime");
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("BurnTime", this.burnTime);
        tags.putInt("TotalBurnTime", this.totalBurnTime);
        return tags;
    }
}
