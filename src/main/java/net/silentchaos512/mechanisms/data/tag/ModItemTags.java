package net.silentchaos512.mechanisms.data.tag;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModItemTags {
    public static final Table<Metals.OreMetal, Metals.OreMetalType, TagKey<Item>> ALL_METAL_TAGS;
    public static final Map<Metals.OreMetal, TagKey<Item>> ALL_STORAGE_BLOCKS_TAGS;

    static {
        ALL_METAL_TAGS = HashBasedTable.create();
        for (Metals.OreMetal oreMetal : Metals.OreMetal.values()) {
            for (Metals.OreMetalType metalType : Metals.OreMetalType.values()) {
                switch (metalType) {
                    case INGOT ->
                            ALL_METAL_TAGS.put(oreMetal, metalType, ItemTags.create(new ResourceLocation("forge", "ingots/" + oreMetal.name().toLowerCase())));
                    case DUST ->
                            ALL_METAL_TAGS.put(oreMetal, metalType, ItemTags.create(new ResourceLocation("forge", "dusts/" + oreMetal.name().toLowerCase())));
                    case CHUNKS ->
                            ALL_METAL_TAGS.put(oreMetal, metalType, ItemTags.create(new ResourceLocation("forge", "chunks/" + oreMetal.name().toLowerCase())));
                    case NUGGET ->
                            ALL_METAL_TAGS.put(oreMetal, metalType, ItemTags.create(new ResourceLocation("forge", "nuggets/" + oreMetal.name().toLowerCase())));
                }
            }
        }

        ALL_STORAGE_BLOCKS_TAGS = new HashMap<>();
        for (Metals.OreMetal metal : Metals.OreMetal.values()) {
            ALL_STORAGE_BLOCKS_TAGS.put(metal, ItemTags.create(new ResourceLocation("forge", "storage_blocks/" + metal.name().toLowerCase())));
        }
    }

    public static final class Provider extends ItemTagsProvider {
        public Provider(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, pBlockTagsProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {
            for (Table.Cell<Metals.OreMetal, Metals.OreMetalType, TagKey<Item>> cell : ALL_METAL_TAGS.cellSet()) {
                switch (cell.getColumnKey()) {
                    case INGOT -> super.tag(Tags.Items.INGOTS).addTag(cell.getValue());
                    case DUST -> super.tag(Tags.Items.DUSTS).addTag(cell.getValue());
                    case NUGGET -> super.tag(Tags.Items.NUGGETS).addTag(cell.getValue());
                }
                super.tag(cell.getValue()).add(ModItems.ALL_ORE_METALS.get(cell.getRowKey(), cell.getColumnKey()).get());
            }
            ALL_STORAGE_BLOCKS_TAGS.forEach((oreMetal, itemTagKey) -> {
                super.tag(Tags.Items.STORAGE_BLOCKS).addTag(itemTagKey);
                super.tag(itemTagKey).add(ModBlocks.ALL_STORAGE_BLOCKS.get(oreMetal).get().asItem());
            });
        }
    }
}
