package net.silentchaos512.mechanisms.network;

import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.crafting.refining.RefiningRecipeManager;

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
        channel.messageBuilder(SyncRefiningRecipesPacket.class, 2)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(SyncRefiningRecipesPacket::decode)
                .encoder(SyncRefiningRecipesPacket::encode)
                .markAsLoginPacket()
                .consumer(FMLHandshakeHandler.biConsumerFor((hh, msg, ctx) -> {
                    RefiningRecipeManager.handleSyncPacket(msg, ctx);
                    channel.reply(new LoginPacket.Reply(), ctx.get());
                }))
                .add();
        channel.messageBuilder(LoginPacket.Reply.class, 3)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(buffer -> new LoginPacket.Reply())
                .encoder((msg, buffer) -> {})
                .consumer(FMLHandshakeHandler.indexFirst((hh, msg, ctx) -> msg.handle(ctx)))
                .add();
    }

    private Network() {}

    public static void init() {}
}
