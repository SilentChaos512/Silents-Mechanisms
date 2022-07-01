package net.silentchaos512.mechanisms.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.blocks.dryingracks.DryingRackBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SilentsMechanisms.MODID);

    /*
        -----------------------  STANDALONE BLOCKS --------------------------
     */

    public static final BlockRegistryObject<DryingRackBlock> OAK_DRYING_RACK = register("oak_drying_rack", () -> new DryingRackBlock(Blocks.OAK_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> SPRUCE_DRYING_RANK = register("spruce_drying_rack", () -> new DryingRackBlock(Blocks.SPRUCE_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> BIRCH_DRYING_RACK = register("birch_drying_rack", () -> new DryingRackBlock(Blocks.BRICK_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> JUNGLE_DRYING_RACK = register("jungle_drying_rack",() -> new DryingRackBlock(Blocks.JUNGLE_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> ACACIA_DRYING_RACK = register("acacia_drying_rack", () -> new DryingRackBlock(Blocks.ACACIA_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> DARK_OAK_DRYING_RACK = register("dark_oak_drying_rack", () -> new DryingRackBlock(Blocks.DARK_OAK_SLAB));

    /*
        -----------------------  BLOCKS COLLECTIONS --------------------------
        Mainly used for data gen
    */
    public static final Map<Metals.Ore, BlockRegistryObject<Block>> ALL_ORE_BLOCKS;
    //missing alloys :P
    public static final Map<Metals.OreMetal, BlockRegistryObject<Block>> ALL_STORAGE_BLOCKS;
    //this will add the alloys
    public static final Map<Metals.Alloy, BlockRegistryObject<Block>> ALL_ALLOY_STORAGE_BLOCKS;
    public static final ImmutableList<BlockRegistryObject<DryingRackBlock>> DRYING_RACK_BLOCKS = ImmutableList.of(OAK_DRYING_RACK, SPRUCE_DRYING_RANK, BIRCH_DRYING_RACK, JUNGLE_DRYING_RACK, ACACIA_DRYING_RACK, DARK_OAK_DRYING_RACK);



    private static <BLOCK extends Block> BlockRegistryObject<BLOCK> register(String name, Supplier<BLOCK> blockSupplier) {
        return new BlockRegistryObject<>(BLOCKS.register(name, blockSupplier));
    };

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
        ALL_ALLOY_STORAGE_BLOCKS = new HashMap<>();
        for (Metals.Alloy alloy : Metals.Alloy.values()) {
            ALL_ALLOY_STORAGE_BLOCKS.put(alloy, new BlockRegistryObject<>(BLOCKS.register(alloy.name().toLowerCase() + "_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)))));
        }
    }

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
