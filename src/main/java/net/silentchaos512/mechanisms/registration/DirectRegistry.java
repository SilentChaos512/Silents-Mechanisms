package net.silentchaos512.mechanisms.registration;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class DirectRegistry<T> {
    private final Map<ResourceLocation, T> entries = new HashMap<>();

    public DirectRegistry() {
    }

    public <U extends T> U register(String name, U entry) {
        this.entries.put(SilentsMechanisms.location(name), entry);

        return entry;
    }

    public void registerAll(RegisterEvent.RegisterHelper<T> helper) {
        this.entries.forEach(helper::register);
    }

    public Collection<T> getEntries() {
        return this.entries.values();
    }

    public Map<ResourceLocation, T> getMappings() {
        return this.entries;
    }
}
