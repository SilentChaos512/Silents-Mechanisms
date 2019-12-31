package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.silentchaos512.mechanisms.block.OreBlockSM;
import net.silentchaos512.mechanisms.config.Config;
import net.silentchaos512.mechanisms.config.OreConfig;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;
import java.util.Optional;

/**
 * Handles ore blocks and default ore configs
 */
public enum Ores {
    COPPER(3, 1, new DefaultOreConfigs(8, 8, 40, 90)),
    TIN(3, 1, new DefaultOreConfigs(8, 8, 20, 80)),
    SILVER(4, 2, new DefaultOreConfigs(4, 8, 0, 40)),
    LEAD(4, 2, new DefaultOreConfigs(4, 8, 0, 30)),
    NICKEL(5, 2, new DefaultOreConfigs(1, 6, 0, 24)),
    PLATINUM(5, 2, new DefaultOreConfigs(1, 8, 5, 20)),
    ZINC(3, 1, new DefaultOreConfigs(4, 8, 20, 60)),
    BISMUTH(3, 1, new DefaultOreConfigs(4, 8, 16, 64)),
    BAUXITE(4, 1, new DefaultOreConfigs(6, 8, 15, 50)),
    URANIUM(6, 2, new DefaultOreConfigs(1, 4, 0, 18)),
    ;

    private final Lazy<Block> block;
    private final DefaultOreConfigs defaultOreConfigs;

    Ores(int hardness, int harvestLevel, DefaultOreConfigs defaultOreConfigs) {
        this.defaultOreConfigs = defaultOreConfigs;
        this.block = Lazy.of(() -> new OreBlockSM(hardness, harvestLevel));
    }
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Block getBlock() {
        return block.get();
    }

    public DefaultOreConfigs getDefaultOreConfigs() {
        return defaultOreConfigs;
    }

    public Optional<OreConfig> getConfig() {
        return Config.COMMON.getOreConfig(this);
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
