package net.silentchaos512.mechanisms.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.silentchaos512.mechanisms.common.blocks.HarvestableBlock;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.common.blocks.generators.coalgenerator.CoalGeneratorBlock;
import net.silentchaos512.mechanisms.registration.DirectRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

//Seriously, pls do not re-format this class in your IDE, it will mess up the blocks categories
@SuppressWarnings({"unused"})
public final class ModBlocks {
    public static final DirectRegistry<Block> BLOCK_DIRECT_REGISTRY = new DirectRegistry<>();
    /*
           -----------------------  BLOCKS COLLECTIONS --------------------------
           Mainly used for data gen
    */
    public static final Map<Metals.Ore, Block> ALL_ORE_BLOCKS = new LinkedHashMap<>();
    public static final Map<Metals.Ore, Block> ALL_DEEPSLATE_ORE_BLOCKS = new LinkedHashMap<>();
    public static final Map<Metals.StorageBlockProvider, Block> METAL_STORAGE_BLOCKS = new LinkedHashMap<>();

    /*
        -----------------------  STANDALONE BLOCKS --------------------------
     */
    public static final DryingRackBlock OAK_DRYING_RACK = register("oak_drying_rack", new DryingRackBlock(Blocks.OAK_SLAB));
    public static final DryingRackBlock SPRUCE_DRYING_RANK = register("spruce_drying_rack", new DryingRackBlock(Blocks.SPRUCE_SLAB));
    public static final DryingRackBlock BIRCH_DRYING_RACK = register("birch_drying_rack", new DryingRackBlock(Blocks.BRICK_SLAB));
    public static final DryingRackBlock JUNGLE_DRYING_RACK = register("jungle_drying_rack", new DryingRackBlock(Blocks.JUNGLE_SLAB));
    public static final DryingRackBlock ACACIA_DRYING_RACK = register("acacia_drying_rack", new DryingRackBlock(Blocks.ACACIA_SLAB));
    public static final DryingRackBlock DARK_OAK_DRYING_RACK = register("dark_oak_drying_rack", new DryingRackBlock(Blocks.DARK_OAK_SLAB));

    public static final Block STONE_MACHINE_FRAME = register("stone_machine_frame", new HarvestableBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).sound(SoundType.STONE).noOcclusion(), BlockTags.MINEABLE_WITH_PICKAXE));
    public static final Block ALLOY_MACHINE_FRAME = register("alloy_machine_frame", new HarvestableBlock(BlockBehaviour.Properties.copy(STONE_MACHINE_FRAME), BlockTags.MINEABLE_WITH_PICKAXE));
    public static final CoalGeneratorBlock COAL_GENERATOR = register("coal_generator", new CoalGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6, 20).sound(SoundType.METAL)));

    public static final Block TIN_BLOCK = storageBlock(Metals.OreMetal.TIN);
    public static final Block SILVER_BLOCK = storageBlock(Metals.OreMetal.SILVER);
    public static final Block LEAD_BLOCK = storageBlock(Metals.OreMetal.LEAD);
    public static final Block NICKEL_BLOCK = storageBlock(Metals.OreMetal.NICKEL);
    public static final Block PLATINUM_BLOCK = storageBlock(Metals.OreMetal.PLATINUM);
    public static final Block ZINC_BLOCK = storageBlock(Metals.OreMetal.ZINC);
    public static final Block BISMUTH_BLOCK = storageBlock(Metals.OreMetal.BISMUTH);
    public static final Block ALUMINUM_BLOCK = storageBlock(Metals.OreMetal.ALUMINUM);
    public static final Block URANIUM_BLOCK = storageBlock(Metals.OreMetal.URANIUM);
    public static final Block REDSTONE_ALLOY_BLOCK = storageBlock(Metals.Alloy.REDSTONE_ALLOY);
    public static final Block BRONZE_BLOCK = storageBlock(Metals.Alloy.BRONZE);
    public static final Block BRASS_BLOCK = storageBlock(Metals.Alloy.BRASS);
    public static final Block INVAR_BLOCK = storageBlock(Metals.Alloy.INVAR);
    public static final Block ELECTRUM_BLOCK = storageBlock(Metals.Alloy.ELECTRUM);
    public static final Block STEEL_BLOCK = storageBlock(Metals.Alloy.STEEL);
    public static final Block BISMUTH_BRASS_BLOCK = storageBlock(Metals.Alloy.BISMUTH_BRASS);
    public static final Block ALUMINUM_STEEL_BLOCK = storageBlock(Metals.Alloy.ALUMINUM_STEEL);
    public static final Block SIGNALUM_BLOCK = storageBlock(Metals.Alloy.SIGNALUM);
    public static final Block LUMIUM_BLOCK = storageBlock(Metals.Alloy.LUMIUM);
    public static final Block ENDERIUM_BLOCK = storageBlock(Metals.Alloy.ENDERIUM);

    public static final Block TIN_ORE = registerOre(Metals.Ore.TIN);
    public static final Block SILVER_ORE = registerOre(Metals.Ore.SILVER);
    public static final Block LEAD_ORE = registerOre(Metals.Ore.LEAD);
    public static final Block NICKEL_ORE = registerOre(Metals.Ore.NICKEL);
    public static final Block ZINC_ORE = registerOre(Metals.Ore.ZINC);
    public static final Block PLATINUM_ORE = registerOre(Metals.Ore.PLATINUM);
    public static final Block BISMUTH_ORE = registerOre(Metals.Ore.BISMUTH);
    public static final Block BAUXITE_ORE = registerOre(Metals.Ore.BAUXITE);
    public static final Block URANIUM_ORE = registerOre(Metals.Ore.URANIUM);

    public static final Block DEEPSLATE_TIN_ORE = registerDeepslateOre(Metals.Ore.TIN);
    public static final Block DEEPSLATE_SILVER_ORE = registerDeepslateOre(Metals.Ore.SILVER);
    public static final Block DEEPSLATE_LEAD_ORE = registerDeepslateOre(Metals.Ore.LEAD);
    public static final Block DEEPSLATE_NICKEL_ORE = registerDeepslateOre(Metals.Ore.NICKEL);
    public static final Block DEEPSLATE_ZINC_ORE = registerDeepslateOre(Metals.Ore.ZINC);
    public static final Block DEEPSLATE_PLATINUM_ORE = registerDeepslateOre(Metals.Ore.PLATINUM);
    public static final Block DEEPSLATE_BISMUTH_ORE = registerDeepslateOre(Metals.Ore.BISMUTH);
    public static final Block DEEPSLATE_BAUXITE_ORE = registerDeepslateOre(Metals.Ore.BAUXITE);
    public static final Block DEEPSLATE_URANIUM_ORE = registerDeepslateOre(Metals.Ore.URANIUM);

    /*
        -------------------- FLUID BLOCKS --------------------
     */
    //public static final BlockRegistryObject<LiquidBlock> FLUID_OIL = register("oil", () -> new LiquidBlock(ModFluids.OIL, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final LiquidBlock FLUID_OIL = register("oil", new LiquidBlock(() -> ModFluids.OIL, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final LiquidBlock FLUID_DIESEL = register("diesel", new LiquidBlock(() -> ModFluids.DIESEL, BlockBehaviour.Properties.copy(Blocks.WATER)));
    //public static final BlockRegistryObject<LiquidBlock> FLUID_DIESEL = register("diesel", () -> new LiquidBlock(ModFluids.DIESEL, BlockBehaviour.Properties.copy(Blocks.WATER)));

    /*
        =========================== HELPER METHODS & INSTANCES INITIALIZATION ===================================
     */

    private static Block registerOre(Metals.Ore metal) {
        Block oreBlock = register(metal.toString() + "_ore", new HarvestableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE), metal.getHarvestLevelTag()));
        ALL_ORE_BLOCKS.put(metal, oreBlock);
        return oreBlock;
    }

    private static Block registerDeepslateOre(Metals.Ore metal) {
        Block oreBlock = register("deepslate_" + metal.toString() + "_ore",
                new HarvestableBlock(
                        BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                                .color(MaterialColor.DEEPSLATE)
                                .strength(4.5f, 3.0f)
                                .sound(SoundType.DEEPSLATE),
                        metal.getHarvestLevelTag()
                )
        );
        ALL_DEEPSLATE_ORE_BLOCKS.put(metal, oreBlock);
        return oreBlock;
    }

    private static Block storageBlock(Metals.StorageBlockProvider metal) {
        Block storageBlock = register(metal.getName() + "_block", new HarvestableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), metal.getHarvestToolTag()));
        return METAL_STORAGE_BLOCKS.put(metal, storageBlock);
    }

    private static <BLOCK extends Block> BLOCK register(String name, BLOCK block) {
        return BLOCK_DIRECT_REGISTRY.register(name, block);
    }

    public static void startStaticInit() {
    }
}
