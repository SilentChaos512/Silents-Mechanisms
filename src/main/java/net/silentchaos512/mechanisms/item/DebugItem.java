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

import net.minecraft.item.Item.Properties;

public class DebugItem extends Item {
    public DebugItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResultType.PASS;

        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                ITextComponent energyText = TextUtil.energyWithMax(e.getEnergyStored(), e.getMaxEnergyStored());
                player.sendMessage(new StringTextComponent("Energy: ").append(energyText), Util.NIL_UUID);
                player.sendMessage(new StringTextComponent("Receive/Extract: " + e.canReceive() + "/" + e.canExtract()), Util.NIL_UUID);
                player.sendMessage(new StringTextComponent(e.getClass().getName()).withStyle(TextFormatting.ITALIC), Util.NIL_UUID);
            });

            for (Direction side : Direction.values()) {
                TileEntity other = world.getBlockEntity(pos.relative(side));
                if (other != null) {
                    other.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                        player.sendMessage(new StringTextComponent(side + ": " + other.getClass().getSimpleName()), Util.NIL_UUID);
                    });
                }
            }

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }
}
