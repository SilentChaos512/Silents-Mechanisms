package net.silentchaos512.mechanisms.block.dryingrack;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class DryingRackTileEntityRenderer extends TileEntityRenderer<DryingRackTileEntity> {
    @Override
    public void render(DryingRackTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        ItemStack stack = tileEntityIn.getItem();
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            Direction facing = getFacing(tileEntityIn);
            Direction opposite = facing.getOpposite();
            double posX = x + 0.5 + 0.375 * opposite.getXOffset();
            double posY = y + 0.375;
            double posZ = z + 0.5 + 0.375 * opposite.getZOffset();
            GlStateManager.translated(posX, posY, posZ);
            GlStateManager.rotated(facing.getHorizontalAngle(), 0, 1, 0);

            double scale = 1.0;
            GlStateManager.scaled(scale, scale, scale);

            //noinspection deprecation
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
    }

    private static Direction getFacing(DryingRackTileEntity tileEntity) {
        World world = tileEntity.getWorld();
        if (world != null) {
            BlockState state = world.getBlockState(tileEntity.getPos());
            return state.get(DryingRackBlock.FACING);
        }
        return Direction.NORTH;
    }
}
