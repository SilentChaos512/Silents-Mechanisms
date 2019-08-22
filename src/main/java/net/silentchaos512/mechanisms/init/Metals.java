package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.silentchaos512.mechanisms.block.MetalBlock;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;

/**
 * Holds metal storage blocks. Could possibly store items like ingots and nuggets, but not really
 * necessary right now.
 */
public enum Metals {
    COPPER,
    TIN,
    SILVER,
    LEAD,
    NICKEL,
    ZINC,
    BISMUTH,
    ALUMINUM,
    URANIUM,
    BISMUTH_BRASS,
    BRASS,
    BRONZE,
    INVAR,
    STEEL,
    ;

    private final Lazy<Block> block;

    Metals() {
        this.block = Lazy.of(MetalBlock::new);
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Block getBlock() {
        return block.get();
    }
}
