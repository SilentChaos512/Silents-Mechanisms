package net.silentchaos512.mechanisms.util;

public enum MachineTier {
    BASIC(10_000, 1.0f),
    STANDARD(50_000, 1.5f),
    ;

    private final int energyCapacity;
    private final float processingSpeed;

    MachineTier(int energyCapacity, float processingSpeed) {
        this.energyCapacity = energyCapacity;
        this.processingSpeed = processingSpeed;
    }

    public int getEnergyCapacity() {
        return energyCapacity;
    }

    public float getProcessingSpeed() {
        return processingSpeed;
    }
}
