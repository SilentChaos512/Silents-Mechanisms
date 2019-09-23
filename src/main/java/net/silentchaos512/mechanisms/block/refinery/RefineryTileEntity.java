package net.silentchaos512.mechanisms.block.refinery;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.silentchaos512.mechanisms.block.AbstractMachineBaseTileEntity;
import net.silentchaos512.mechanisms.init.ModTileEntities;
import net.silentchaos512.mechanisms.util.MachineTier;
import net.silentchaos512.mechanisms.util.TextUtil;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class RefineryTileEntity extends AbstractMachineBaseTileEntity {
    public static final int FIELDS_COUNT = 10; // TODO

    private final FluidTank[] tanks;

    public RefineryTileEntity() {
        super(ModTileEntities.refinery, 0, MachineTier.STANDARD.getEnergyCapacity(), 500, 0, MachineTier.BASIC);
        tanks = IntStream.range(0, 5).mapToObj(k -> new FluidTank(10_000)).toArray(FluidTank[]::new);
    }

    public FluidStack getFluid(int tank) {
        if (tank < 0 || tank >= tanks.length)
            return FluidStack.EMPTY;
        return tanks[tank].getFluid();
    }

    @Override
    public void tick() {
        super.tick();
        // TODO
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new RefineryContainer(id, player, this, this.fields);
    }
}
