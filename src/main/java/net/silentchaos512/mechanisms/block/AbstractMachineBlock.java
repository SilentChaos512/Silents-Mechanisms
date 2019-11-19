package net.silentchaos512.mechanisms.block;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.mechanisms.util.MachineTier;

public abstract class AbstractMachineBlock extends AbstractFurnaceBlock {
    protected final MachineTier tier;

    public AbstractMachineBlock(MachineTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            player.openContainer((INamedContainerProvider) tileEntity);
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof AbstractMachineBaseTileEntity) {
            return calcRedstoneFromInventory((AbstractMachineBaseTileEntity) tileEntity);
        }
        return super.getComparatorInputOverride(blockState, worldIn, pos);
    }

    private static int calcRedstoneFromInventory(AbstractMachineBaseTileEntity inv) {
        // Copied from Container.calcRedstoneFromInventory
        int slotsFilled = 0;
        float fillRatio = 0.0F;

        for (int i = 0; i < inv.getSizeInventory() - inv.getMachineTier().getUpgradeSlots(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                fillRatio += (float) itemstack.getCount() / Math.min(inv.getInventoryStackLimit(), itemstack.getMaxStackSize());
                ++slotsFilled;
            }
        }

        fillRatio = fillRatio / (float) inv.getSizeInventory();
        return MathHelper.floor(fillRatio * 14.0F) + (slotsFilled > 0 ? 1 : 0);
    }
}
