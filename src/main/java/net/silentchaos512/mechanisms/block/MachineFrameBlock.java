package net.silentchaos512.mechanisms.block;

import net.minecraft.block.StainedGlassBlock;
import net.minecraft.item.DyeColor;

import net.minecraft.block.AbstractBlock.Properties;

/**
 * Machine frame block. Currently this extends StainedGlassBlock to work around a Forge bug (#32)
 */
public class MachineFrameBlock extends StainedGlassBlock {
    public MachineFrameBlock(Properties properties) {
        super(DyeColor.WHITE, properties);
    }
}
