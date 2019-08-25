package net.silentchaos512.mechanisms.api;

import net.minecraft.util.ResourceLocation;

public enum RedstoneMode {
    IGNORED(new ResourceLocation("textures/item/barrier.png")),
    ON(new ResourceLocation("textures/block/redstone_torch.png")),
    OFF(new ResourceLocation("textures/block/redstone_torch_off.png"));

    private final ResourceLocation texture;

    RedstoneMode(ResourceLocation texture) {
        this.texture = texture;
    }

    public boolean shouldRun(boolean isPowered) {
        if (this == ON)
            return isPowered;
        if (this == OFF)
            return !isPowered;
        return true;
    }

    public ResourceLocation getTexture() {
        return texture;
    }
}
