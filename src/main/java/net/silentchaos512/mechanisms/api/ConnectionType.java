package net.silentchaos512.mechanisms.api;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum ConnectionType implements IStringSerializable {
    NONE,
    IN,
    OUT,
    BOTH;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean canReceive() {
        return this == IN || this == BOTH;
    }

    public boolean canExtract() {
        return this == OUT || this == BOTH;
    }
}
