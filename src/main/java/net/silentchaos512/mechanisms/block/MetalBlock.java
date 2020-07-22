package net.silentchaos512.mechanisms.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class MetalBlock extends Block {
    public MetalBlock() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(4, 20)
                .sound(SoundType.METAL)
        );
    }
}
