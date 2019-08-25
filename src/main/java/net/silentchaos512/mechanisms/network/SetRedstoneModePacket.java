package net.silentchaos512.mechanisms.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.silentchaos512.mechanisms.api.RedstoneMode;
import net.silentchaos512.mechanisms.block.AbstractMachineContainer;
import net.silentchaos512.mechanisms.block.AbstractMachineTileEntity;
import net.silentchaos512.utils.EnumUtils;

import java.util.function.Supplier;

public class SetRedstoneModePacket {
    private BlockPos pos;
    private RedstoneMode mode;

    public SetRedstoneModePacket() {
    }

    public SetRedstoneModePacket(BlockPos pos, RedstoneMode mode) {
        this.pos = pos;
        this.mode = mode;
    }

    public static SetRedstoneModePacket fromBytes(PacketBuffer buffer) {
        SetRedstoneModePacket packet = new SetRedstoneModePacket();
        packet.pos = buffer.readBlockPos();
        packet.mode = EnumUtils.byOrdinal(buffer.readByte(), RedstoneMode.IGNORED);
        return packet;
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeByte(this.mode.ordinal());
    }

    public static void handle(SetRedstoneModePacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayerEntity player = context.get().getSender();
        context.get().enqueueWork(() -> handlePacket(packet, player));
        context.get().setPacketHandled(true);
    }

    private static void handlePacket(SetRedstoneModePacket packet, ServerPlayerEntity player) {
        if (player != null) {
            if (player.openContainer instanceof AbstractMachineContainer) {
                TileEntity tileEntity = ((AbstractMachineContainer) player.openContainer).getTileEntity();
                if (tileEntity instanceof AbstractMachineTileEntity) {
                    ((AbstractMachineTileEntity) tileEntity).setRedstoneMode(packet.mode);
                }
            }
        }
    }
}
