package net.silentchaos512.mechanisms.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.batterybox.BatteryBoxBlock;
import net.silentchaos512.mechanisms.block.crusher.CrusherBlock;
import net.silentchaos512.mechanisms.block.generator.CoalGeneratorBlock;
import net.silentchaos512.mechanisms.block.wire.WireBlock;

import javax.annotation.Nullable;

public final class ModBlocks {
    public static CrusherBlock crusher;
    public static CoalGeneratorBlock coalGenerator;
    public static BatteryBoxBlock batteryBox;
    public static WireBlock wire;

    private ModBlocks() {}

    public static void registerAll(RegistryEvent.Register<Block> event) {
        // Register your blocks here
        crusher = register("crusher", new CrusherBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(6, 20)));
        coalGenerator = register("coal_generator", new CoalGeneratorBlock());
        batteryBox = register("battery_box", new BatteryBoxBlock());
        wire = register("wire", new WireBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1, 5)));
    }

    private static <T extends Block> T register(String name, T block) {
        BlockItem item = new BlockItem(block, new Item.Properties().group(SilentMechanisms.ITEM_GROUP));
        return register(name, block, item);
    }

    private static <T extends Block> T register(String name, T block, @Nullable BlockItem item) {
        ResourceLocation id = SilentMechanisms.getId(name);
        block.setRegistryName(id);
        ForgeRegistries.BLOCKS.register(block);

        if (item != null) {
            ModItems.BLOCKS_TO_REGISTER.put(name, item);
        }

        return block;
    }
}
