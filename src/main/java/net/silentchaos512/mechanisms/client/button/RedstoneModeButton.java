package net.silentchaos512.mechanisms.client.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseContainer;
import net.silentchaos512.utils.EnumUtils;

import net.minecraft.client.gui.widget.button.Button.IPressable;

public class RedstoneModeButton extends Button {
    private final AbstractMachineBaseContainer container;

    public RedstoneModeButton(AbstractMachineBaseContainer container, int x, int y, int width, int height, IPressable onPress) {
        super(x, y, width, height, new StringTextComponent(""), button -> {
            ((RedstoneModeButton) button).cycleMode();
            onPress.onPress(button);
        });
        this.container = container;
    }

    public RedstoneMode getMode() {
        return container.getRedstoneMode();
    }

    private void cycleMode() {
        int ordinal = container.getRedstoneMode().ordinal() + 1;
        if (ordinal >= RedstoneMode.values().length)
            ordinal = 0;
        container.setRedstoneMode(EnumUtils.byOrdinal(ordinal, RedstoneMode.IGNORED));
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(container.getRedstoneMode().getTexture());
        GlStateManager._disableDepthTest();

        blit(matrixStack, this.x, this.y, 0, 0, this.width, this.height, 16, 16);
        GlStateManager._enableDepthTest();
    }
}
