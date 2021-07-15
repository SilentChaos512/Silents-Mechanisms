package net.silentchaos512.mechanisms.block.mixer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.mechanisms.block.AbstractMachineBlock;
import net.silentchaos512.mechanisms.util.MachineTier;

import javax.annotation.Nullable;
import java.util.Random;

public class MixerBlock extends AbstractMachineBlock {

    private static final VoxelShape SHAPE = VoxelShapes.or(Block.box(1, 0, 1, 15, 2, 15), Block.box(1, 6, 1, 15, 8, 15), Block.box(2, 2, 2, 14, 6, 14), Block.box(2, 8, 2, 14, 16, 14));

    public MixerBlock() {
        super(MachineTier.STANDARD, Block.Properties.of(Material.METAL).strength(6, 20).sound(SoundType.METAL));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected void openContainer(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            player.openMenu((INamedContainerProvider) tileEntity);
        }
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new MixerTileEntity();
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        // TODO: Unique sound and particles? Copied from BlastFurnaceBlock.
        if (stateIn.getValue(LIT)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = stateIn.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
            double d6 = rand.nextDouble() * 9.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }
}
