package net.silentchaos512.mechanisms.block.wire;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.silentchaos512.mechanisms.api.ConnectionType;
import net.silentchaos512.mechanisms.util.EnergyUtils;

import java.util.*;

public final class WireNetwork implements IEnergyStorage {
    public static final int TRANSFER_PER_CONNECTION = 1000;

    private final IBlockReader world;
    private final Map<BlockPos, Set<Connection>> connections = new HashMap<>();
    private boolean connectionsBuilt;
    private int energyStored;

    private WireNetwork(IBlockReader world, Set<BlockPos> wires, int energyStored) {
        this.world = world;
        wires.forEach(pos -> connections.put(pos, Collections.emptySet()));
        this.energyStored = energyStored;
    }

    public boolean contains(IBlockReader world, BlockPos pos) {
        return this.world == world && connections.containsKey(pos);
    }

    public int getWireCount() {
        return connections.size();
    }

    public Connection getConnection(BlockPos pos, Direction side) {
        if (connections.containsKey(pos)) {
            for (Connection connection : connections.get(pos)) {
                if (connection.side == side) {
                    return connection;
                }
            }
        }
        return new Connection(this, side, ConnectionType.NONE);
    }

    private void updateWireEnergy() {
        int energyPerWire = energyStored / getWireCount();
        connections.keySet().forEach(p -> {
            TileEntity tileEntity = world.getTileEntity(p);
            if (tileEntity instanceof WireTileEntity) {
                ((WireTileEntity) tileEntity).energyStored = energyPerWire;
            }
        });
    }

    void invalidate() {
        connections.values().forEach(set -> set.forEach(con -> con.getLazyOptional().invalidate()));
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        buildConnections();
        int received = Math.min(getMaxEnergyStored() - energyStored, Math.min(maxReceive, TRANSFER_PER_CONNECTION));
        if (received > 0) {
//            SilentMechanisms.LOGGER.debug("receive ({}): {}, {} -> {}", simulate, received, energyStored, energyStored + received);
            if (!simulate) {
                energyStored += received;
                updateWireEnergy();
            }
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        buildConnections();
        int extracted = Math.min(energyStored, Math.min(maxExtract, TRANSFER_PER_CONNECTION));
        if (extracted > 0) {
//            SilentMechanisms.LOGGER.debug("extract ({}): {}, {} -> {}", simulate, extracted, energyStored, energyStored - extracted);
            if (!simulate) {
                energyStored -= extracted;
                updateWireEnergy();
            }
        }
        return extracted;
    }

    void sendEnergy() {
        buildConnections();

        // Send stored energy to connected blocks
        for (Map.Entry<BlockPos, Set<Connection>> entry : connections.entrySet()) {
            BlockPos pos = entry.getKey();
            Set<Connection> connections = entry.getValue();
            for (Connection con : connections) {
                if (con.type.canExtract()) {
                    IEnergyStorage energy = EnergyUtils.getEnergy(world, pos.offset(con.side));
                    if (energy != null && energy.canReceive()) {
                        int toSend = extractEnergy(TRANSFER_PER_CONNECTION, true);
                        int accepted = energy.receiveEnergy(toSend, false);
                        extractEnergy(accepted, false);
//                        SilentMechanisms.LOGGER.debug("send {} to {} of {}, accepted {}", toSend, con.side, pos, accepted);
                    }
                }
            }
        }
    }

    @Override
    public int getEnergyStored() {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored() {
        return 1000 * connections.size();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    static WireNetwork buildNetwork(IBlockReader world, BlockPos pos) {
        Set<BlockPos> wires = buildWireSet(world, pos);
        int energyStored = wires.stream().mapToInt(p -> {
            TileEntity tileEntity = world.getTileEntity(p);
            return tileEntity instanceof WireTileEntity ? ((WireTileEntity) tileEntity).energyStored : 0;
        }).sum();
        return new WireNetwork(world, wires, energyStored);
    }

    private static Set<BlockPos> buildWireSet(IBlockReader world, BlockPos pos) {
        return buildWireSet(world, pos, new HashSet<>());
    }

    private static Set<BlockPos> buildWireSet(IBlockReader world, BlockPos pos, Set<BlockPos> set) {
        // Get all positions that have a wire connected to the wire at pos
        set.add(pos);
        for (Direction side : Direction.values()) {
            BlockPos pos1 = pos.offset(side);
            if (!set.contains(pos1) && world.getTileEntity(pos1) instanceof WireTileEntity) {
                set.add(pos1);
                set.addAll(buildWireSet(world, pos1, set));
            }
        }
        return set;
    }

    private void buildConnections() {
        // Determine all connections. This will be done once the connections are actually needed.
        if (!connectionsBuilt) {
            connections.keySet().forEach(p -> connections.put(p, getConnections(world, p)));
            connectionsBuilt = true;
        }
    }

    private Set<Connection> getConnections(IBlockReader world, BlockPos pos) {
        // Get all connections for the wire at pos
        Set<Connection> connections = new HashSet<>();
        for (Direction direction : Direction.values()) {
            TileEntity te = world.getTileEntity(pos.offset(direction));
            if (te != null && !(te instanceof WireTileEntity) && te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                ConnectionType type = WireBlock.getConnection(world.getBlockState(pos), direction);
                connections.add(new Connection(this, direction, type));
            }
        }
        return connections;
    }

    @Override
    public String toString() {
        return String.format("WireNetwork %s, %d wires, %,d FE", Integer.toHexString(hashCode()), connections.size(), energyStored);
    }

    public static class Connection implements IEnergyStorage {
        private final WireNetwork network;
        private final Direction side;
        private final ConnectionType type;
        private final LazyOptional<Connection> lazyOptional;

        Connection(WireNetwork network, Direction side, ConnectionType type) {
            this.network = network;
            this.side = side;
            this.type = type;
            this.lazyOptional = LazyOptional.of(() -> this);
        }

        public LazyOptional<Connection> getLazyOptional() {
            return lazyOptional;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!canReceive()) {
                return 0;
            }
            return network.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!canExtract()) {
                return 0;
            }
            return network.extractEnergy(maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return network.energyStored;
        }

        @Override
        public int getMaxEnergyStored() {
            return network.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return type.canExtract();
        }

        @Override
        public boolean canReceive() {
            return type.canReceive();
        }
    }
}
