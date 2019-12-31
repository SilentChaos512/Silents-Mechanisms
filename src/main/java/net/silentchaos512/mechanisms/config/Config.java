package net.silentchaos512.mechanisms.config;

import net.minecraftforge.fml.loading.FMLPaths;
import net.silentchaos512.mechanisms.init.Ores;
import net.silentchaos512.utils.config.BooleanValue;
import net.silentchaos512.utils.config.ConfigSpecWrapper;
import net.silentchaos512.utils.config.IntValue;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public final class Config {
    private static final ConfigSpecWrapper WRAPPER = ConfigSpecWrapper.create(FMLPaths.CONFIGDIR.get().resolve("silents-mechanisms-common.toml"));

    public static final Common COMMON = new Common(WRAPPER);

    public static class Common {
        public final BooleanValue showBetaWelcomeMessage;
        public final IntValue worldGenOilLakeChance;
        private final BooleanValue oreWorldGenMasterSwitch;
        private final Map<Ores, OreConfig> oreConfigs = new EnumMap<>(Ores.class);

        public final IntValue fluidGeneratorInjectionVolume;

        public Common(ConfigSpecWrapper wrapper) {
            showBetaWelcomeMessage = wrapper
                    .builder("general.showBetaWelcomeMessage")
                    .comment("Shows a message in chat warning the player that the mod is early in development")
                    .define(true);

            wrapper.comment("world", "All world generation settings require you to restart Minecraft!");

            worldGenOilLakeChance = wrapper
                    .builder("world.oilLake.chance")
                    .comment("Chance of oil lakes spawning (1 in chance). Higher numbers = less common. Set 0 to disable.",
                            "Water is 4, lava is 80. Oil lakes will spawn underground about 90% of the time.",
                            "Note that disabling oil will make some items uncraftable unless recipes are changed")
                    .defineInRange(6, 0, Integer.MAX_VALUE);

            oreWorldGenMasterSwitch = wrapper
                    .builder("world.masterSwitch")
                    .comment("Set to 'false' to completely disable ore generation from this mod, ignoring all other settings.",
                            "You can also enable/disable ores individually, but this is useful if you plan to use another mod for ore generation.")
                    .define(true);

            Arrays.stream(Ores.values()).forEach(ore -> oreConfigs.put(ore, new OreConfig(ore, wrapper, oreWorldGenMasterSwitch)));

            fluidGeneratorInjectionVolume = wrapper
                    .builder("machine.fluidGenerators.injectionVolume")
                    .comment("The amount of fluid (in milliBuckets, or mB) fluid generators consume at once.",
                            "Lower values reduce waste, but may cause lag as the generator more frequently turns on/off.",
                            "A generator with less fluid in the tank will not be able to run.")
                    .defineInRange(100, 1, 1000);
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
