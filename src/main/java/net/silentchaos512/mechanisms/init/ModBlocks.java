package net.silentchaos512.mechanisms.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.silentchaos512.mechanisms.common.blocks.HarvestableBlock;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.common.blocks.generators.coalgenerator.CoalGeneratorBlock;
import net.silentchaos512.mechanisms.registration.DirectRegistry;

import java.util.Map;

//Seriously, pls do not re-format this class in your IDE, it will mess up the blocks categories
public final class ModBlocks {
    public static final DirectRegistry<Block> BLOCK_DIRECT_REGISTRY = new DirectRegistry<>();

    /*
        -----------------------  STANDALONE BLOCKS --------------------------
     */
    public static final DryingRackBlock OAK_DRYING_RACK = register("oak_drying_rack", new DryingRackBlock(Blocks.OAK_SLAB));
    public static final DryingRackBlock SPRUCE_DRYING_RANK = register("spruce_drying_rack", new DryingRackBlock(Blocks.SPRUCE_SLAB));
    public static final DryingRackBlock BIRCH_DRYING_RACK = register("birch_drying_rack", new DryingRackBlock(Blocks.BRICK_SLAB));
    public static final DryingRackBlock JUNGLE_DRYING_RACK = register("jungle_drying_rack", new DryingRackBlock(Blocks.JUNGLE_SLAB));
    public static final DryingRackBlock ACACIA_DRYING_RACK = register("acacia_drying_rack", new DryingRackBlock(Blocks.ACACIA_SLAB));
    public static final DryingRackBlock DARK_OAK_DRYING_RACK = register("dark_oak_drying_rack", new DryingRackBlock(Blocks.DARK_OAK_SLAB));
    public static final ImmutableList<DryingRackBlock> DRYING_RACK_BLOCKS = ImmutableList.of(OAK_DRYING_RACK, SPRUCE_DRYING_RANK, BIRCH_DRYING_RACK, JUNGLE_DRYING_RACK, ACACIA_DRYING_RACK, DARK_OAK_DRYING_RACK);

    public static final Block STONE_MACHINE_FRAME = register("stone_machine_frame", new HarvestableBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).sound(SoundType.STONE).noOcclusion(), BlockTags.MINEABLE_WITH_PICKAXE));
    public static final Block ALLOY_MACHINE_FRAME = register("alloy_machine_frame", new HarvestableBlock(BlockBehaviour.Properties.copy(STONE_MACHINE_FRAME), BlockTags.MINEABLE_WITH_PICKAXE));
    public static final CoalGeneratorBlock COAL_GENERATOR = register("coal_generator", new CoalGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6, 20).sound(SoundType.METAL)));

    /*
        -------------------- FLUID BLOCKS --------------------
     */
    //public static final BlockRegistryObject<LiquidBlock> FLUID_OIL = register("oil", () -> new LiquidBlock(ModFluids.OIL, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final LiquidBlock FLUID_OIL = register("oil", new LiquidBlock(() -> ModFluids.OIL, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final LiquidBlock FLUID_DIESEL = register("diesel", new LiquidBlock(() -> ModFluids.DIESEL, BlockBehaviour.Properties.copy(Blocks.WATER)));

    //public static final BlockRegistryObject<LiquidBlock> FLUID_DIESEL = register("diesel", () -> new LiquidBlock(ModFluids.DIESEL, BlockBehaviour.Properties.copy(Blocks.WATER)));
    /*
        -----------------------  BLOCKS COLLECTIONS --------------------------
        Mainly used for data gen
    */
    public static final Map<Metals.Ore, Block> ALL_ORE_BLOCKS = Maps.newHashMap();
    //missing alloys :P
    public static final Map<Metals.OreMetal, Block> ALL_STORAGE_BLOCKS = Maps.newHashMap();

    //this will add the alloys
    public static final Map<Metals.Alloy, Block> ALL_ALLOY_STORAGE_BLOCKS = Maps.newHashMap();


    /*
        =========================== HELPER METHODS & INSTANCES INITIALIZATION ===================================
     */

    static {

        for (Metals.OreMetal metal : Metals.OreMetal.values()) {
            ALL_STORAGE_BLOCKS.put(metal, register(metal.name().toLowerCase() + "_block", new HarvestableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), BlockTags.MINEABLE_WITH_PICKAXE, metal.getHarvestLevelTag())))
            ;
        }

        for (Metals.Ore ore : Metals.Ore.values()) {
            Block oreBlock = register(ore.name().toLowerCase() + "_ore", new HarvestableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE), BlockTags.MINEABLE_WITH_PICKAXE, ore.getHarvestLevelTag()));
            ALL_ORE_BLOCKS.put(ore, oreBlock);
        }
        for (Metals.Alloy alloy : Metals.Alloy.values()) {
            ALL_ALLOY_STORAGE_BLOCKS.put(alloy, register(alloy.name().toLowerCase() + "_block", new HarvestableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)))
            ;
        }
    }

    private static <BLOCK extends Block> BLOCK register(String name, BLOCK block) {
        return BLOCK_DIRECT_REGISTRY.register(name, block);
    }

    public static void startStaticInit() {}
}
