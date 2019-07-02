package net.silentchaos512.mechanisms.block.wire;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class WireBlock extends SixWayBlock implements ITileEntityProvider {
    public WireBlock(Properties properties) {
        super(0.125f, properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.FALSE).with(EAST, Boolean.FALSE)
                .with(SOUTH, Boolean.FALSE).with(WEST, Boolean.FALSE).with(UP, Boolean.FALSE).with(DOWN, Boolean.FALSE));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new WireTileEntity();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.makeConnections(context.getWorld(), context.getPos());
    }

    public BlockState makeConnections(IBlockReader worldIn, BlockPos pos) {
        return this.getDefaultState()
                .with(DOWN, canConnectTo(worldIn, pos.down()))
                .with(UP, canConnectTo(worldIn, pos.up()))
                .with(NORTH, canConnectTo(worldIn, pos.north()))
                .with(EAST, canConnectTo(worldIn, pos.east()))
                .with(SOUTH, canConnectTo(worldIn, pos.south()))
                .with(WEST, canConnectTo(worldIn, pos.west()));
    }

    private boolean canConnectTo(IBlockReader worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        return tileEntity != null && tileEntity.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), canConnectTo(worldIn, facingPos));
    }
}
