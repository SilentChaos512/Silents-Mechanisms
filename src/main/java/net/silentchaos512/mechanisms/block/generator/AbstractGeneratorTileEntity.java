package net.silentchaos512.mechanisms.block.generator;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;

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
                    return burnTime;
                case 3:
                    return totalBurnTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    setEnergyStoredDirectly(value);
                    break;
                case 2:
                    burnTime = value;
                    break;
                case 3:
                    totalBurnTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    protected AbstractGeneratorTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract) {
        super(typeIn, inventorySize, maxEnergy, maxReceive, maxExtract);
    }

    @Override
    public void tick() {
//        if (world == null || world.isRemote) return;
//
//        if (isBurning()) {
//            // Currently burning fuel
//            --burnTime;
//            energy.createEnergy(ENERGY_CREATED_PER_TICK);
//
//            sendUpdate();
//        } else {
//            ItemStack fuel = getStackInSlot(0);
//            if (getEnergyStored() < getMaxEnergyStored() && isFuel(fuel)) {
//                // Not burning, and not at max energy
//                burnTime = getBurnTime(fuel);
//                if (isBurning()) {
//                    totalBurnTime = burnTime;
//
//                    // Consume fuel
//                    if (fuel.hasContainerItem()) {
//                        setInventorySlotContents(0, fuel.getContainerItem());
//                    } else if (!fuel.isEmpty()) {
//                        fuel.shrink(1);
//                        if (fuel.isEmpty()) {
//                            setInventorySlotContents(0, fuel.getContainerItem());
//                        }
//                    }
//                }
//
//                sendUpdate();
//            }
//        }
//
//        super.tick();
    }

    private void sendUpdate() {
//        if (world != null) {
//            BlockState oldState = world.getBlockState(pos);
//            BlockState newState = oldState.with(AbstractFurnaceBlock.LIT, isBurning());
//            if (oldState != newState) {
//                world.setBlockState(pos, newState, 3);
//                world.notifyBlockUpdate(pos, oldState, newState, 3);
//            }
//        }
    }
}
