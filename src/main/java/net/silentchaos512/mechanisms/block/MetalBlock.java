package net.silentchaos512.mechanisms.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class MetalBlock extends Block {
    public MetalBlock() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(4, 20)
                .sound(SoundType.METAL)
        );
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
        return true;
    }
}
