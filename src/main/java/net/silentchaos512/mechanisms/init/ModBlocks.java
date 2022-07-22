package net.silentchaos512.mechanisms.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.blocks.HarvestableBlock;
import net.silentchaos512.mechanisms.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.blocks.generators.coalgenerator.CoalGeneratorBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//Seriously, pls do not re-format this class in your IDE, it will mess up the blocks categories
@SuppressWarnings("deprecation")
public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SilentsMechanisms.MODID);

    /*
        -----------------------  STANDALONE BLOCKS --------------------------
     */
    public static final BlockRegistryObject<DryingRackBlock> OAK_DRYING_RACK = register("oak_drying_rack", () -> new DryingRackBlock(Blocks.OAK_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> SPRUCE_DRYING_RANK = register("spruce_drying_rack", () -> new DryingRackBlock(Blocks.SPRUCE_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> BIRCH_DRYING_RACK = register("birch_drying_rack", () -> new DryingRackBlock(Blocks.BRICK_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> JUNGLE_DRYING_RACK = register("jungle_drying_rack",() -> new DryingRackBlock(Blocks.JUNGLE_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> ACACIA_DRYING_RACK = register("acacia_drying_rack", () -> new DryingRackBlock(Blocks.ACACIA_SLAB));
    public static final BlockRegistryObject<DryingRackBlock> DARK_OAK_DRYING_RACK = register("dark_oak_drying_rack", () -> new DryingRackBlock(Blocks.DARK_OAK_SLAB));

    public static final BlockRegistryObject<Block> STONE_MACHINE_FRAME = register("stone_machine_frame", () -> new HarvestableBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).sound(SoundType.STONE).noOcclusion(), BlockTags.MINEABLE_WITH_PICKAXE));
    public static final BlockRegistryObject<Block> ALLOY_MACHINE_FRAME = register("alloy_machine_frame", () -> new HarvestableBlock(BlockBehaviour.Properties.copy(STONE_MACHINE_FRAME.get()), BlockTags.MINEABLE_WITH_PICKAXE));

    public static final BlockRegistryObject<CoalGeneratorBlock> COAL_GENERATOR = register("coal_generator", () -> new CoalGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(6, 20).sound(SoundType.METAL)));


    /*
        -------------------- FLUID BLOCKS --------------------
     */

    public static final BlockRegistryObject<LiquidBlock> FLUID_OIL = register("oil",() ->  new LiquidBlock(ModFluids.OIL, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final BlockRegistryObject<LiquidBlock> FLUID_DIESEL = register("diesel", () -> new LiquidBlock(ModFluids.DIESEL, BlockBehaviour.Properties.copy(Blocks.WATER)));


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

    /*
        =========================== HELPER METHODS & INSTANCES INITIALIZATION ===================================
     */

    private static <BLOCK extends Block> BlockRegistryObject<BLOCK> register(String name, Supplier<BLOCK> blockSupplier) {
        return new BlockRegistryObject<>(BLOCK_REGISTRY.register(name, blockSupplier));
    }

    static {
        ALL_ORE_BLOCKS = new HashMap<>();
        ALL_STORAGE_BLOCKS = new HashMap<>();
        ALL_ALLOY_STORAGE_BLOCKS = new HashMap<>();

        for (Metals.OreMetal metal : Metals.OreMetal.values()) {
            ALL_STORAGE_BLOCKS.put(metal, new BlockRegistryObject<>(BLOCK_REGISTRY.register(metal.name().toLowerCase() + "_block", () -> new HarvestableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), BlockTags.MINEABLE_WITH_PICKAXE, metal.getHarvestLevelTag()))));
        }

        for (Metals.Ore ore : Metals.Ore.values()) {
            BlockRegistryObject<Block> oreBlock = new BlockRegistryObject<>(BLOCK_REGISTRY.register(ore.name().toLowerCase() + "_ore", () -> new HarvestableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE), BlockTags.MINEABLE_WITH_PICKAXE, ore.getHarvestLevelTag())));
            ALL_ORE_BLOCKS.put(ore, oreBlock);
        }
        for (Metals.Alloy alloy : Metals.Alloy.values()) {
            ALL_ALLOY_STORAGE_BLOCKS.put(alloy, new BlockRegistryObject<>(BLOCK_REGISTRY.register(alloy.name().toLowerCase() + "_block", () -> new HarvestableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL))));
        }
    }

    public static void init(IEventBus eventBus) {
        BLOCK_REGISTRY.register(eventBus);
    }
}
