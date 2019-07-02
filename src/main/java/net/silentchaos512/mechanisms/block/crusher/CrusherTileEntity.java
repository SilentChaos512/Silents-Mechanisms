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
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.mechanisms.capability.EnergyStorageImpl;
import net.silentchaos512.mechanisms.crafting.recipe.CrushingRecipe;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
            switch (index) {
                case 0:
                    return progress;
                case 1:
                    return processTime;
                case 2:
                    return energy.getEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
                case 1:
                    processTime = value;
                    break;
                case 2:
                    if (energy instanceof EnergyStorageImpl) {
                        ((EnergyStorageImpl) energy).setEnergyDirectly(value);
                    }
                    break;
            }
        }

        @Override
        public int size() {
            return 3;
        }
    };

    public CrusherTileEntity() {
        super(ModTileEntities.crusher, INVENTORY_SIZE);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        CrushingRecipe recipe = world.getRecipeManager().getRecipe(CrushingRecipe.RECIPE_TYPE, this, world).orElse(null);
        if (recipe != null && hasRoomInOutput(recipe) && energy.getEnergyStored() >= ENERGY_USED_PER_TICK) {
            // Process
            processTime = recipe.getProcessTime();
            ++progress;
            energy.extractEnergy(ENERGY_USED_PER_TICK, false);

            if (progress > processTime) {
                // Create results
                createAndStoreResults(recipe);
                consumeIngredients(recipe);
                progress = 0;
            }

            sendUpdate();
        } else if (recipe == null) {
            setNeutralState();
        }

        // testing energy
        energy.receiveEnergy(100, false);
    }

    private void sendUpdate() {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    private void setNeutralState() {
        boolean update = false;
        if (progress > 0) {
            progress = 0;
            update = true;
        }
        if (update) {
            sendUpdate();
        }
    }

    private boolean hasRoomInOutput(CrushingRecipe recipe) {
        // Determine if all possible results could fit
        Set<ItemStack> results = recipe.getPossibleResults(this);
        int i = INPUT_SLOT_COUNT;
        for (ItemStack stack : results) {
            ItemStack output = getStackInSlot(i);
            if (!canItemsStack(stack, output)) {
                return false;
            }
            ++i;
        }
        return true;
    }

    private static boolean canItemsStack(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    private void createAndStoreResults(CrushingRecipe recipe) {
        List<ItemStack> results = recipe.getResults(this);
        int i = INPUT_SLOT_COUNT;
        for (ItemStack stack : results) {
            ItemStack output = getStackInSlot(i);
            if (output.isEmpty()) {
                setInventorySlotContents(i, stack);
            } else {
                output.setCount(output.getCount() + stack.getCount());
            }
            ++i;
        }
    }

    private void consumeIngredients(CrushingRecipe recipe) {
        decrStackSize(0, 1);
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
        return new CrusherContainer(id, playerInventory, this);
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
