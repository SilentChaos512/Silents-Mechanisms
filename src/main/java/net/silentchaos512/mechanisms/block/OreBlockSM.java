package net.silentchaos512.mechanisms.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreBlockSM extends Block {
    public OreBlockSM(int hardness, int harvestLevel) {
        super(Properties.create(Material.ROCK)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(harvestLevel)
                .hardnessAndResistance(hardness, 3)
                .sound(SoundType.STONE)
        );
    }
}
