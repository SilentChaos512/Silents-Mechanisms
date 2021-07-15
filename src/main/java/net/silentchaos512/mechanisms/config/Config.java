package net.silentchaos512.mechanisms.config;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.init.Ores;

import java.util.*;

@Mod.EventBusSubscriber(modid = SilentMechanisms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config {
    public static final ForgeConfigSpec.BooleanValue showBetaWelcomeMessage;
    public static final ForgeConfigSpec.IntValue worldGenOilLakeChance;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> worldGenOilDimensionBlacklist;
    public static final ForgeConfigSpec.IntValue fluidGeneratorInjectionVolume;
    private static final ForgeConfigSpec commonSpec;
    private static final ForgeConfigSpec.BooleanValue oreWorldGenMasterSwitch;
    private static final Map<Ores, OreConfig> oreConfigs = new EnumMap<>(Ores.class);

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        showBetaWelcomeMessage = builder
                .comment("Shows a message in chat warning the player that the mod is early in development")
                .define("general.showBetaWelcomeMessage", true);
        fluidGeneratorInjectionVolume = builder
                .comment("The amount of fluid (in milliBuckets, or mB) fluid generators consume at once.",
                        "Lower values reduce waste, but may cause lag as the generator more frequently turns on/off.",
                        "A generator with less fluid in the tank will not be able to run.")
                .defineInRange("machine.fluidGenerators.injectionVolume", 100, 1, 1000);

        // World Gen/Ores
        {
            builder.push("world");

            oreWorldGenMasterSwitch = builder
                    .comment("Set to 'false' to completely disable ore generation from this mod, ignoring all other settings.",
                            "You can also enable/disable ores individually, but this is useful if you plan to use another mod for ore generation.")
                    .define("masterSwitch", true);

            worldGenOilLakeChance = builder
                    .comment("Chance of oil lakes spawning (1 in chance). Higher numbers = less common. Set 0 to disable.",
                            "Water is 4, lava is 80. Oil lakes will spawn underground about 90% of the time.",
                            "Note that disabling oil will make some items uncraftable unless recipes are changed")
                    .defineInRange("oilLake.chance", 6, 0, Integer.MAX_VALUE);

            worldGenOilDimensionBlacklist = builder
                    .comment("The dimensions that oil lakes are not allow to generate in")
                    .define("oilLake.dimensionBlacklist", ImmutableList.of(
                            World.NETHER.location().toString(),
                            World.END.location().toString()
                    ), o -> o instanceof List);

            builder.comment("Configs for specific ores. Set veinCount to zero to disable an ore.");
            builder.push("ores");
            Arrays.stream(Ores.values()).forEach(ore -> oreConfigs.put(ore, new OreConfig(ore, builder, oreWorldGenMasterSwitch)));

            builder.pop(2);
        }

        commonSpec = builder.build();
    }

    public static Optional<OreConfig> getOreConfig(Ores ore) {
        return Optional.ofNullable(oreConfigs.getOrDefault(ore, null));
    }

    private Config() {}

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
    }

    public static void sync() {
    }

    @SubscribeEvent
    public static void sync(ModConfig.Loading event) {
        sync();
    }

    @SubscribeEvent
    public static void sync(ModConfig.Reloading event) {
        sync();
    }
}
