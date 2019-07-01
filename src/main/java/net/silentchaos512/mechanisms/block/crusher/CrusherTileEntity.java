package net.silentchaos512.mechanisms.block.crusher;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.capability.EnergyStorageImpl;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CrusherTileEntity extends LockableSidedInventoryTileEntity implements ITickableTileEntity {
    // Energy constant
    public static final int MAX_ENERGY = 100_000;
    public static final int MAX_SEND_RECEIVE = 1_000;
    public static final int ENERGY_USED_PER_TICK = 100;

    // Inventory constants
    static final int INPUT_SLOT_COUNT = 1;
    static final int OUTPUT_SLOT_COUNT = 4;
    static final int INVENTORY_SIZE = INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT;
    private static final int[] SLOTS_INPUT = {0};
    private static final int[] SLOTS_OUTPUT = IntStream.range(INPUT_SLOT_COUNT, INVENTORY_SIZE).toArray();
    private static final int[] SLOTS_ALL = IntStream.range(0, INVENTORY_SIZE).toArray();

    @Getter
    @SyncVariable(name = "Progress")
    private int progress;
    @Getter
    @SyncVariable(name = "ProcessTime")
    private int processTime;

    private IEnergyStorage energy = new EnergyStorageImpl(MAX_ENERGY, MAX_SEND_RECEIVE, MAX_SEND_RECEIVE, 0);

    final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            return 0;
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int size() {
            return 0;
        }
    };

    public CrusherTileEntity() {
        super(ModTileEntities.crusher, INVENTORY_SIZE);
    }

    @Override
    public void tick() {
        // TODO

        // testing energy
        energy.receiveEnergy(1, false);
        if (energy.getEnergyStored() % 20 == 0) {
            SilentMechanisms.LOGGER.debug("energy: {}", energy.getEnergyStored());
            sendUpdate();
        }
    }

    private void sendUpdate() {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP)
            return SLOTS_INPUT;
        if (side == Direction.DOWN)
            return SLOTS_OUTPUT;
        return SLOTS_ALL;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index < INPUT_SLOT_COUNT;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index >= INPUT_SLOT_COUNT;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "crusher");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new CrusherContainer(id, playerInventory);
    }

    @Override
    public void read(CompoundNBT tags) {
        super.read(tags);
        SyncVariable.Helper.readSyncVars(this, tags);
        this.energy = new EnergyStorageImpl(MAX_ENERGY, MAX_SEND_RECEIVE, MAX_SEND_RECEIVE, tags.getInt("Energy"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
        tags.putInt("Energy", energy.getEnergyStored());
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        SyncVariable.Helper.readSyncVars(this, packet.getNbtCompound());
        if (energy instanceof EnergyStorageImpl) {
            ((EnergyStorageImpl) energy).setEnergyDirectly(packet.getNbtCompound().getInt("Energy"));
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
        tags.putInt("Energy", energy.getEnergyStored());
        return tags;
    }

    List<String> getDebugText() {
        List<String> list = new ArrayList<>();
        list.add("progress = " + progress);
        list.add("processTime = " + processTime);
        list.add("energy = " + energy.getEnergyStored() + " FE / " + energy.getMaxEnergyStored() + " FE");
        return list;
    }
}
