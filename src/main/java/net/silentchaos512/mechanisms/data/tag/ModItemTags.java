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
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ModItemTags {
    public static final HashBasedTable<Metals.OreMetal, Metals.OreMetalType, TagKey<Item>> ALL_METAL_TAGS;
    public static final HashMap<Metals.OreMetal, TagKey<Item>> ALL_STORAGE_BLOCKS_TAGS;
    public static final HashBasedTable<Metals.Alloy, Metals.AlloyType, TagKey<Item>> ALL_ALLOY_TAGS;
    public static final HashMap<Metals.Alloy, TagKey<Item>> ALL_ALLOY_STORAGE_BLOCKS_TAGS;

    //STANDALONE TAGS
    public static final TagKey<Item> COAL_GENERATOR_FUELS = ItemTags.create(SilentsMechanisms.location("coal_generator_fuels"));

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

        ALL_ALLOY_TAGS = HashBasedTable.create();
        ALL_ALLOY_STORAGE_BLOCKS_TAGS = new HashMap<>();
        for (Metals.Alloy alloy : Metals.Alloy.values()) {
            for (Metals.AlloyType alloyType : Metals.AlloyType.values()) {
                switch (alloyType) {
                    case DUST -> ALL_ALLOY_TAGS.put(alloy, alloyType, ItemTags.create(new ResourceLocation("forge", "dusts/" + alloy.name().toLowerCase() + '_' + alloyType.name().toLowerCase())));
                    case INGOT -> ALL_ALLOY_TAGS.put(alloy, alloyType, ItemTags.create(new ResourceLocation("forge", "ingots/" + alloy.name().toLowerCase() + '_' + alloyType.name().toLowerCase())));
                    case NUGGET -> ALL_ALLOY_TAGS.put(alloy, alloyType, ItemTags.create(new ResourceLocation("forge", "nuggets/" + alloy.name().toLowerCase() + '_' + alloyType.name().toLowerCase())));
                }
            }
            ALL_ALLOY_STORAGE_BLOCKS_TAGS.put(alloy, ItemTags.create(new ResourceLocation("forge", "storage_blocks/" + alloy.name().toLowerCase())));
        }

    }

    @SuppressWarnings("ConstantConditions")
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
                super.tag(cell.getValue()).add(ModItems.ALL_ORE_METALS.get(cell.getRowKey(), cell.getColumnKey()));
            }
            ALL_STORAGE_BLOCKS_TAGS.forEach((oreMetal, itemTagKey) -> {
                super.tag(Tags.Items.STORAGE_BLOCKS).addTag(itemTagKey);
                super.tag(itemTagKey).add(ModBlocks.ALL_STORAGE_BLOCKS.get(oreMetal).asItem());
            });

            ALL_ALLOY_STORAGE_BLOCKS_TAGS.forEach((alloy, tag) -> {
                super.tag(Tags.Items.STORAGE_BLOCKS).addTag(tag);
                super.tag(tag).add(ModBlocks.ALL_ALLOY_STORAGE_BLOCKS.get(alloy).asItem());
            });

            for (Table.Cell<Metals.Alloy, Metals.AlloyType, TagKey<Item>> cell : ALL_ALLOY_TAGS.cellSet()) {
                switch(cell.getColumnKey()) {
                    case DUST -> super.tag(Tags.Items.DUSTS).addTag(cell.getValue());
                    case INGOT -> super.tag(Tags.Items.INGOTS).addTag(cell.getValue());
                    case NUGGET -> super.tag(Tags.Items.NUGGETS).addTag(cell.getValue());
                }

                super.tag(cell.getValue()).add(ModItems.ALL_ALLOYS.get(cell.getRowKey(), cell.getColumnKey()));
            }

            super.tag(COAL_GENERATOR_FUELS).add(Items.COAL, Items.CHARCOAL, Items.COAL_BLOCK);
        }
    }
}
