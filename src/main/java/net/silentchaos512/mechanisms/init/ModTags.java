package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.mechanisms.SilentMechanisms;

public class ModTags {
    public static final class Blocks {
        public static final Tag<Block> DRYING_RACKS = mod("drying_racks");

        private Blocks() {}

        private static Tag<Block> forge(String path) {
            return new BlockTags.Wrapper(forgeId(path));
        }

        private static Tag<Block> mod(String path) {
            return new BlockTags.Wrapper(modId(path));
        }
    }

    public static final class Items {
        public static final Tag<Item> CHUNKS = mod("chunks");
        public static final Tag<Item> COAL_GENERATOR_FUELS = mod("coal_generator_fuels");
        public static final Tag<Item> DRYING_RACKS = mod("drying_racks");
        public static final Tag<Item> DUSTS_COAL = forge("dusts/coal");
        public static final Tag<Item> STEELS = mod("ingots/steels");

        private Items() {}

        private static Tag<Item> forge(String path) {
            return new ItemTags.Wrapper(forgeId(path));
        }

        private static Tag<Item> mod(String path) {
            return new ItemTags.Wrapper(modId(path));
        }
    }

    private static ResourceLocation forgeId(String path) {
        return new ResourceLocation("forge", path);
    }

    private static ResourceLocation modId(String path) {
        return new ResourceLocation(SilentMechanisms.MOD_ID, path);
    }
}
