package net.silentchaos512.mechanisms.compat.hwyla;

import mcp.mobius.waila.api.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.silentchaos512.mechanisms.block.wire.WireBlock;
import net.silentchaos512.mechanisms.block.wire.WireTileEntity;

import java.util.List;

@WailaPlugin
public class SMechanismsHwylaPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(new IComponentProvider() {
            @Override
            public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
                tooltip.add(new StringTextComponent(accessor.getServerData().getString("WireNetwork")));
            }
        }, TooltipPosition.BODY, WireBlock.class);

        registrar.registerBlockDataProvider((data, player, world, tileEntity) -> {
            data.putString("WireNetwork", ((WireTileEntity) tileEntity).getWireNetworkData());
        }, WireTileEntity.class);
    }
}
