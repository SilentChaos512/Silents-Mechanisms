package net.silentchaos512.mechanisms.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlockEntity;
import net.silentchaos512.mechanisms.common.blocks.generators.coalgenerator.CoalGeneratorBlockEntity;
import net.silentchaos512.mechanisms.registration.DirectRegistry;

@SuppressWarnings("ALL")
public final class ModBlockEntities {
    public static final DirectRegistry<BlockEntityType<?>> DIRECT_BE_TPYES = new DirectRegistry<>();
    public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACKS;
    public static final BlockEntityType<CoalGeneratorBlockEntity> COAL_GENERATOR;

    static {
        DRYING_RACKS = DIRECT_BE_TPYES.register("drying_racks",
                BlockEntityType.Builder.of(DryingRackBlockEntity::new, ModBlocks.DRYING_RACK_BLOCKS.toArray(new DryingRackBlock[0]))
                        .build(null));
        COAL_GENERATOR = DIRECT_BE_TPYES.register("coal_generators",
                BlockEntityType.Builder.of(CoalGeneratorBlockEntity::new, ModBlocks.COAL_GENERATOR)
                        .build(null));
    }

    public static void staticInitializing() {

    }
}
