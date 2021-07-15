package net.silentchaos512.mechanisms.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.api.IMachineUpgrade;
import net.silentchaos512.mechanisms.util.TextUtil;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item.Properties;

public class MachineUpgradeItem extends Item {
    private final IMachineUpgrade upgrade;

    public MachineUpgradeItem(IMachineUpgrade upgrade) {
        super(new Properties().tab(SilentMechanisms.ITEM_GROUP));
        this.upgrade = upgrade;
    }

    public IMachineUpgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        // Upgrade description and value
        tooltip.add(new TranslationTextComponent(this.getDescriptionId() + ".desc", upgrade.getDisplayValue()));

        // Energy usage multiplier
        float energyCost = upgrade.getEnergyUsageMultiplier();
        if (!MathUtils.floatsEqual(energyCost, 0f)) {
            String str = String.format("%d", (int) (100 * energyCost));
            if (energyCost > 0)
                str = "+" + str;
            tooltip.add(TextUtil.translate("item", "machine_upgrade.energy", str));
        }
    }
}
