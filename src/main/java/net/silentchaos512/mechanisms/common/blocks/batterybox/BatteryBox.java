package net.silentchaos512.mechanisms.common.blocks.batterybox;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.mechanisms.common.blocks.abstracts.AbstractEntityBlock;
import net.silentchaos512.mechanisms.init.ModBlockEntities;
import net.silentchaos512.mechanisms.utls.CompoundTagUtils;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BatteryBox extends AbstractEntityBlock<BatteryBoxBlockEntity> {
    public static final VoxelShape SHAPE = box(0, 0, 0, 16, 13, 16);
    public static final IntegerProperty BATTERIES = IntegerProperty.create("batteries", 0, 6);

    public BatteryBox() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(6, 20).sound(SoundType.METAL), BlockTags.MINEABLE_WITH_PICKAXE);
        super.registerDefaultState(super.defaultBlockState().setValue(BATTERIES, 0));
    }

    @Override
    public @Nullable BatteryBoxBlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BatteryBoxBlockEntity(pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BATTERIES);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected BlockEntityType<BatteryBoxBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BATTERY_BOX;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof BatteryBoxBlockEntity batteryBox) {
            IEnergyStorage energy = batteryBox.getEnergyStorage();
            return 15 * energy.getEnergyStored() / energy.getMaxEnergyStored();
        }
        return 0;
    }

    @Override
    protected void loadTagToBlock(CompoundTag blockEntityTag, BatteryBoxBlockEntity blockEntity) {
        CompoundTagUtils.loadEnergyToBlockEntity(blockEntity, blockEntityTag);
    }
}
