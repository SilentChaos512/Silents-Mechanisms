package net.silentchaos512.mechanisms.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.blocks.dryingracks.DryingRackBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SilentsMechanisms.MODID);

    public static void init(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

    public static final RegistryObject<BlockEntityType<DryingRackBlockEntity>> DRYING_RACKS = BLOCK_ENTITY_TYPES.register("drying_racks",
            () -> BlockEntityType.Builder.of(DryingRackBlockEntity::new,
                            ModBlocks.OAK_DRYING_RACK.get(),
                            ModBlocks.SPRUCE_DRYING_RANK.get(),
                            ModBlocks.BIRCH_DRYING_RACK.get(),
                            ModBlocks.JUNGLE_DRYING_RACK.get(),
                            ModBlocks.ACACIA_DRYING_RACK.get(),
                            ModBlocks.DARK_OAK_DRYING_RACK.get())
                    .build(null));


}
