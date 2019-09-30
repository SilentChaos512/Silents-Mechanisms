package net.silentchaos512.mechanisms.block.wire;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.mechanisms.SilentMechanisms;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public final class WireNetworkManager {
    private static final Collection<LazyOptional<WireNetwork>> NETWORK_LIST = new ArrayList<>();

    private WireNetworkManager() {throw new IllegalAccessError("Utility class");}

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static WireNetwork get(IBlockReader world, BlockPos pos) {
        return getLazy(world, pos).orElse(null);
    }

    public static LazyOptional<WireNetwork> getLazy(IBlockReader world, BlockPos pos) {
        for (LazyOptional<WireNetwork> network : NETWORK_LIST) {
            if (network.isPresent()) {
                WireNetwork net = network.orElseThrow(IllegalStateException::new);
                if (net.contains(world, pos)) {
//                    SilentMechanisms.LOGGER.debug("get network {}", network);
                    return network;
                }
            }
        }

        // Create new
        WireNetwork network = WireNetwork.buildNetwork(world, pos);
        LazyOptional<WireNetwork> lazy = LazyOptional.of(() -> network);
        NETWORK_LIST.add(lazy);
        SilentMechanisms.LOGGER.debug("create network {}", network);
        return lazy;
    }

    public static void invalidateNetwork(IBlockReader world, BlockPos pos) {
        Collection<LazyOptional<WireNetwork>> toRemove = NETWORK_LIST.stream()
                .filter(n -> n != null && n.isPresent() && n.orElseThrow(IllegalStateException::new).contains(world, pos))
                .collect(Collectors.toList());
        toRemove.forEach(WireNetworkManager::invalidateNetwork);
    }

    private static void invalidateNetwork(LazyOptional<WireNetwork> network) {
        SilentMechanisms.LOGGER.debug("invalidateNetwork {}", network);
        NETWORK_LIST.removeIf(n -> n.isPresent() && n.equals(network));
        network.invalidate();
    }

    public static void invalidateNetworks() {
        NETWORK_LIST.clear();
    }

    public static int getNetworkCount() {
        return NETWORK_LIST.size();
    }
}
