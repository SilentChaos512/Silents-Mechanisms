package net.silentchaos512.mechanisms.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.mechanisms.SilentsMechanisms;

import java.util.HashMap;
import java.util.Map;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SilentsMechanisms.MODID);
    public static final Map<Metals.Ore, BlockRegistryObject<Block>> ALL_ORE_BLOCKS;
    public static final Map<Metals.OreMetal, BlockRegistryObject<Block>> ALL_STORAGE_BLOCKS;

    static {
        ALL_ORE_BLOCKS = new HashMap<>();
        ALL_STORAGE_BLOCKS = new HashMap<>();
        for (Metals.OreMetal metal : Metals.OreMetal.values()) {
            ALL_STORAGE_BLOCKS.put(metal, new BlockRegistryObject<>(BLOCKS.register(metal.name().toLowerCase() + "_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)))));
        }

        for (Metals.Ore ore : Metals.Ore.values()) {
            BlockRegistryObject<Block> oreBlock = new BlockRegistryObject<>(BLOCKS.register(ore.name().toLowerCase() + "_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE))));
            ALL_ORE_BLOCKS.put(ore, oreBlock);
        }

    }

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
