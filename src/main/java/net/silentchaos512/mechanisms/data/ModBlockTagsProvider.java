package net.silentchaos512.mechanisms.data;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackBlock;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModTags;
import net.silentchaos512.mechanisms.init.Registration;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void registerTags() {
        getBuilder(ModTags.Blocks.DRYING_RACKS).add(Registration.getBlocks(DryingRackBlock.class).toArray(new Block[0]));

        for (Metals metal : Metals.values()) {
            metal.getOreTag().ifPresent(tag ->
                    getBuilder(tag).add(metal.getOre().get()));
            metal.getStorageBlockTag().ifPresent(tag ->
                    getBuilder(tag).add(metal.getStorageBlock().get()));
        }

        groupBuilder(Tags.Blocks.ORES, Metals::getOreTag);
        groupBuilder(Tags.Blocks.STORAGE_BLOCKS, Metals::getStorageBlockTag);
    }

    private void groupBuilder(Tag<Block> tag, Function<Metals, Optional<Tag<Block>>> tagGetter) {
        Tag.Builder<Block> builder = getBuilder(tag);
        for (Metals metal : Metals.values()) {
            tagGetter.apply(metal).ifPresent(builder::add);
        }
    }

    private void builder(ResourceLocation id, IBlockProvider... items) {
        getBuilder(blockTag(id)).add(Arrays.stream(items).map(IBlockProvider::asBlock).toArray(Block[]::new));
    }

    private void builder(ResourceLocation id, Block... blocks) {
        getBuilder(blockTag(id)).add(blocks);
    }

    private static Tag<Block> blockTag(ResourceLocation id) {
        return new BlockTags.Wrapper(id);
    }

    private static Tag<Item> itemTag(ResourceLocation id) {
        return new ItemTags.Wrapper(id);
    }

    private static ResourceLocation modId(String path) {
        return SilentMechanisms.getId(path);
    }

    private static ResourceLocation forgeId(String path) {
        return new ResourceLocation("forge", path);
    }

    @Override
    public String getName() {
        return "Silent's Mechanisms - Block Tags";
    }
}
