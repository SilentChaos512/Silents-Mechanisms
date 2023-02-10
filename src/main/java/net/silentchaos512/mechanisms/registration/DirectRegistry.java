package net.silentchaos512.mechanisms.registration;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DirectRegistry<T> {
    public static final Logger LOGGER = LogManager.getLogger("SM Registry Debug");
    private final Map<T, ResourceLocation> entries = new LinkedHashMap<>();
    private final boolean debug;

    public DirectRegistry() {
        this(false);
    }

    public DirectRegistry(boolean debug) {
        this.debug = debug;
    }

    public <U extends T> U register(String name, U entry) {
        if (debug) LOGGER.info("Putting [{}]", name);
        this.entries.put(entry, SilentsMechanisms.location(name));

        return entry;
    }

    public ResourceLocation getId(T entry) {
        return this.entries.get(entry);
    }

    public void registerAll(RegisterEvent.RegisterHelper<T> helper) {
        if (this.entries.isEmpty()) LOGGER.warn("DirectRegistry of [{}] is empty", helper);
        this.entries.forEach((object, rl) -> {
            if (debug) LOGGER.info("Registering [{}]", rl);
            helper.register(rl, object);
        });
    }

    public Collection<T> getEntries() {
        return this.entries.keySet();
    }

    public Map<T, ResourceLocation> getMappings() {
        return this.entries;
    }
}
