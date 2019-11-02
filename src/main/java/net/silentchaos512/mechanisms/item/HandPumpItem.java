package net.silentchaos512.mechanisms.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.silentchaos512.lib.util.PlayerUtils;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.api.IFluidContainer;
import net.silentchaos512.mechanisms.util.EnergyUtils;
import net.silentchaos512.mechanisms.util.InventoryUtils;

public class HandPumpItem extends EnergyStoringItem {
    private static final int MAX_ENERGY = 100_000;
    private static final int MAX_RECEIVE = 100;
    private static final int ENERGY_PER_OPERATION = 500;

    public HandPumpItem() {
        super(new Properties().group(SilentMechanisms.ITEM_GROUP).maxStackSize(1), MAX_ENERGY, MAX_RECEIVE, ENERGY_PER_OPERATION);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResultType.PASS;

        IEnergyStorage energy = EnergyUtils.getEnergy(context.getItem());
        if (energy == null) return ActionResultType.PASS;

        if (energy.getEnergyStored() < ENERGY_PER_OPERATION) {
            // TODO: Notify player?
            return ActionResultType.FAIL;
        }

        World world = context.getWorld();

        // Try to pull fluid from machines
        BlockPos pos = context.getPos();
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null) {
            LazyOptional<IFluidHandler> lazyOptional = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
            if (lazyOptional.isPresent()) {
                IFluidHandler fluidHandler = lazyOptional.orElseThrow(IllegalStateException::new);
                return tryExtractFromTank(player, energy, fluidHandler);
            }
        }

        // Or pickup fluids from the world
        BlockPos posOpposite = context.getPos().offset(context.getFace());
        BlockState state = world.getBlockState(posOpposite);

        if (state.getBlock() instanceof IBucketPickupHandler) {
            ItemStack emptyContainer = takeFluidContainer(player);
            if (!emptyContainer.isEmpty()) {
                Fluid fluid = ((IBucketPickupHandler) state.getBlock()).pickupFluid(world, posOpposite, state);
                FluidStack fluidStack = new FluidStack(fluid, 1000);
                if (!fluidStack.isEmpty()) {
                    giveFilledContainer(player, energy, emptyContainer, fluidStack);
                    return ActionResultType.SUCCESS;
                }
            }
        }

        return ActionResultType.PASS;
    }

    private static ActionResultType tryExtractFromTank(PlayerEntity player, IEnergyStorage energy, IFluidHandler fluidHandler) {
        ItemStack emptyContainer = takeFluidContainer(player);
        if (!emptyContainer.isEmpty()) {
            for (int i = 0; i < fluidHandler.getTanks(); ++i) {
                FluidStack fluidInTank = fluidHandler.getFluidInTank(i);
                if (fluidInTank.getAmount() > 999) {
                    FluidStack fluidStack = new FluidStack(fluidInTank.getFluid(), 1000);
                    if (fluidHandler.drain(fluidStack, IFluidHandler.FluidAction.SIMULATE).getAmount() == 1000) {
                        fluidHandler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        giveFilledContainer(player, energy, emptyContainer, fluidStack);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }

    private static void giveFilledContainer(PlayerEntity player, IEnergyStorage energy, ItemStack emptyContainer, FluidStack fluidStack) {
        ItemStack filledContainer = IFluidContainer.fillBucketOrFluidContainer(emptyContainer, fluidStack);
        energy.extractEnergy(ENERGY_PER_OPERATION, false);
        PlayerUtils.giveItem(player, filledContainer);
    }

    private static ItemStack takeFluidContainer(PlayerEntity player) {
        for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (InventoryUtils.isEmptyFluidContainer(stack)) {
                ItemStack split = stack.split(1);
                if (stack.isEmpty()) {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
                return split;
            }
        }

        return ItemStack.EMPTY;
    }
}
