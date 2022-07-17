package net.silentchaos512.mechanisms.blocks.dryingracks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import net.silentchaos512.mechanisms.blocks.abstracts.AbstractEntityBlock;
import net.silentchaos512.mechanisms.init.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class DryingRackBlock extends AbstractEntityBlock<DryingRackBlockEntity> {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE_NORTH = Block.box(0, 12, 12, 16, 16, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 12, 0, 16, 16, 4);
    private static final VoxelShape SHAPE_WEST = Block.box(12, 12, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_EAST = Block.box(0, 12, 0, 4, 16, 16);
    //Define the kind of wood that this rack is made from
    public final Block woodMaterial;

    public DryingRackBlock(Block woodMaterial) {
        super(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2f, 3f).sound(SoundType.WOOD), BlockTags.MINEABLE_WITH_AXE, Tags.Blocks.NEEDS_WOOD_TOOL);
        this.woodMaterial = woodMaterial;
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof DryingRackBlockEntity dryingRack) {
            return dryingRack.attemptInteract(pPlayer) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.facingBlockStateForPlacement(FACING, pContext);
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(FACING);
        return switch (facing) {
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable DryingRackBlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DryingRackBlockEntity(pPos, pState);
    }

    @Override
    protected BlockEntityType<DryingRackBlockEntity> getBlockEntityType() {
        return ModBlockEntities.DRYING_RACKS.get();
    }
}
