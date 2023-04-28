package net.silentchaos512.mechanisms.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.silentchaos512.mechanisms.common.abstracts.TickableBlockEntity;
import net.silentchaos512.mechanisms.common.blocks.abstracts.AbstractEntityBlock;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SpecialBlock<T extends BlockEntity & TickableBlockEntity> extends AbstractEntityBlock<T> {

    private final Supplier<BlockEntityType<T>> supplier;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public SpecialBlock(Properties properties, Supplier<BlockEntityType<T>> supplier) {
        super(properties, BlockTags.MINEABLE_WITH_PICKAXE);
        this.supplier = supplier;
        super.registerDefaultState(super.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public @Nullable T newBlockEntity(BlockPos pPos, BlockState pState) {
        return supplier.get().create(pPos, pState);
    }

    @Override
    protected BlockEntityType<T> getBlockEntityType() {
        return supplier.get();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
}
