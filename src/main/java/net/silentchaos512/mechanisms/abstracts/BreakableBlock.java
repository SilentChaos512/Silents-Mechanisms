package net.silentchaos512.mechanisms.abstracts;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface BreakableBlock {
    TagKey<Block> getHarvestLevel();
    TagKey<Block> getHarvestTool();
}
