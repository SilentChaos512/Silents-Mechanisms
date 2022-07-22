package net.silentchaos512.mechanisms.registration;

import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.mechanisms.SilentsMechanisms;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class DirectRegister<T extends ForgeRegistryEntry<T>> {
    private final Set<T> entries = new HashSet<>();
    private final IForgeRegistry<T> registry;

    public DirectRegister(IForgeRegistry<T> registry) {
        this.registry = registry;
    }

    public <U extends T> U register(String name, U entry) {
        if (entry.getRegistryName() != null) {
            entries.add(entry);
        } else {
            entries.add(entry.setRegistryName(SilentsMechanisms.loc(name)));
        }

        return entry;
    }

    public void registerAllEntries() {
        this.entries.forEach(registry::register);
    }

    public void forEachEntry(Consumer<T> action) {
        this.entries.forEach(action);
    }
}
