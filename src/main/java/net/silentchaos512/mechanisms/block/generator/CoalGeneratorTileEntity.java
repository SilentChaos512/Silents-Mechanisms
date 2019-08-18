package net.silentchaos512.mechanisms.block.generator;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.ForgeEventFactory;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.mechanisms.block.AbstractEnergyInventoryTileEntity;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.List;

public class CoalGeneratorTileEntity extends AbstractEnergyInventoryTileEntity {
    // Energy constants
    public static final int MAX_ENERGY = 10_000;
    public static final int MAX_SEND = 500;
    public static final int ENERGY_CREATED_PER_TICK = 60;

    @Getter
    @SyncVariable(name = "BurnTime")
    private int burnTime;
    @Getter
    @SyncVariable(name = "TotalBurnTime")
    private int totalBurnTime;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case 0:
                    // Energy lower bytes
                    return CoalGeneratorTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (CoalGeneratorTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
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

    public CoalGeneratorTileEntity() {
        super(ModTileEntities.coalGenerator, 1, MAX_ENERGY, 0, MAX_SEND);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        if (isBurning()) {
            // Currently burning fuel
            --burnTime;
            energy.createEnergy(ENERGY_CREATED_PER_TICK);

            sendUpdate();
        } else {
            ItemStack fuel = getStackInSlot(0);
            if (getEnergyStored() < getMaxEnergyStored() && isFuel(fuel)) {
                // Not burning, and not at max energy
                burnTime = getBurnTime(fuel);
                if (isBurning()) {
                    totalBurnTime = burnTime;

                    // Consume fuel
                    if (fuel.hasContainerItem()) {
                        setInventorySlotContents(0, fuel.getContainerItem());
                    } else if (!fuel.isEmpty()) {
                        fuel.shrink(1);
                        if (fuel.isEmpty()) {
                            setInventorySlotContents(0, fuel.getContainerItem());
                        }
                    }
                }

                sendUpdate();
            }
        }

        super.tick();
    }

    private void sendUpdate() {
        if (world != null) {
            BlockState oldState = world.getBlockState(pos);
            BlockState newState = oldState.with(AbstractFurnaceBlock.LIT, isBurning());
            if (oldState != newState) {
                world.setBlockState(pos, newState, 3);
                world.notifyBlockUpdate(pos, oldState, newState, 3);
            }
        }
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    static boolean isFuel(ItemStack stack) {
        return AbstractFurnaceTileEntity.isFuel(stack);
    }

    private static int getBurnTime(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        int ret = stack.getBurnTime();
        return ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? AbstractFurnaceTileEntity.getBurnTimes().getOrDefault(stack.getItem(), 0) : ret);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return isFuel(itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return stack.getItem() == Items.BUCKET;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "coal_generator");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new CoalGeneratorContainer(id, playerInventory, this, this.fields);
    }

    public List<String> getDebugText() {
        return ImmutableList.of(
                "burnTime = " + burnTime,
                "totalBurnTime = " + totalBurnTime,
                "energy = " + getEnergyStored() + " FE / " + getMaxEnergyStored() + " FE",
                "ENERGY_CREATED_PER_TICK = " + ENERGY_CREATED_PER_TICK,
                "MAX_SEND = " + MAX_SEND
        );
    }
}
