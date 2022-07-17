package net.silentchaos512.mechanisms.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.mechanisms.worldgen.OreVeinValues;

public final class Metals {
    private Metals() {
    }

    public enum Alloy {
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
    }

    public enum OreMetal {
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

        public TagKey<Block> getHarvestLevelTag() {
            return harvestLevelTag;
        }
    }

    public enum OreMetalType {
        INGOT,
        DUST,
        CHUNKS,
        NUGGET;
    }

    //1.18 introduced copper so copper in the mod is removed
    public enum Ore {
        TIN(OreMetal.TIN, BlockTags.NEEDS_STONE_TOOL, new OreVeinValues(8, 8, 20, 80)),
        SILVER(OreMetal.SILVER, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(4, 8, 0, 40)),
        LEAD(OreMetal.LEAD, BlockTags.NEEDS_IRON_TOOL,new OreVeinValues(4, 8, 0, 30)),
        NICKEL(OreMetal.NICKEL, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(1, 6, 0, 24)),
        PLATINUM(OreMetal.PLATINUM, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(1, 8, 5, 20)),
        ZINC(OreMetal.ZINC, BlockTags.NEEDS_STONE_TOOL, new OreVeinValues(4, 8, 20, 60)),
        BISMUTH(OreMetal.BISMUTH, BlockTags.NEEDS_STONE_TOOL, new OreVeinValues(4, 8, 16, 64)),
        BAUXITE(OreMetal.ALUMINUM, BlockTags.NEEDS_STONE_TOOL, new OreVeinValues(6, 8, 25, 50)),
        URANIUM(OreMetal.URANIUM, BlockTags.NEEDS_IRON_TOOL, new OreVeinValues(1, 4, 0, 18));

        public final TagKey<Block> harvestLevelTag;
        public final OreVeinValues oreVeinValues;
        public final OreMetal respectiveMetal;

        Ore(OreMetal respectiveMetal, TagKey<Block> harvestLeveltag, OreVeinValues oreVeinValues) {
            this.respectiveMetal = respectiveMetal;
            this.harvestLevelTag = harvestLeveltag;
            this.oreVeinValues = oreVeinValues;
        }

        public TagKey<Block> getHarvestLevelTag() {
            return harvestLevelTag;
        }

        public OreVeinValues getOreVeinValues() {
            return oreVeinValues;
        }

        public OreMetal getRespectiveMetal() {
            return respectiveMetal;
        }

        public Item getChunkItem() {
            return ModItems.ALL_ORE_METALS.get(respectiveMetal, OreMetalType.CHUNKS).get();
        }
    }

    public enum AlloyType {
        DUST,
        INGOT,
        NUGGET;
    }
}
