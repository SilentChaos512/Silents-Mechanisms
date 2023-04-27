package net.silentchaos512.mechanisms.common.blocks;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.silentchaos512.mechanisms.common.abstracts.BreakableBlock;

public class HarvestableBlock extends Block implements BreakableBlock {
    private final TagKey<Block> harvestToolTag;
    private final TagKey<Block> levelTag;

    public HarvestableBlock(Properties properties, TagKey<Block> toolTag) {
        this(properties, toolTag, Tags.Blocks.NEEDS_WOOD_TOOL);
    }

    public HarvestableBlock(Properties properties, TagKey<Block> toolTag, TagKey<Block> levelTag) {
        super(properties);
        this.harvestToolTag = toolTag;
        this.levelTag = levelTag;
    }

    @Override
    public TagKey<Block> getHarvestLevel() {
        return harvestToolTag;
    }

    @Override
    public TagKey<Block> getHarvestTool() {
        return levelTag;
    }
}
