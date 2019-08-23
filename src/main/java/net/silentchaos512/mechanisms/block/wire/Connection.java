package net.silentchaos512.mechanisms.block.wire;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class Connection {
    private BlockPos pos;
    private Direction facing;

    public Connection() {
        pos = new BlockPos(0, 0, 0);
        facing = Direction.UP;
    }

    public Connection(BlockPos pos, Direction facing) {
        this.pos = pos;
        this.facing = facing;
    }

    public BlockPos getCablePos() {
        return this.pos;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public BlockPos getConnectedPos() {
        return this.pos.offset(this.facing);
    }

    public boolean areEqual(Connection con2) {
        return this.pos.equals(con2.pos) && this.facing.equals(con2.facing);
    }

    public boolean areEqual(BlockPos pos, Direction facing) {
        return this.pos.equals(pos) && this.facing.equals(facing);
    }

    public CompoundNBT serializeConnection() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("posx", this.pos.getX());
        nbt.putInt("posy", this.pos.getY());
        nbt.putInt("posz", this.pos.getZ());
        nbt.putInt("facing", this.facing.getIndex());
        return nbt;
    }

    public Connection deserializeConnection(CompoundNBT nbt) {
        this.pos = new BlockPos(nbt.getInt("posx"), nbt.getInt("posy"), nbt.getInt("posz"));
        this.facing = Direction.byIndex(nbt.getInt("facing"));
        return this;
    }
}
