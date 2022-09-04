package net.silentchaos512.mechanisms.common.blocks.generators;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.silentchaos512.mechanisms.common.abstracts.BaseMachineBlockEntity;
import net.silentchaos512.mechanisms.common.blocks.abstracts.AbstractEntityBlock;
import net.silentchaos512.mechanisms.utls.CompoundTagUtils;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractGeneratorBlock<T extends BaseMachineBlockEntity> extends AbstractEntityBlock<T> {
    public AbstractGeneratorBlock(Properties p_49224_) {
        super(p_49224_, BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        CompoundTag blockEntityTag = pStack.getOrCreateTag();
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (CompoundTagUtils.hasBlockEntityTag(blockEntityTag) && blockEntity != null && blockEntity.getType().equals(getBlockEntityType())) {
            this.loadTagToBlock(CompoundTagUtils.getBlockEntityTag(blockEntityTag), (T) blockEntity);
        }
    }

    protected abstract void loadTagToBlock(CompoundTag blockEntityTag, T machineBlockEntity);
}
