package net.silentchaos512.mechanisms.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import net.silentchaos512.mechanisms.abstracts.BreakableBlock;
import net.silentchaos512.mechanisms.abstracts.TickableBlockEntity;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractEntityBlock<TILE extends BlockEntity & TickableBlockEntity> extends BaseEntityBlock implements EntityBlock, BreakableBlock {

    private final TagKey<Block> harvestLevelTag;
    private final TagKey<Block> harvestToolTag;

    public AbstractEntityBlock(Properties p_49224_, TagKey<Block> harvestToolTag) {
        this(p_49224_, harvestToolTag, Tags.Blocks.NEEDS_WOOD_TOOL);
    }

    public AbstractEntityBlock(Properties p_49224_, TagKey<Block> harvestToolTag, TagKey<Block> harvestLevelTag) {
        super(p_49224_);
        this.harvestLevelTag = harvestLevelTag;
        this.harvestToolTag = harvestToolTag;
    }

    @Override
    public TagKey<Block> getHarvestLevel() {
        return this.harvestLevelTag;
    }

    @Override
    public TagKey<Block> getHarvestTool() {
        return this.harvestToolTag;
    }

    @Nullable
    @Override
    public abstract TILE newBlockEntity(BlockPos pPos, BlockState pState);

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, this.getBlockEntityType(), TickableBlockEntity::staticServerTick) : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof MenuProvider menu) {
            pPlayer.openMenu(menu);
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }

        return InteractionResult.PASS;
    }

    protected abstract BlockEntityType<TILE> getBlockEntityType();

    @Override
    public abstract BlockState getStateForPlacement(BlockPlaceContext pContext);

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    protected final BlockState facingBlockStateForPlacement(DirectionProperty property, BlockPlaceContext pContext) {
        return defaultBlockState().setValue(property, pContext.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            if (pLevel.getBlockEntity(pPos) instanceof Container container) {
                Containers.dropContents(pLevel, pPos, container);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
