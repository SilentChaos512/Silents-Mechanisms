package net.silentchaos512.mechanisms.block.generator;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.silentchaos512.mechanisms.block.AbstractMachineBlock;
import net.silentchaos512.mechanisms.util.MachineTier;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractGeneratorBlock extends AbstractMachineBlock {
    public AbstractGeneratorBlock(MachineTier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        // TODO: Show energy production rate and fuel consumption rate?
    }
}
