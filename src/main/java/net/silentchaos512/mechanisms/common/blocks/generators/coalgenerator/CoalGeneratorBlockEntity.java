package net.silentchaos512.mechanisms.common.blocks.generators.coalgenerator;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.silentchaos512.mechanisms.common.abstracts.BaseMachineBlockEntity;
import net.silentchaos512.mechanisms.common.abstracts.TickableBlockEntity;
import net.silentchaos512.mechanisms.common.capability.energy.GeneratorEnergyStorage;
import net.silentchaos512.mechanisms.data.tag.ModItemTags;
import net.silentchaos512.mechanisms.init.ModBlockEntities;
import net.silentchaos512.mechanisms.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class CoalGeneratorBlockEntity extends BaseMachineBlockEntity implements TickableBlockEntity {
    public static final int ENERGY_CAPACITY = 15000; //old value: 10000
    public static final int ENERGY_PER_TICK = 80; // old value : 60
    public static final int MAX_EXTRACT = 700; // old value 500
    private int burnTime;
    private int burnDuration;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> energyStorage.getEnergyStored();
                case 1 -> energyStorage.getMaxEnergyStored();
                case 2 -> burnTime;
                case 3 -> burnDuration;
                default -> throw new IllegalStateException("Unexpected value: " + pIndex);
            };
        }

        @Override
        public void set(int pIndex, int pValue) {

        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public CoalGeneratorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.COAL_GENERATOR, pWorldPosition, pBlockState, new GeneratorEnergyStorage(ENERGY_CAPACITY, ENERGY_PER_TICK, MAX_EXTRACT), 1);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        super.sendEnergyToBlocks(level, pos);

        if (this.burnTime <= 0) {
            if (isValidFuelItem(items.get(0))) {
                this.burnDuration = ForgeHooks.getBurnTime(items.get(0), null);
                this.burnTime = this.burnDuration;
                this.items.get(0).shrink(1);
                state = state.setValue(CoalGeneratorBlock.LIT, true);
            } else state = state.setValue(CoalGeneratorBlock.LIT, false);
        } else {
            super.getEnergyStorage().generateEnergy(ENERGY_PER_TICK);
            this.burnTime--;
        }

        level.setBlock(pos, state, 3);
        setChanged(level, pos, state);
    }

    private boolean isValidFuelItem(ItemStack item) {
        return !item.isEmpty() && item.is(ModItemTags.COAL_GENERATOR_FUELS) && AbstractFurnaceBlockEntity.isFuel(item);
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.COAL_GENERATOR.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CoalGeneratorMenu(pContainerId, pPlayerInventory, this, data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("BurnTime", this.burnTime);
        pTag.putInt("BurnDuration", this.burnDuration);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.burnTime = pTag.getInt("BurnTime");
        this.burnDuration = pTag.getInt("BurnDuration");
    }
}
