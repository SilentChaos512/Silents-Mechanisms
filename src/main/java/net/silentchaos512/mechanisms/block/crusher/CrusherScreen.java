package net.silentchaos512.mechanisms.block.crusher;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.AbstractMachineScreen;

public class CrusherScreen extends AbstractMachineScreen<CrusherContainer> {
    public static final ResourceLocation TEXTURE = SilentMechanisms.getId("textures/gui/crusher.png");

    public CrusherScreen(CrusherContainer containerIn, PlayerInventory playerInventoryIn, ITextComponent titleIn) {
        super(containerIn, playerInventoryIn, titleIn);
    }

    @Override
    public ResourceLocation getGuiTexture() {
        return TEXTURE;
    }

    @Override
    protected int getProgressArrowPosX(int guiPosX) {
        return guiPosX + 49;
    }

    @Override
    protected int getProgressArrowPosY(int guiPosY) {
        return guiPosY + 34;
    }
}
