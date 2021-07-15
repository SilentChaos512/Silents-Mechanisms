package net.silentchaos512.mechanisms.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import net.minecraft.block.AbstractBlock.Properties;

public class MetalBlock extends Block {
    public MetalBlock() {
        super(Properties.of(Material.METAL)
                .strength(4, 20)
                .sound(SoundType.METAL)
        );
    }
}
