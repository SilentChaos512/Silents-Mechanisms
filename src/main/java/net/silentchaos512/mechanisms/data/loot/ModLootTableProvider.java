package net.silentchaos512.mechanisms.data.loot;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.init.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator pGenerator) {
        super(pGenerator.getPackOutput(), Set.of(), List.of(new LootTableProvider.SubProviderEntry(BlockLoots::new, LootContextParamSets.BLOCK)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
        map.forEach((rl, loot) -> LootTables.validate(validationTracker, rl, loot));
    }

    private static final class BlockLoots extends BlockLootSubProvider {
        private BlockLoots() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCK_DIRECT_REGISTRY.getEntries().stream().filter(block -> !(block instanceof LiquidBlock)).collect(Collectors.toList());
        }

        @Override
        protected void generate() {
            ModBlocks.ALL_ORE_BLOCKS.forEach(((ore, block) -> super.add(block, createOreDrop(block, ore.getRawOreItem()))));
            ModBlocks.ALL_DEEPSLATE_ORE_BLOCKS.forEach((ore, block) -> super.add(block, createOreDrop(block, ore.getRawOreItem())));
            ModBlocks.METAL_STORAGE_BLOCKS.values().forEach(this::dropSelf);
            DryingRackBlock.ALL_RACKS.forEach(this::dropSelf);
            dropSelf(ModBlocks.STONE_MACHINE_FRAME);
            dropSelf(ModBlocks.ALLOY_MACHINE_FRAME);
            super.add(ModBlocks.COAL_GENERATOR, (block) -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("EnergyStored", "BlockEntityTag.EnergyStored")))));
            super.add(ModBlocks.BATTERY_BOX, block -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("EnergyStored", "BlockEntityTag.EnergyStored")))));
        }
    }
}
