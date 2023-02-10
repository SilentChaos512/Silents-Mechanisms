package net.silentchaos512.mechanisms.utls;


import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class TranslateUtils {
    private TranslateUtils() {
    }

    public static final String KEY_FORMAT = "%s.silents_mechanisms.%s";
    public static final String ENERGY_FORMAT = "%,d";

    public static MutableComponent translate(String pre, String suf, Object... params) {
        return Component.translatable(KEY_FORMAT.formatted(pre, suf), params);
    }

    public static MutableComponent translateEnergyWithMax(int energy, int capacity) {
        return translate("misc", "energyWithMax", ENERGY_FORMAT.formatted(energy), ENERGY_FORMAT.formatted(capacity));
    }
}
