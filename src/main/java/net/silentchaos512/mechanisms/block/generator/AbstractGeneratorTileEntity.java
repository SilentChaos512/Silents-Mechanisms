package net.silentchaos512.mechanisms.block.generator;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;
import net.silentchaos512.utils.EnumUtils;

public abstract class AbstractGeneratorTileEntity extends AbstractMachineBaseTileEntity {
    protected int burnTime;
    protected int totalBurnTime;

    private final IIntArray fields = new IIntArray() {
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
                    return redstoneMode.ordinal();
                case 3:
                    return burnTime;
                case 4:
                    return totalBurnTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2:
                    redstoneMode = EnumUtils.byOrdinal(value, RedstoneMode.IGNORED);
                    break;
                case 3:
                    burnTime = value;
                    break;
                case 4:
                    totalBurnTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 5;
        }
    };

    protected AbstractGeneratorTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract) {
        super(typeIn, inventorySize, maxEnergy, maxReceive, maxExtract);
    }

    protected abstract boolean hasFuel();

    protected abstract void consumeFuel();

    protected abstract int getEnergyCreatedPerTick();

    protected abstract BlockState getActiveState();

    protected abstract BlockState getInactiveState();

    @Override
    public void tick() {
        if (canRun()) {
            if (burnTime <= 0 && hasFuel()) {
                consumeFuel();
                sendUpdate(getActiveState(), true);
            }

            if (burnTime > 0) {
                --burnTime;
                energy.createEnergy(getEnergyCreatedPerTick());
            } else {
                sendUpdate(getInactiveState(), false);
            }
        } else if (burnTime <= 0) {
            sendUpdate(getInactiveState(), false);
        }
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
}
