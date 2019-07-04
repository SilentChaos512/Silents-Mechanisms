package net.silentchaos512.mechanisms.events;

import net.minecraft.item.Item;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.capability.EnergyStorageImplBase;
import net.silentchaos512.mechanisms.item.BatteryItem;

@Mod.EventBusSubscriber(modid = SilentMechanisms.MOD_ID)
public class CommonEvents {
    @SubscribeEvent
    public static void onAttachItemCaps(AttachCapabilitiesEvent<Item> event) {
        if (event.getObject() instanceof BatteryItem) {
            event.addCapability(SilentMechanisms.getId("energy"), new EnergyStorageImplBase(500_000, 10_000, 10_000));
        }
    }
}
