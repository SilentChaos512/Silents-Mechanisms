package net.silentchaos512.mechanisms.events;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.crusher.CrusherTileEntity;
import net.silentchaos512.mechanisms.capability.EnergyStorageImpl;

@Mod.EventBusSubscriber(modid = SilentMechanisms.MOD_ID)
public class CommonEvents {
    @SubscribeEvent
    public void onAttachTileEntityCaps(AttachCapabilitiesEvent<TileEntity> event) {
        // TODO: Is this even needed?
        if (event.getObject() instanceof CrusherTileEntity) {
            SilentMechanisms.LOGGER.debug("attach energy cap to {}", event.getObject());
            event.addCapability(SilentMechanisms.getId("energy"), new EnergyStorageImpl(100_000, 100, 100));
        }
    }
}
