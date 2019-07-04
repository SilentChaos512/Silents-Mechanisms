package net.silentchaos512.mechanisms.block.batterybox;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.silentchaos512.mechanisms.block.AbstractEnergyInventoryTileEntity;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

public class BatteryBoxTileEntity extends AbstractEnergyInventoryTileEntity {
    public static final int MAX_ENERGY = 500_000;
    public static final int MAX_RECEIVE = 1_000;
    public static final int MAX_SEND = 1_000;

    static final int INVENTORY_SIZE = 6;
    private static final int[] SLOTS = IntStream.range(0, INVENTORY_SIZE).toArray();

    public BatteryBoxTileEntity() {
        super(ModTileEntities.batteryBox, 6, MAX_ENERGY, MAX_RECEIVE, MAX_SEND);
    }

    @Override
    public void tick() {
        super.tick();

        if (world != null && !world.isRemote && world.getGameTime() % 50 == 0) {
            int batteryCount = 0;
            for (int i = 0; i < getSizeInventory(); ++i) {
                if (!getStackInSlot(i).isEmpty()) {
                    ++batteryCount;
                }
            }

            int currentCount = world.getBlockState(pos).get(BatteryBoxBlock.BATTERIES);
            if (batteryCount != currentCount) {
                world.setBlockState(pos, world.getBlockState(pos).with(BatteryBoxBlock.BATTERIES, batteryCount), 3);
            }
        }
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return itemStackIn.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "battery_box");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new BatteryBoxContainer(id, playerInventory, this, this.getFields());
    }

    public List<String> getDebugText() {
        return ImmutableList.of(
                "energy = " + getEnergyStored() + " FE / " + getMaxEnergyStored() + " FE",
                "MAX_RECEIVE = " + MAX_RECEIVE,
                "MAX_SEND = " + MAX_SEND
        );
    }
}
