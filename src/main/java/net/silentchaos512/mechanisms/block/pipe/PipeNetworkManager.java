package net.silentchaos512.mechanisms.block.pipe;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.mechanisms.SilentMechanisms;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = SilentMechanisms.MOD_ID)
public final class PipeNetworkManager {
    private static final Collection<LazyOptional<PipeNetwork>> NETWORK_LIST = Collections.synchronizedList(new ArrayList<>());

    private PipeNetworkManager() {throw new IllegalAccessError("Utility class");}

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static PipeNetwork get(IWorldReader world, BlockPos pos) {
        return getLazy(world, pos).orElse(null);
    }

    public static LazyOptional<PipeNetwork> getLazy(IWorldReader world, BlockPos pos) {
        synchronized (NETWORK_LIST) {
            for (LazyOptional<PipeNetwork> network : NETWORK_LIST) {
                if (network.isPresent()) {
                    PipeNetwork net = network.orElseThrow(IllegalStateException::new);
                    if (net.contains(world, pos)) {
//                    SilentMechanisms.LOGGER.debug("get network {}", network);
                        return network;
                    }
                }
            }
        }

        // Create new
        PipeNetwork network = PipeNetwork.buildNetwork(world, pos);
        LazyOptional<PipeNetwork> lazy = LazyOptional.of(() -> network);
        NETWORK_LIST.add(lazy);
        SilentMechanisms.LOGGER.debug("create network {}", network);
        return lazy;
    }

    public static void invalidateNetwork(IWorldReader world, BlockPos pos) {
        Collection<LazyOptional<PipeNetwork>> toRemove = NETWORK_LIST.stream()
                .filter(n -> n != null && n.isPresent() && n.orElseThrow(IllegalStateException::new).contains(world, pos))
                .collect(Collectors.toList());
        toRemove.forEach(PipeNetworkManager::invalidateNetwork);
    }

    private static void invalidateNetwork(LazyOptional<PipeNetwork> network) {
        SilentMechanisms.LOGGER.debug("invalidateNetwork {}", network);
        NETWORK_LIST.removeIf(n -> n.isPresent() && n.equals(network));
        network.ifPresent(PipeNetwork::invalidate);
        network.invalidate();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        NETWORK_LIST.stream()
                .filter(n -> n != null && n.isPresent())
                .forEach(n -> n.ifPresent(PipeNetwork::moveFluids));
    }
}
