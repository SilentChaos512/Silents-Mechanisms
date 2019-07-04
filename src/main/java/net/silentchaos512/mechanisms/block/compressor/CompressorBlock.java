package net.silentchaos512.mechanisms.block.compressor;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CompressorBlock extends AbstractFurnaceBlock {
    public CompressorBlock() {
        super(Properties.create(Material.IRON).hardnessAndResistance(6, 20).sound(SoundType.METAL));
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof CompressorTileEntity) {
            player.openContainer((INamedContainerProvider) tileEntity);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new CompressorTileEntity();
    }
}
