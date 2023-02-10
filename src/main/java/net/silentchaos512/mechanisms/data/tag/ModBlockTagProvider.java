package net.silentchaos512.mechanisms.data.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.mechanisms.common.abstracts.BreakableBlock;
import net.silentchaos512.mechanisms.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(DataGenerator pGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ModBlocks.BLOCK_DIRECT_REGISTRY.getEntries().forEach((b) -> {
                    if (b instanceof BreakableBlock block) {
                        super.tag(block.getHarvestLevel()).add(b);
                        super.tag(block.getHarvestTool()).add(b);
                    }
                }
        );

        for (Block block : ModBlocks.ALL_ORE_BLOCKS.values()) {
            super.tag(BlockTags.STONE_ORE_REPLACEABLES).add(block);
        }
    }
}
