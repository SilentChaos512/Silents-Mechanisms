package net.silentchaos512.mechanisms.blocks.dryingracks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    public DryingRackRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(DryingRackBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemStack itemOnRack = pBlockEntity.getItemOnRack();
        if (!itemOnRack.isEmpty()) {
            pPoseStack.pushPose();
            RenderSystem.enableBlend();

            Direction facing = getRackFacing(pBlockEntity);
            Direction opposite = facing.getOpposite();

            double posX = 0.5 + 0.375 * opposite.getStepX();
            double posY = 0.425;
            double posZ = 0.5 + 0.375 * opposite.getStepZ();

            pPoseStack.translate(posX, posY, posZ);
            pPoseStack.mulPose(new Quaternion(0, 180 - facing.toYRot(), 0, true));

            pPoseStack.scale(.75f, .75f, .75f);

            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            renderer.renderStatic(itemOnRack, ItemTransforms.TransformType.FIXED, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, pPackedOverlay);
            pPoseStack.popPose();
        }
    }

    private Direction getRackFacing(DryingRackBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        return level != null ? level.getBlockState(blockEntity.getBlockPos()).getValue(DryingRackBlock.FACING) : Direction.NORTH;
    }
}
