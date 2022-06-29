package net.silentchaos512.mechanisms.data.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.mechanisms.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(DataGenerator pGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ModBlocks.ALL_ORE_BLOCKS.forEach((ore, block) -> {
            Block oreBlock = block.get();
            super.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(oreBlock);
            super.tag(ore.tookLevelTag).add(oreBlock);
            super.tag(BlockTags.STONE_ORE_REPLACEABLES);
        });
    }
}
