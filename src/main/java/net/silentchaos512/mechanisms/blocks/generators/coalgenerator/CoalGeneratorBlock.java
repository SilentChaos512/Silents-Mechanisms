package net.silentchaos512.mechanisms.blocks.generators.coalgenerator;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.silentchaos512.mechanisms.blocks.generators.AbstractGeneratorBlock;
import net.silentchaos512.mechanisms.init.ModBlockEntities;
import net.silentchaos512.mechanisms.utls.CompoundTagUtils;

public class CoalGeneratorBlock extends AbstractGeneratorBlock<CoalGeneratorBlockEntity> {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public CoalGeneratorBlock(Properties properties) {
        super(properties);
        super.registerDefaultState(defaultBlockState().setValue(LIT, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void loadTagToBlock(CompoundTag blockEntityTag, CoalGeneratorBlockEntity blockEntity) {
        CompoundTagUtils.loadEnergyToBlockEntity(blockEntity, blockEntityTag);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT, FACING);
    }

    @Override
    public  CoalGeneratorBlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CoalGeneratorBlockEntity(pPos, pState);
    }

    @Override
    protected BlockEntityType<CoalGeneratorBlockEntity> getBlockEntityType() {
        return ModBlockEntities.COAL_GENERATOR.get();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
}
