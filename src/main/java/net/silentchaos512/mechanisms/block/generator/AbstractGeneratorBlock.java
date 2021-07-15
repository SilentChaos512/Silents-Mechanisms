package net.silentchaos512.mechanisms.block.generator;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.silentchaos512.mechanisms.block.AbstractMachineBlock;
import net.silentchaos512.mechanisms.util.MachineTier;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.block.AbstractBlock.Properties;

public abstract class AbstractGeneratorBlock extends AbstractMachineBlock {
    public AbstractGeneratorBlock(MachineTier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        // TODO: Show energy production rate and fuel consumption rate?
    }
}
