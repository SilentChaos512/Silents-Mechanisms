package net.silentchaos512.mechanisms.api;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public enum RedstoneMode {
    IGNORED(new ResourceLocation("textures/item/barrier.png")),
    ON(new ResourceLocation("textures/block/redstone_torch.png")),
    OFF(new ResourceLocation("textures/block/redstone_torch_off.png"));

    private final ResourceLocation texture;

    RedstoneMode(ResourceLocation texture) {
        this.texture = texture;
    }

    @Nullable
    public static RedstoneMode byName(String name) {
        for (RedstoneMode mode : values()) {
            if (mode.name().equalsIgnoreCase(name)) {
                return mode;
            }
        }
        return null;
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
