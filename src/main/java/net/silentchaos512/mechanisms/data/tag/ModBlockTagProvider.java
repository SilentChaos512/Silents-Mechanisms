package net.silentchaos512.mechanisms.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.mechanisms.common.abstracts.BreakableBlock;
import net.silentchaos512.mechanisms.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator.getPackOutput(), provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider pProvider) {
        ModBlocks.BLOCK_DIRECT_REGISTRY.getEntries().forEach((b) -> {
                    if (b instanceof BreakableBlock block) {
                        super.tag(block.getHarvestLevel()).add(b);
                        super.tag(block.getHarvestTool()).add(b);
                    }
                }
        );

        ModBlocks.METAL_STORAGE_BLOCKS.forEach((provider, block) -> {
            TagKey<Block> tag = provider.getStorageBlockTag();
            super.tag(tag).add(block);
            super.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(tag);
        });

        for (Block block : ModBlocks.ALL_ORE_BLOCKS.values()) {
            super.tag(BlockTags.STONE_ORE_REPLACEABLES).add(block);
        }
    }
}
