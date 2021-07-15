package net.silentchaos512.mechanisms.block.pump;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.mechanisms.block.AbstractMachineBlock;
import net.silentchaos512.mechanisms.util.MachineTier;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class PumpBlock extends AbstractMachineBlock {

    private static final VoxelShape SHAPE = VoxelShapes.or(Block.box(1, 0, 1, 15, 15, 15));

    public PumpBlock() {
        super(MachineTier.STANDARD, Properties.of(Material.METAL).strength(6, 20).sound(SoundType.METAL));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new PumpTileEntity();
    }

    @Override
    protected void openContainer(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            player.openMenu((INamedContainerProvider) tileEntity);
        }
    }
}
