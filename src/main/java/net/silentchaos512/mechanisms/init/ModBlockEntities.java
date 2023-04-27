package net.silentchaos512.mechanisms.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.silentchaos512.mechanisms.common.blocks.batterybox.BatteryBoxBlockEntity;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlockEntity;
import net.silentchaos512.mechanisms.common.blocks.generators.coalgenerator.CoalGeneratorBlockEntity;
import net.silentchaos512.mechanisms.registration.DirectRegistry;

//just to remove the null warning :PP
@SuppressWarnings("ConstantConditions")

public final class ModBlockEntities {
    public static final DirectRegistry<BlockEntityType<?>> DIRECT_BE_TYPES = new DirectRegistry<>();
    public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACKS;
    public static final BlockEntityType<CoalGeneratorBlockEntity> COAL_GENERATOR;
    public static final BlockEntityType<BatteryBoxBlockEntity> BATTERY_BOX;
    //public static final BlockEntityType<>

    static {
        DRYING_RACKS = DIRECT_BE_TYPES.register("drying_racks",
                BlockEntityType.Builder.of(DryingRackBlockEntity::new, DryingRackBlock.ALL_RACKS.toArray(new DryingRackBlock[0]))
                        .build(null));
        COAL_GENERATOR = DIRECT_BE_TYPES.register("coal_generators",
                BlockEntityType.Builder.of(CoalGeneratorBlockEntity::new, ModBlocks.COAL_GENERATOR)
                        .build(null));
        BATTERY_BOX = DIRECT_BE_TYPES.register("batery_box",
                BlockEntityType.Builder.of(BatteryBoxBlockEntity::new, ModBlocks.BATTERY_BOX)
                        .build(null));
    }
}
