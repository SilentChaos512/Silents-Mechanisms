package net.silentchaos512.mechanisms.data.tag;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModItemTags {
    public static final HashBasedTable<Metals.Alloy, Metals.AlloyType, TagKey<Item>> ALL_ALLOY_TAGS;
    public static final HashBasedTable<Metals.OreMetal, Metals.OreMetalType, TagKey<Item>> ALL_METAL_TAGS;
    public static final Map<Metals.StorageBlockProvider, TagKey<Item>> ALL_METAL_BLOCK_TAGS;

    //STANDALONE TAGS
    public static final TagKey<Item> COAL_GENERATOR_FUELS = ItemTags.create(SilentsMechanisms.location("coal_generator_fuels"));

    static {
        ALL_METAL_BLOCK_TAGS = Metals.StorageBlockProvider.ALL_PROVIDERS.stream().collect(Collectors.toMap(provider -> provider, provider -> ItemTags.create(new ResourceLocation("forge", "storage_blocks/" + provider.toString()))));

        ALL_METAL_TAGS = HashBasedTable.create();
        for (Metals.OreMetal oreMetal : Metals.OreMetal.values()) {
            for (Metals.OreMetalType metalType : Metals.OreMetalType.values()) {
                switch (metalType) {
                    case INGOT ->
                            ALL_METAL_TAGS.put(oreMetal, metalType, forge("ingots", oreMetal.name().toLowerCase()));
                    case DUST -> ALL_METAL_TAGS.put(oreMetal, metalType, forge("dusts", oreMetal.name().toLowerCase()));
                    case RAW ->
                            ALL_METAL_TAGS.put(oreMetal, metalType, forge("raw_materials", oreMetal.name().toLowerCase()));
                    case NUGGET ->
                            ALL_METAL_TAGS.put(oreMetal, metalType, forge("nuggets", oreMetal.name().toLowerCase()));
                }
            }
        }

        ALL_ALLOY_TAGS = HashBasedTable.create();
        for (Metals.Alloy alloy : Metals.Alloy.values()) {
            for (Metals.AlloyType alloyType : Metals.AlloyType.values()) {
                ALL_ALLOY_TAGS.put(alloy, alloyType, forge(alloyType.toString().concat("s"), alloy.toString()));
            }
        }
    }

    private static TagKey<Item> create(String name) {
        return ItemTags.create(SilentsMechanisms.location(name));
    }

    private static TagKey<Item> forge(String prefix, String suffix) {
        return ItemTags.create(new ResourceLocation("forge", prefix + "/" + suffix));
    }

    @SuppressWarnings("ConstantConditions")
    public static final class Provider extends ItemTagsProvider {
        public Provider(DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> completableFuture, BlockTagsProvider pBlockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator.getPackOutput(), completableFuture, pBlockTagsProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags(@Nonnull HolderLookup.Provider p) {
            for (Table.Cell<Metals.OreMetal, Metals.OreMetalType, TagKey<Item>> cell : ALL_METAL_TAGS.cellSet()) {
                switch (cell.getColumnKey()) {
                    case INGOT -> super.tag(Tags.Items.INGOTS).addTag(cell.getValue());
                    case DUST -> super.tag(Tags.Items.DUSTS).addTag(cell.getValue());
                    case NUGGET -> super.tag(Tags.Items.NUGGETS).addTag(cell.getValue());
                }
                super.tag(cell.getValue()).add(ModItems.ALL_ORE_METALS.get(cell.getRowKey(), cell.getColumnKey()));
            }

            ALL_METAL_BLOCK_TAGS.forEach(((provider, tag) -> {
                super.tag(Tags.Items.STORAGE_BLOCKS).addTag(tag);
                super.tag(tag).add(provider.getStorageBlock().asItem());
            }));

            for (Table.Cell<Metals.Alloy, Metals.AlloyType, TagKey<Item>> cell : ALL_ALLOY_TAGS.cellSet()) {
                switch (cell.getColumnKey()) {
                    case DUST -> super.tag(Tags.Items.DUSTS).addTag(cell.getValue());
                    case INGOT -> super.tag(Tags.Items.INGOTS).addTag(cell.getValue());
                    case NUGGET -> super.tag(Tags.Items.NUGGETS).addTag(cell.getValue());
                }

                super.tag(cell.getValue()).add(ModItems.ALL_ALLOYS.get(cell.getRowKey(), cell.getColumnKey()));
            }

            ModBlocks.METAL_STORAGE_BLOCKS.forEach((provider, block) -> {
                TagKey<Block> blockTag = provider.getStorageBlockTag();
                TagKey<Item> itemTag = ItemTags.create(blockTag.location());
                super.copy(blockTag, itemTag);
                super.tag(Tags.Items.STORAGE_BLOCKS).addTag(itemTag);
            });

            super.tag(COAL_GENERATOR_FUELS).add(Items.COAL, Items.CHARCOAL, Items.COAL_BLOCK);
        }
    }
}
