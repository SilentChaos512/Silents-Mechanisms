package net.silentchaos512.mechanisms.block.wire;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.mechanisms.api.ConnectionType;
import net.silentchaos512.mechanisms.util.EnergyUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WireNetwork implements IEnergyStorage {
    private final IBlockReader world;
    private final Map<BlockPos, Set<Connection>> connections = new HashMap<>();

    public WireNetwork(IBlockReader world) {
        this.world = world;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energySent = 0;
        // Try to send energy to each connection
        for (Map.Entry<BlockPos, Set<Connection>> entry : connections.entrySet()) {
            BlockPos pos = entry.getKey();
            Set<Connection> connections = entry.getValue();
            for (Connection con : connections) {
                if (con.type.canReceive()) {
                    IEnergyStorage energy = EnergyUtils.getEnergy(world, pos.offset(con.side));
                    if (energy != null) {
                        energySent += energy.receiveEnergy(maxReceive - energySent, simulate);
                    }
                }
            }
        }
        return energySent;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energySent = 0;
        // Try to send energy to each connection
        for (Map.Entry<BlockPos, Set<Connection>> entry : connections.entrySet()) {
            BlockPos pos = entry.getKey();
            Set<Connection> connections = entry.getValue();
            for (Connection con : connections) {
                if (con.type.canExtract()) {
                    IEnergyStorage energy = EnergyUtils.getEnergy(world, pos.offset(con.side));
                    if (energy != null) {
                        energySent += energy.extractEnergy(maxExtract - energySent, simulate);
                    }
                }
            }
        }
        return energySent;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 0;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public static WireNetwork buildNetwork(IBlockReader world, BlockPos pos) {
        WireNetwork network = new WireNetwork(world);
        buildWireSet(world, pos, new HashSet<>()).forEach(p -> {
            Set<Connection> connections = new HashSet<>();
            for (Direction direction : Direction.values()) {
                TileEntity te = world.getTileEntity(p.offset(direction));
                if (te != null && !(te instanceof WireTileEntity) && te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                    ConnectionType type = WireBlock.getConnection(world.getBlockState(p), direction);
                    connections.add(new Connection(direction, type));
                }
            }
            network.connections.put(p, connections);
        });
//        SilentMechanisms.LOGGER.debug("Wire network has {} nodes", network.connections.size());
        return network;
    }

    private static Set<BlockPos> buildWireSet(IBlockReader world, BlockPos pos, Set<BlockPos> set) {
        for (Direction side : Direction.values()) {
            BlockPos pos1 = pos.offset(side);
            if (!set.contains(pos1) && world.getTileEntity(pos1) instanceof WireTileEntity) {
                set.add(pos1);
                set.addAll(buildWireSet(world, pos1, set));
            }
        }
        return set;
    }

    public static class Connection {
        private final Direction side;
        private final ConnectionType type;

        public Connection(Direction side, ConnectionType type) {
            this.side = side;
            this.type = type;
        }
    }
}
