package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.mechanisms.config.Config;
import net.silentchaos512.mechanisms.config.OreConfig;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Handles ore blocks and default ore configs
 */
public enum Ores implements IBlockProvider {
    COPPER(() -> Metals.COPPER, 3, 1, new DefaultOreConfigs(8, 8, 40, 90)),
    TIN(() -> Metals.TIN, 3, 1, new DefaultOreConfigs(8, 8, 20, 80)),
    SILVER(() -> Metals.SILVER, 4, 2, new DefaultOreConfigs(4, 8, 0, 40)),
    LEAD(() -> Metals.LEAD, 4, 2, new DefaultOreConfigs(4, 8, 0, 30)),
    NICKEL(() -> Metals.NICKEL, 5, 2, new DefaultOreConfigs(1, 6, 0, 24)),
    PLATINUM(() -> Metals.PLATINUM, 5, 2, new DefaultOreConfigs(1, 8, 5, 20)),
    ZINC(() -> Metals.ZINC, 3, 1, new DefaultOreConfigs(4, 8, 20, 60)),
    BISMUTH(() -> Metals.BISMUTH, 3, 1, new DefaultOreConfigs(4, 8, 16, 64)),
    BAUXITE(() -> Metals.ALUMINUM, 4, 1, new DefaultOreConfigs(6, 8, 15, 50)),
    URANIUM(() -> Metals.URANIUM, 6, 2, new DefaultOreConfigs(1, 4, 0, 18)),
    ;

    private final Supplier<Metals> metal;
    private final DefaultOreConfigs defaultOreConfigs;
    private final int hardness;
    private final int harvestLevel;

    Ores(Supplier<Metals> metal, int hardness, int harvestLevel, DefaultOreConfigs defaultOreConfigs) {
        this.metal = metal;
        this.defaultOreConfigs = defaultOreConfigs;
        this.hardness = hardness;
        this.harvestLevel = harvestLevel;
    }
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getHardness() {
        return hardness;
    }

    public int getHarvestLevel() {
        return harvestLevel;
    }

    public DefaultOreConfigs getDefaultOreConfigs() {
        return defaultOreConfigs;
    }

    public Optional<OreConfig> getConfig() {
        return Config.COMMON.getOreConfig(this);
    }

    @Override
    public Block asBlock() {
        return metal.get().getOre().get();
    }

    public static class DefaultOreConfigs {
        private final int veinCount;
        private final int veinSize;
        private final int minHeight;
        private final int maxHeight;

        public DefaultOreConfigs(int veinCount, int veinSize, int minHeight, int maxHeight) {
            this.veinCount = veinCount;
            this.veinSize = veinSize;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
        }

        public int getVeinCount() {
            return veinCount;
        }

        public int getVeinSize() {
            return veinSize;
        }

        public int getMinHeight() {
            return minHeight;
        }

        public int getMaxHeight() {
            return maxHeight;
        }
    }
}
