package net.silentchaos512.mechanisms.init;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;

public class SMCreativeTab {
    public static CreativeModeTab SM_TAB;

    public static void registerTab(CreativeModeTabEvent.Register event) {
        SM_TAB = event.registerCreativeModeTab(new ResourceLocation(SilentsMechanisms.MODID, "sm_tab"), builder -> {
            //Placeholder for crusher, will replace it after it is finished
            builder.icon(() -> new ItemStack(Items.FURNACE))
                    .title(Component.translatable("itemGroup.silents_mechanisms"))
                    .displayItems(((pEnabledFeatures, pOutput, pDisplayOperatorCreativeTab) -> ModItems.ITEM_DIRECT_REGISTRY.stream().forEach(pOutput::accept)));
        });
    }
}
