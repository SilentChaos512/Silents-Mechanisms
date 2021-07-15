package net.silentchaos512.mechanisms.block.batterybox;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.mechanisms.block.IEnergyHandler;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class BatteryBoxBlock extends Block {
    public static final IntegerProperty BATTERIES = IntegerProperty.create("batteries", 0, 6);

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 13, 16);

    public BatteryBoxBlock() {
        super(Properties.of(Material.METAL).strength(6, 20).sound(SoundType.METAL));
        this.registerDefaultState(this.getStateDefinition().any().setValue(BATTERIES, 0));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BatteryBoxTileEntity();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BATTERIES);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState();
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            this.interactWith(worldIn, pos, player);
        }
        return ActionResultType.SUCCESS;
    }

    public void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof BatteryBoxTileEntity) {
            player.openMenu((INamedContainerProvider) tileEntity);
        }
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BatteryBoxTileEntity) {
                ((BatteryBoxTileEntity) tileentity).setCustomName(stack.getHoverName());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BatteryBoxTileEntity) {
                InventoryHelper.dropContents(worldIn, pos, (BatteryBoxTileEntity)tileentity);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof IEnergyHandler) {
            IEnergyHandler te = (IEnergyHandler) tileEntity;
            return 15 * te.getEnergyStored() / te.getMaxEnergyStored();
        }
        return 0;
    }
}
