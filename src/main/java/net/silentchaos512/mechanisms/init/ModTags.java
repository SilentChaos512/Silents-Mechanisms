package net.silentchaos512.mechanisms.init;

import net.silentchaos512.mechanisms.SilentMechanisms;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static final class Blocks {
        // This will be "forge:ores/example"
        public static final Tag<Block> EXAMPLE = tag("forge", "ores/example");
        // Default namespace is mod ID, so this one will be "silents_mechanisms:example_block"
        public static final Tag<Block> EXAMPLE2 = tag("example_block");

        private Blocks() {}

        private static Tag<Block> tag(String name) {
            return new BlockTags.Wrapper(new ResourceLocation(SilentMechanisms.MOD_ID, name));
        }

        private static Tag<Block> tag(String namespace, String name) {
            return new BlockTags.Wrapper(new ResourceLocation(namespace, name));
        }
    }

    public static final class Items {
        public static final Tag<Item> COAL_GENERATOR_FUELS = tag("coal_generator_fuels");

        private Items() {}

        private static Tag<Item> tag(String name) {
            return new ItemTags.Wrapper(new ResourceLocation(SilentMechanisms.MOD_ID, name));
        }

        private static Tag<Item> tag(String namespace, String name) {
            return new ItemTags.Wrapper(new ResourceLocation(namespace, name));
        }
    }
}
