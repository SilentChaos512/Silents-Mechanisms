package net.silentchaos512.mechanisms.config;

import net.silentchaos512.mechanisms.init.Ores;
import net.silentchaos512.utils.config.BooleanValue;
import net.silentchaos512.utils.config.ConfigSpecWrapper;
import net.silentchaos512.utils.config.IntValue;

public class OreConfig {
    private final BooleanValue masterSwitch;
    private final IntValue veinCount;
    private final IntValue veinSize;
    private final IntValue minHeight;
    private final IntValue maxHeight;

    public OreConfig(Ores ore, ConfigSpecWrapper wrapper, BooleanValue masterSwitch) {
        String key = "world.gen." + ore + ".";
        this.masterSwitch = masterSwitch;
        this.veinCount = wrapper.builder(key + "veinCount")
                .comment("Number of veins per chunk")
                .defineInRange(ore.getDefaultOreConfigs().getVeinCount(), 0, Integer.MAX_VALUE);
        this.veinSize = wrapper.builder(key + "veinSize")
                .comment("Size of veins")
                .defineInRange(ore.getDefaultOreConfigs().getVeinSize(), 0, 100);
        this.minHeight = wrapper.builder(key + "minHeight")
                .comment("Minimum Y-coordinate (base height) of veins")
                .defineInRange(ore.getDefaultOreConfigs().getMinHeight(), 0, 255);
        this.maxHeight = wrapper.builder(key + "maxHeight")
                .comment("Maximum Y-coordinate (highest level) of veins")
                .defineInRange(ore.getDefaultOreConfigs().getMaxHeight(), 0, 255);
    }

    public boolean isEnabled() {
        return masterSwitch.get() && getVeinCount() > 0 && getVeinSize() > 0;
    }

    public int getVeinCount() {
        return veinCount.get();
    }

    public int getVeinSize() {
        return veinSize.get();
    }

    public int getMinHeight() {
        return minHeight.get();
    }

    public int getMaxHeight() {
        return maxHeight.get();
    }
}
