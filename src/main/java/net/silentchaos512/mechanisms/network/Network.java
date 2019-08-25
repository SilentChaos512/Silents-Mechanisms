package net.silentchaos512.mechanisms.network;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.silentchaos512.mechanisms.SilentMechanisms;

import java.util.Objects;

public final class Network {
    public static SimpleChannel channel;
    static {
        channel = NetworkRegistry.ChannelBuilder.named(SilentMechanisms.getId("network"))
                .clientAcceptedVersions(s -> Objects.equals(s, "1"))
                .serverAcceptedVersions(s -> Objects.equals(s, "1"))
                .networkProtocolVersion(() -> "1")
                .simpleChannel();

        channel.messageBuilder(SetRedstoneModePacket.class, 1)
                .decoder(SetRedstoneModePacket::fromBytes)
                .encoder(SetRedstoneModePacket::toBytes)
                .consumer(SetRedstoneModePacket::handle)
                .add();
    }

    private Network() {}

    public static void init() {}
}
