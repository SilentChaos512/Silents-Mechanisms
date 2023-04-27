package net.silentchaos512.mechanisms.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.worldgen.OreVeinValues;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public final class Metals {
    private Metals() {
    }

    public enum Alloy implements StorageBlockProvider {
        REDSTONE_ALLOY,
        BRONZE,
        BRASS,
        INVAR,
        ELECTRUM,
        STEEL,
        BISMUTH_BRASS,
        ALUMINUM_STEEL,
        SIGNALUM,
        LUMIUM,
        ENDERIUM;

        Alloy() {
        }

        @Override
        public Item getIngot() {
            return ModItems.ALL_ALLOYS.get(this, AlloyType.INGOT);
        }

        @Override
        public Item getNugget() {
            return ModItems.ALL_ALLOYS.get(this, AlloyType.NUGGET);
        }

        @Override
        public Item getDust() {
            return ModItems.ALL_ALLOYS.get(this, AlloyType.DUST);
        }

        @Override
        public TagKey<Block> getHarvestToolTag() {
            return BlockTags.NEEDS_IRON_TOOL;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ROOT);
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public enum OreMetal implements StorageBlockProvider {
        TIN(BlockTags.NEEDS_STONE_TOOL),
        SILVER(BlockTags.NEEDS_IRON_TOOL),
        LEAD(BlockTags.NEEDS_IRON_TOOL),
        NICKEL(BlockTags.NEEDS_IRON_TOOL),
        PLATINUM(BlockTags.NEEDS_IRON_TOOL),
        ZINC(BlockTags.NEEDS_STONE_TOOL),
        BISMUTH(BlockTags.NEEDS_STONE_TOOL),
        ALUMINUM(BlockTags.NEEDS_STONE_TOOL),
        URANIUM(BlockTags.NEEDS_IRON_TOOL);

        private final TagKey<Block> harvestLevelTag;

        OreMetal(TagKey<Block> harvestLevelTag) {
            this.harvestLevelTag = harvestLevelTag;
        }

        @Override
        public Item getIngot() {
            return ModItems.ALL_ORE_METALS.get(this, OreMetalType.INGOT);
        }

        @Override
        public Item getNugget() {
            return ModItems.ALL_ORE_METALS.get(this, OreMetalType.NUGGET);
        }

        @Override
        public Item getDust() {
            return ModItems.ALL_ORE_METALS.get(this, OreMetalType.DUST);
        }

        @Override
        public TagKey<Block> getHarvestToolTag() {
            return this.harvestLevelTag;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ROOT);
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public enum OreMetalType {
        INGOT,
        DUST,
        RAW,
        NUGGET;

        public String getName(OreMetal metal) {
            if (this == RAW) {
                if (metal == OreMetal.ALUMINUM) {
                    return "raw_bauxite";
                }
                return "raw_" + metal.getName();
            }
            return metal.getName() + "_" + name().toLowerCase(Locale.ROOT);
        }
    }

    public enum Ore {
        TIN(OreMetal.TIN, BlockTags.NEEDS_STONE_TOOL, new OreVeinValues(8, 8, 20, 80)),
        SILVER(OreMetal.SILVER, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(4, 8, -40, 40)),
        LEAD(OreMetal.LEAD, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(4, 8, -50, 30)),
        NICKEL(OreMetal.NICKEL, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(1, 6, -60, 24)),
        PLATINUM(OreMetal.PLATINUM, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(1, 8, -60, 20)),
        ZINC(OreMetal.ZINC, BlockTags.NEEDS_STONE_TOOL, new OreVeinValues(4, 8, 0, 60)),
        BISMUTH(OreMetal.BISMUTH, BlockTags.NEEDS_STONE_TOOL, new OreVeinValues(4, 8, -20, 64)),
        BAUXITE(OreMetal.ALUMINUM, BlockTags.NEEDS_STONE_TOOL, new OreVeinValues(6, 8, 0, 50)),
        URANIUM(OreMetal.URANIUM, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(1, 4, -60, 0));

        public final TagKey<Block> harvestLevelTag;
        public final OreVeinValues oreVeinValues;
        public final OreMetal respectiveMetal;

        public final ResourceKey<ConfiguredFeature<?, ?>> featureConfigKey;
        public final ResourceKey<PlacedFeature> placedFeatureKey;

        Ore(OreMetal respectiveMetal, TagKey<Block> harvestLevelTag, OreVeinValues oreVeinValues) {
            this.respectiveMetal = respectiveMetal;
            this.harvestLevelTag = harvestLevelTag;
            this.oreVeinValues = oreVeinValues;
            this.featureConfigKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, SilentsMechanisms.location(this + "_ores_config"));
            this.placedFeatureKey = ResourceKey.create(Registries.PLACED_FEATURE,  SilentsMechanisms.location(this + "_ores"));
        }

        public ResourceKey<ConfiguredFeature<?, ?>> getFeatureConfigKey() {
            return featureConfigKey;
        }

        public ResourceKey<PlacedFeature> getPlacedFeatureKey() {
            return placedFeatureKey;
        }

        public TagKey<Block> getHarvestLevelTag() {
            return harvestLevelTag;
        }

        public OreVeinValues getOreVeinValues() {
            return oreVeinValues;
        }

        public Block getOreBlock() {
            return ModBlocks.ALL_ORE_BLOCKS.get(this);
        }

        public Block getDeepslateOreBlock() {
            return ModBlocks.ALL_DEEPSLATE_ORE_BLOCKS.get(this);
        }

        public Item getRawOreItem() {
            return ModItems.ALL_ORE_METALS.get(respectiveMetal, OreMetalType.RAW);
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ROOT);
        }
    }

    public enum AlloyType {
        DUST,
        INGOT,
        NUGGET;

        public String getName(Alloy metal) {
            return metal.getName() + "_" + name().toLowerCase(Locale.ROOT);
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ROOT);
        }
    }

    public interface StorageBlockProvider {
        List<StorageBlockProvider> ALL_PROVIDERS = new LinkedList<>();

        Item getIngot();

        Item getNugget();

        Item getDust();

        TagKey<Block> getHarvestToolTag();

        String toString();

        default Block getStorageBlock() {
            return ModBlocks.METAL_STORAGE_BLOCKS.get(this);
        }

        String getName();

    }
}
