package net.silentchaos512.mechanisms.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.mechanisms.worldgen.OreGenValues;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class Metals {
    private Metals() {
    }

    public enum Material {
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

        Material() {
        }
    }

    public enum OreMetal {
        TIN,
        SILVER,
        LEAD,
        NICKEL,
        PLATINUM,
        ZINC,
        BISMUTH,
        ALUMINUM,
        URANIUM;
    }

    public enum OreMetalType {
        INGOT,
        DUST,
        CHUNKS,
        NUGGET;
    }

    //1.18 introduced copper so copper in the mod is removed
    public enum Ore {
        TIN(BlockTags.NEEDS_STONE_TOOL, new OreGenValues(8, 8, 20, 80)),
        SILVER(BlockTags.NEEDS_IRON_TOOL, new OreGenValues(4, 8, 0, 40)),
        LEAD(BlockTags.NEEDS_IRON_TOOL,new OreGenValues(4, 8, 0, 30)),
        NICKEL(BlockTags.NEEDS_IRON_TOOL, new OreGenValues(1, 6, 0, 24)),
        PLATINUM(BlockTags.NEEDS_IRON_TOOL, new OreGenValues(1, 8, 5, 20)),
        ZINC(BlockTags.NEEDS_STONE_TOOL, new OreGenValues(4, 8, 20, 60)),
        BISMUTH(BlockTags.NEEDS_STONE_TOOL, new OreGenValues(4, 8, 16, 64)),
        BAUXITE(BlockTags.NEEDS_STONE_TOOL, new OreGenValues(6, 8, 25, 50)),
        URANIUM(BlockTags.NEEDS_IRON_TOOL, new OreGenValues(1, 4, 0, 18));

        public final TagKey<Block> tookLevelTag;
        public final OreGenValues oreGenValues;
        @Nullable private BlockRegistryObject<Block> oreBlock;

        Ore(TagKey<Block> tookLevelTag, OreGenValues oreGenValues) {
            this.tookLevelTag = tookLevelTag;
            this.oreGenValues = oreGenValues;
        }

        @Nullable
        public BlockRegistryObject<Block> getOreBlock() {
            return oreBlock;
        }

        public void setOreBlock(@Nonnull BlockRegistryObject<Block> oreBlock) {
            this.oreBlock = oreBlock;
        }
    }

    public enum Type {
        DUST,
        INGOT,
        NUGGET;
    }
}
