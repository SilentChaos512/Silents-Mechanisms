package net.silentchaos512.mechanisms.config;

import net.minecraftforge.fml.loading.FMLPaths;
import net.silentchaos512.mechanisms.init.Ores;
import net.silentchaos512.utils.config.BooleanValue;
import net.silentchaos512.utils.config.ConfigSpecWrapper;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public final class Config {
    private static final ConfigSpecWrapper WRAPPER = ConfigSpecWrapper.create(FMLPaths.CONFIGDIR.get().resolve("silents-mechanisms-common.toml"));

    public static final Common COMMON = new Common(WRAPPER);

    public static class Common {
        private final BooleanValue oreWorldGenMasterSwitch;
        private final Map<Ores, OreConfig> oreConfigs = new EnumMap<>(Ores.class);

        public Common(ConfigSpecWrapper wrapper) {
            wrapper.comment("world", "All world generation settings require you to restart Minecraft!");

            oreWorldGenMasterSwitch = wrapper
                    .builder("world.masterSwitch")
                    .comment("Set to 'false' to completely disable ore generation from this mod, ignoring all other settings.",
                            "You can also enable/disable ores individually, but this is useful if you plan to use another mod for ore generation.")
                    .define(true);

            Arrays.stream(Ores.values()).forEach(ore -> oreConfigs.put(ore, new OreConfig(ore, wrapper, oreWorldGenMasterSwitch)));
        }

        public Optional<OreConfig> getOreConfig(Ores ore) {
            if (oreConfigs.containsKey(ore))
                return Optional.of(oreConfigs.get(ore));
            return Optional.empty();
        }
    }

    private Config() {}

    public static void init() {
        WRAPPER.validate();
        WRAPPER.validate();
    }
}
