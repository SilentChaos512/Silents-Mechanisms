package net.silentchaos512.mechanisms.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModTags;
import net.silentchaos512.mechanisms.item.CraftingItems;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        // Empties
        builder(forgeId("nuggets/coal"));
        builder(forgeId("storage_blocks/charcoal"));

        getBuilder(ModTags.Items.STEELS).add(
                itemTag(forgeId("ingots/aluminum_steel")),
                itemTag(forgeId("ingots/bismuth_steel")),
                itemTag(forgeId("ingots/steel"))
        );
        getBuilder(ModTags.Items.COAL_GENERATOR_FUELS).add(
                ItemTags.COALS,
                itemTag(forgeId("nuggets/coal")),
                itemTag(forgeId("storage_blocks/charcoal")),
                Tags.Items.STORAGE_BLOCKS_COAL
        );
        copy(blockTag(modId("drying_racks")), itemTag(modId("drying_racks")));

        getBuilder(ModTags.Items.DUSTS_COAL).add(CraftingItems.COAL_DUST.asItem());

        for (Metals metal : Metals.values()) {
            metal.getOreTag().ifPresent(tag ->
                    copy(tag, new ItemTags.Wrapper(tag.getId())));
            metal.getStorageBlockTag().ifPresent(tag ->
                    copy(tag, new ItemTags.Wrapper(tag.getId())));
            metal.getChunksTag().ifPresent(tag ->
                    getBuilder(tag).add(metal.getChunks().get()));
            metal.getDustTag().ifPresent(tag ->
                    getBuilder(tag).add(metal.getDust().get()));
            metal.getIngotTag().ifPresent(tag ->
                    metal.getIngot().ifPresent(item ->
                            getBuilder(tag).add(item)));
            metal.getNuggetTag().ifPresent(tag ->
                    metal.getNugget().ifPresent(item ->
                            getBuilder(tag).add(item)));
        }

        copy(Tags.Blocks.ORES, Tags.Items.ORES);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        groupBuilder(ModTags.Items.CHUNKS, Metals::getChunksTag);
        groupBuilder(Tags.Items.DUSTS, Metals::getDustTag,
                ModTags.Items.DUSTS_COAL);
        groupBuilder(Tags.Items.INGOTS, Metals::getIngotTag);
        groupBuilder(Tags.Items.NUGGETS, Metals::getNuggetTag);
    }

    @SafeVarargs
    private final void groupBuilder(Tag<Item> tag, Function<Metals, Optional<Tag<Item>>> tagGetter, Tag<Item>... extras) {
        Tag.Builder<Item> builder = getBuilder(tag);
        for (Metals metal : Metals.values()) {
            tagGetter.apply(metal).ifPresent(builder::add);
        }
        for (Tag<Item> extraTag : extras) {
            builder.add(extraTag);
        }
    }

    private void builder(ResourceLocation id, IItemProvider... items) {
        getBuilder(itemTag(id)).add(Arrays.stream(items).map(IItemProvider::asItem).toArray(Item[]::new));
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
        return "Silent's Mechanisms - Item Tags";
    }
}
