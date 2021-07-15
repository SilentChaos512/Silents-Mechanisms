package net.silentchaos512.mechanisms.block.dryingrack;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class DryingRackBlock extends HorizontalBlock implements IWaterLoggable {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE_NORTH = Block.box(0, 12, 12, 16, 16, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 12, 0, 16, 16, 4);
    private static final VoxelShape SHAPE_WEST = Block.box(12, 12, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_EAST = Block.box(0, 12, 0, 4, 16, 16);

    public DryingRackBlock() {
        super(Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2f, 3f).sound(SoundType.WOOD));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DryingRackTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof DryingRackTileEntity) {
            return ((DryingRackTileEntity) tileEntity).interact(player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropContents(worldIn, pos, (IInventory)tileentity);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction facing = state.getValue(FACING);
        switch (facing) {
            case NORTH: return SHAPE_NORTH;
            case SOUTH: return SHAPE_SOUTH;
            case WEST: return SHAPE_WEST;
            case EAST: return SHAPE_EAST;
            default: return SHAPE_NORTH;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
