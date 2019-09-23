package net.silentchaos512.mechanisms.util;

import net.minecraft.util.ResourceLocation;
import net.silentchaos512.mechanisms.SilentMechanisms;

public final class Constants {
    public static final ResourceLocation ALLOY_SMELTING = SilentMechanisms.getId("alloy_smelting");
    public static final ResourceLocation COMPRESSING = SilentMechanisms.getId("compressing");
    public static final ResourceLocation CRUSHING = SilentMechanisms.getId("crushing");
    public static final ResourceLocation DRYING = SilentMechanisms.getId("drying");
    public static final ResourceLocation SOLDERING = SilentMechanisms.getId("soldering");

    // Machine upgrades
    public static final int UPGRADES_PER_SLOT = 1;
    public static final float UPGRADE_PROCESSING_SPEED_AMOUNT = 0.5f;
    public static final float UPGRADE_SECONDARY_OUTPUT_AMOUNT = 0.1f;
    public static final float UPGRADE_ENERGY_EFFICIENCY_AMOUNT = -0.15f;

    private Constants() {throw new IllegalAccessError("Utility class");}
}
