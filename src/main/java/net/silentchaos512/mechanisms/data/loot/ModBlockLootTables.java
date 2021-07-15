package net.silentchaos512.mechanisms.data.loot;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.mechanisms.init.Registration;

import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootTables {
    @Override
    protected void addTables() {
        Registration.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> block.asItem() != Items.AIR)
                .forEach(this::dropSelf);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
}
