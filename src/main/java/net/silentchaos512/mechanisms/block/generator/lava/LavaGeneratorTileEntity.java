package net.silentchaos512.mechanisms.block.generator.lava;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.mechanisms.api.IFluidContainer;
import net.silentchaos512.mechanisms.block.generator.AbstractFluidFuelGeneratorTileEntity;
import net.silentchaos512.mechanisms.init.ModBlocks;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;

public class LavaGeneratorTileEntity extends AbstractFluidFuelGeneratorTileEntity {
    // Energy constants
    public static final int MAX_ENERGY = 50_000;
    public static final int MAX_SEND = 500;
    public static final int ENERGY_CREATED_PER_TICK = 100;
    public static final int TICKS_PER_MILLIBUCKET = 5;

    public LavaGeneratorTileEntity() {
        super(ModTileEntities.lavaGenerator, 2, MAX_ENERGY, 0, MAX_SEND, new FluidTank(4000, s -> s.getFluid().isIn(FluidTags.LAVA)));
    }

    public IFluidHandler getTank() {
        return tank;
    }

    @Override
    protected boolean hasFuel() {
        return tank.getFluidAmount() > 0;
    }

    @Override
    protected void consumeFuel() {
        tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
        burnTime = TICKS_PER_MILLIBUCKET;
    }

    @Override
    protected int getEnergyCreatedPerTick() {
        return ENERGY_CREATED_PER_TICK;
    }

    @Override
    protected BlockState getActiveState() {
        return ModBlocks.lavaGenerator.getDefaultState().with(AbstractFurnaceBlock.LIT, true);
    }

    @Override
    protected BlockState getInactiveState() {
        return ModBlocks.lavaGenerator.getDefaultState().with(AbstractFurnaceBlock.LIT, false);
    }

    @Override
    public void tick() {
        // Drain buckets into internal tank
        ItemStack input = getStackInSlot(0);
        if (!input.isEmpty()) {
            tryFillTank(input);
        }

        super.tick();
    }

    private void tryFillTank(ItemStack input) {
        FluidStack fluidStack = IFluidContainer.getBucketOrContainerFluid(input);
        if (canAcceptLavaContainer(input, fluidStack)) {
            tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            sendUpdate(getBlockState(), true);

            ItemStack containerItem = input.getContainerItem();
            input.shrink(1);

            ItemStack output = getStackInSlot(1);
            if (output.isEmpty()) {
                setInventorySlotContents(1, containerItem);
            } else {
                output.grow(1);
            }
        }
    }

    private boolean canAcceptLavaContainer(ItemStack input, FluidStack fluid) {
        ItemStack output = getStackInSlot(1);
        return !fluid.isEmpty()
                && tank.isFluidValid(0, fluid)
                && tank.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == 1000
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 0
                && stack.getItem() instanceof BucketItem
                && ((BucketItem) stack.getItem()).getFluid().isIn(FluidTags.LAVA);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "lava_generator");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new LavaGeneratorContainer(id, playerInventory, this, this.fields);
    }
}
