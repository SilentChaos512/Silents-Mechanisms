package net.silentchaos512.mechanisms.data.loot;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.silentchaos512.mechanisms.init.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTable extends LootTableProvider {
    private static final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> TABLES =
            ImmutableList.of(Pair.of(BlockLoots::new, LootContextParamSets.BLOCK));


    public ModLootTable(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return TABLES;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
        map.forEach((rl, loot) -> LootTables.validate(validationTracker, rl, loot));
    }

    private static final class BlockLoots extends BlockLoot {
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCK_REGISTRY.getEntries().stream().map(Supplier::get).toList();
        }

        @Override
        protected void addTables() {
            ModBlocks.ALL_STORAGE_BLOCKS.values().forEach(this::dropSelf);
            ModBlocks.ALL_ORE_BLOCKS.forEach(((ore, block) -> super.add(block.get(), createOreDrop(block.get(), ore.getChunkItem()))));
            ModBlocks.ALL_ALLOY_STORAGE_BLOCKS.values().forEach(this::dropSelf);
            ModBlocks.DRYING_RACK_BLOCKS.forEach(this::dropSelf);
            dropSelf(ModBlocks.STONE_MACHINE_FRAME);
            dropSelf(ModBlocks.ALLOY_MACHINE_FRAME);
            super.add(ModBlocks.COAL_GENERATOR.get(), (block) -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("EnergyStored", "BlockEntityTag.EnergyStored")))));
        }

        private <T extends Block> void dropSelf(Supplier<T> block) {
            this.dropSelf(block.get());
        }
    }
}
