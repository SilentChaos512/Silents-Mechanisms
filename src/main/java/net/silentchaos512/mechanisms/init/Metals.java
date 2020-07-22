package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.MetalBlock;
import net.silentchaos512.mechanisms.block.OreBlockSM;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum Metals {
    REDSTONE_ALLOY(builderAlloy("redstone_alloy")),
    REFINED_IRON(builder("refined_iron").ingot()),
    COMPRESSED_IRON(builder("compressed_iron").ingot()),
    IRON(builder("iron").chunks().dust().ingotTagOnly().nuggetTagOnly()),
    GOLD(builder("gold").chunks().dust().ingotTagOnly().nuggetTagOnly()),
    COPPER(builderBaseWithOre("copper", Ores.COPPER)),
    TIN(builderBaseWithOre("tin", Ores.TIN)),
    SILVER(builderBaseWithOre("silver", Ores.SILVER)),
    LEAD(builderBaseWithOre("lead", Ores.LEAD)),
    NICKEL(builderBaseWithOre("nickel", Ores.NICKEL)),
    PLATINUM(builderBaseWithOre("platinum", Ores.PLATINUM)),
    ZINC(builderBaseWithOre("zinc", Ores.ZINC)),
    BISMUTH(builderBaseWithOre("bismuth", Ores.BISMUTH)),
    ALUMINUM(builderBaseWithOre("aluminum", Ores.BAUXITE), "bauxite"),
    URANIUM(builderBaseWithOre("uranium", Ores.URANIUM)),
    BRONZE(builderAlloy("bronze")),
    BRASS(builderAlloy("brass")),
    INVAR(builderAlloy("invar")),
    ELECTRUM(builderAlloy("electrum")),
    STEEL(builderAlloy("steel")),
    BISMUTH_BRASS(builderAlloy("bismuth_brass")),
    ALUMINUM_STEEL(builderAlloy("aluminum_steel")),
    BISMUTH_STEEL(builderAlloy("bismuth_steel")),
    SIGNALUM(builderAlloy("signalum")),
    LUMIUM(builderAlloy("lumium")),
    ENDERIUM(builderAlloy("enderium")),
    ;

    private final String oreName;
    @SuppressWarnings("NonFinalFieldInEnum") private BlockRegistryObject<Block> ore;
    @SuppressWarnings("NonFinalFieldInEnum") private BlockRegistryObject<Block> storageBlock;
    @SuppressWarnings("NonFinalFieldInEnum") private ItemRegistryObject<Item> chunks;
    @SuppressWarnings("NonFinalFieldInEnum") private ItemRegistryObject<Item> dust;
    @SuppressWarnings("NonFinalFieldInEnum") private ItemRegistryObject<Item> ingot;
    @SuppressWarnings("NonFinalFieldInEnum") private ItemRegistryObject<Item> nugget;
    private final Supplier<Block> oreSupplier;
    private final Supplier<Block> storageBlockSupplier;
    private final Supplier<Item> chunksSupplier;
    private final Supplier<Item> dustSupplier;
    private final Supplier<Item> ingotSupplier;
    private final Supplier<Item> nuggetSupplier;
    private final ITag.INamedTag<Block> storageBlockTag;
    private final ITag.INamedTag<Block> oreTag;
    private final ITag.INamedTag<Item> storageBlockItemTag;
    private final ITag.INamedTag<Item> oreItemTag;
    private final ITag.INamedTag<Item> chunksTag;
    private final ITag.INamedTag<Item> dustTag;
    private final ITag.INamedTag<Item> ingotTag;
    private final ITag.INamedTag<Item> nuggetTag;

    Metals(Builder builder) {
        this(builder, builder.name);
    }

    Metals(Builder builder, String oreName) {
        if (!builder.name.equals(this.getName())) {
            throw new IllegalArgumentException("Builder name is incorrect, should be " + this.getName());
        }
        this.oreName = oreName;
        this.storageBlockSupplier = builder.storageBlock;
        this.oreSupplier = builder.ore;
        this.chunksSupplier = builder.chunks;
        this.dustSupplier = builder.dust;
        this.ingotSupplier = builder.ingot;
        this.nuggetSupplier = builder.nugget;
        this.oreTag = builder.oreTag;
        this.storageBlockTag = builder.storageBlockTag;
        this.oreItemTag = this.oreTag != null ? Builder.itemTag(this.oreTag.getName()) : null;
        this.storageBlockItemTag = this.storageBlockTag != null ? Builder.itemTag(this.storageBlockTag.getName()) : null;
        this.chunksTag = builder.chunksTag;
        this.dustTag = builder.dustTag;
        this.ingotTag = builder.ingotTag;
        this.nuggetTag = builder.nuggetTag;
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Optional<Block> getOre() {
        return ore != null ? Optional.of(ore.get()) : Optional.empty();
    }

    public Optional<Block> getStorageBlock() {
        return storageBlock != null ? Optional.of(storageBlock.get()) : Optional.empty();
    }

    public Optional<Item> getChunks() {
        return chunks != null ? Optional.of(chunks.get()) : Optional.empty();
    }

    public Optional<Item> getDust() {
        return dust != null ? Optional.of(dust.get()) : Optional.empty();
    }

    public Optional<Item> getIngot() {
        return ingot != null ? Optional.of(ingot.get()) : Optional.empty();
    }

    public Optional<Item> getNugget() {
        return nugget != null ? Optional.of(nugget.get()) : Optional.empty();
    }

    public Optional<ITag.INamedTag<Block>> getOreTag() {
        return oreTag != null ? Optional.of(oreTag) : Optional.empty();
    }

    public Optional<ITag.INamedTag<Block>> getStorageBlockTag() {
        return storageBlockTag != null ? Optional.of(storageBlockTag) : Optional.empty();
    }

    public Optional<ITag.INamedTag<Item>> getOreItemTag() {
        return oreItemTag != null ? Optional.of(oreItemTag) : Optional.empty();
    }

    public Optional<ITag.INamedTag<Item>> getStorageBlockItemTag() {
        return storageBlockItemTag != null ? Optional.of(storageBlockItemTag) : Optional.empty();
    }

    public Optional<ITag.INamedTag<Item>> getChunksTag() {
        return chunksTag != null ? Optional.of(chunksTag) : Optional.empty();
    }

    public Optional<ITag.INamedTag<Item>> getDustTag() {
        return dustTag != null ? Optional.of(dustTag) : Optional.empty();
    }

    public Optional<ITag.INamedTag<Item>> getIngotTag() {
        return ingotTag != null ? Optional.of(ingotTag) : Optional.empty();
    }

    public Optional<ITag.INamedTag<Item>> getNuggetTag() {
        return nuggetTag != null ? Optional.of(nuggetTag) : Optional.empty();
    }

    public Ingredient getSmeltables() {
        return getSmeltables(true);
    }

    public Ingredient getSmeltables(boolean includeIngot) {
        Stream.Builder<ITag.INamedTag<Item>> builder = Stream.builder();
        if (includeIngot) {
            getIngotTag().ifPresent(builder::add);
        }
        getChunksTag().ifPresent(builder::add);
        getDustTag().ifPresent(builder::add);
        return Ingredient.fromItemListStream(builder.build().map(Ingredient.TagList::new));
    }

    public static void registerBlocks() {
        for (Metals metal : values()) {
            if (metal.oreSupplier != null) {
                String name = metal.oreName + "_ore";
                metal.ore = new BlockRegistryObject<>(Registration.BLOCKS.register(name, metal.oreSupplier));
                Registration.ITEMS.register(name, () ->
                        new BlockItem(metal.ore.get(), new Item.Properties().group(SilentMechanisms.ITEM_GROUP)));
            }
        }
        for (Metals metal : values()) {
            if (metal.storageBlockSupplier != null) {
                String name = metal.getName() + "_block";
                metal.storageBlock = new BlockRegistryObject<>(Registration.BLOCKS.register(name, metal.storageBlockSupplier));
                Registration.ITEMS.register(name, () ->
                        new BlockItem(metal.storageBlock.get(), new Item.Properties().group(SilentMechanisms.ITEM_GROUP)));
            }
        }
    }

    public static void registerItems() {
        for (Metals metal : values()) {
            if (metal.chunksSupplier != null) {
                metal.chunks = new ItemRegistryObject<>(Registration.ITEMS.register(
                        metal.oreName + "_chunks", metal.chunksSupplier));
            }
            if (metal.dustSupplier != null) {
                metal.dust = new ItemRegistryObject<>(Registration.ITEMS.register(
                        metal.getName() + "_dust", metal.dustSupplier));
            }
            if (metal.ingotSupplier != null) {
                metal.ingot = new ItemRegistryObject<>(Registration.ITEMS.register(
                        metal.getName() + "_ingot", metal.ingotSupplier));
            }
            if (metal.nuggetSupplier != null) {
                metal.nugget = new ItemRegistryObject<>(Registration.ITEMS.register(
                        metal.getName() + "_nugget", metal.nuggetSupplier));
            }
        }
    }

    private static Builder builder(String name) {
        return new Builder(name);
    }

    private static Builder builderBaseWithOre(String name, Ores ore) {
        return builder(name).storageBlock().ore(ore).chunks().dust().ingot().nugget();
    }

    private static Builder builderAlloy(String name) {
        return builder(name).storageBlock().dust().ingot().nugget();
    }

    private static class Builder {
        final String name;
        Supplier<Block> ore;
        Supplier<Block> storageBlock;
        Supplier<Item> chunks;
        Supplier<Item> dust;
        Supplier<Item> ingot;
        Supplier<Item> nugget;
        ITag.INamedTag<Block> oreTag;
        ITag.INamedTag<Block> storageBlockTag;
        ITag.INamedTag<Item> chunksTag;
        ITag.INamedTag<Item> dustTag;
        ITag.INamedTag<Item> ingotTag;
        ITag.INamedTag<Item> nuggetTag;

        Builder(String name) {
            this.name = name;
        }

        Builder ore(Ores ore) {
            this.ore = () -> new OreBlockSM(ore.getHardness(), ore.getHarvestLevel());
            this.oreTag = blockTag("ores/" + name);
            return this;
        }

        Builder storageBlock() {
            this.storageBlock = MetalBlock::new;
            this.storageBlockTag = blockTag("storage_blocks/" + name);
            return this;
        }

        Builder chunks() {
            this.chunks = () -> new Item(new Item.Properties().group(SilentMechanisms.ITEM_GROUP));
            this.chunksTag = itemTag(SilentMechanisms.getId("chunks/" + name));
            return this;
        }

        Builder dust() {
            this.dust = () -> new Item(new Item.Properties().group(SilentMechanisms.ITEM_GROUP));
            this.dustTag = itemTag("dusts/" + name);
            return this;
        }

        Builder ingot() {
            this.ingot = () -> new Item(new Item.Properties().group(SilentMechanisms.ITEM_GROUP));
            this.ingotTag = itemTag("ingots/" + name);
            return this;
        }

        Builder ingotTagOnly() {
            this.ingotTag = itemTag("ingots/" + name);
            return this;
        }

        Builder nugget() {
            this.nugget = () -> new Item(new Item.Properties().group(SilentMechanisms.ITEM_GROUP));
            this.nuggetTag = itemTag("nuggets/" + name);
            return this;
        }

        Builder nuggetTagOnly() {
            this.nuggetTag = itemTag("nuggets/" + name);
            return this;
        }

        private static ITag.INamedTag<Block> blockTag(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> itemTag(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> itemTag(ResourceLocation tag) {
            return ItemTags.makeWrapperTag(tag.toString());
        }
    }
}
