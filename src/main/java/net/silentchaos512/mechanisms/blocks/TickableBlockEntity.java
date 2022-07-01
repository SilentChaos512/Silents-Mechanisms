package net.silentchaos512.mechanisms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface TickableBlockEntity {
    static <E extends BlockEntity & TickableBlockEntity> void staticServerTick(Level level, BlockPos pos, BlockState state, E blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    void serverTick(Level level, BlockPos pos, BlockState state);
}
