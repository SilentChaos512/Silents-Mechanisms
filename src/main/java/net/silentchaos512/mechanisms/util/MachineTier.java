package net.silentchaos512.mechanisms.util;

public enum MachineTier {
    BASIC(0, 10_000, 1.0f),
    STANDARD(4, 50_000, 2.0f),
    ;

    private final int upgradeSlots;
    private final int energyCapacity;
    private final float processingSpeed;

    MachineTier(int upgradeSlots, int energyCapacity, float processingSpeed) {
        this.upgradeSlots = upgradeSlots;
        this.energyCapacity = energyCapacity;
        this.processingSpeed = processingSpeed;
    }

    public int getUpgradeSlots() {
        return upgradeSlots;
    }

    public int getEnergyCapacity() {
        return energyCapacity;
    }

    public float getProcessingSpeed() {
        return processingSpeed;
    }
}
