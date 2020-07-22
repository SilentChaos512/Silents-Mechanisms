package net.silentchaos512.mechanisms.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.silentchaos512.mechanisms.util.TextUtil;

public class DebugItem extends Item {
    public DebugItem() {
        super(new Properties().maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResultType.PASS;

        World world = context.getWorld();
        BlockPos pos = context.getPos();
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                ITextComponent energyText = TextUtil.energyWithMax(e.getEnergyStored(), e.getMaxEnergyStored());
                player.sendMessage(new StringTextComponent("Energy: ").func_230529_a_(energyText), Util.DUMMY_UUID);
                player.sendMessage(new StringTextComponent("Receive/Extract: " + e.canReceive() + "/" + e.canExtract()), Util.DUMMY_UUID);
                player.sendMessage(new StringTextComponent(e.getClass().getName()).func_240699_a_(TextFormatting.ITALIC), Util.DUMMY_UUID);
            });

            for (Direction side : Direction.values()) {
                TileEntity other = world.getTileEntity(pos.offset(side));
                if (other != null) {
                    other.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                        player.sendMessage(new StringTextComponent(side + ": " + other.getClass().getSimpleName()), Util.DUMMY_UUID);
                    });
                }
            }

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }
}
