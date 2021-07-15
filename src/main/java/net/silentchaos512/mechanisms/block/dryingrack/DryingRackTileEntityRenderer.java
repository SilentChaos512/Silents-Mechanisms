package net.silentchaos512.mechanisms.block.dryingrack;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.World;

public class DryingRackTileEntityRenderer extends TileEntityRenderer<DryingRackTileEntity> {
    public DryingRackTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DryingRackTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack stack = tileEntityIn.getItem();
        if (!stack.isEmpty()) {
            matrixStackIn.pushPose();
            RenderSystem.enableBlend();
            Direction facing = getFacing(tileEntityIn);
            Direction opposite = facing.getOpposite();
            double posX = 0.5 + 0.375 * opposite.getStepX();
            double posY = 0.425;
            double posZ = 0.5 + 0.375 * opposite.getStepZ();
            matrixStackIn.translate(posX, posY, posZ);
            matrixStackIn.mulPose(new Quaternion(0, 180 - facing.toYRot(), 0, true));

            float scale = 0.75f;
            matrixStackIn.scale(scale, scale, scale);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.popPose();
        }
    }

    private static Direction getFacing(DryingRackTileEntity tileEntity) {
        World world = tileEntity.getLevel();
        if (world != null) {
            BlockState state = world.getBlockState(tileEntity.getBlockPos());
            return state.getValue(DryingRackBlock.FACING);
        }
        return Direction.NORTH;
    }
}
