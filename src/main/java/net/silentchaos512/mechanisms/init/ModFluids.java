package net.silentchaos512.mechanisms.init;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.registration.DirectRegister;

import java.util.function.Supplier;

public class ModFluids {
    public static final DirectRegister<Fluid> ALL_FLUIDS = new DirectRegister<>(ForgeRegistries.FLUIDS);
    public static FlowingFluid OIL;
    public static FlowingFluid OIL_FLOWING;

    public static FlowingFluid DIESEL;
    public static FlowingFluid DIESEL_FLOWING;

    public static FlowingFluid ETHANE;
    public static FlowingFluid POLYETHYLENE;

    static {
        ForgeFlowingFluid.Properties oilProperties = createFluidProperties("oil", () -> OIL, () -> OIL_FLOWING, ModBlocks.FLUID_OIL, ModItems.OIL_BUCKET);
        OIL = ALL_FLUIDS.register("oil", new ForgeFlowingFluid.Source(oilProperties));
        OIL_FLOWING = ALL_FLUIDS.register("oil_flowing", new ForgeFlowingFluid.Flowing(oilProperties));

        ForgeFlowingFluid.Properties dieselProperties = createFluidProperties("diesel", () -> DIESEL, () -> DIESEL_FLOWING, ModBlocks.FLUID_DIESEL, ModItems.BUCKET_DIESEL);
        DIESEL = ALL_FLUIDS.register("diesel", new ForgeFlowingFluid.Source(dieselProperties));
        DIESEL_FLOWING = ALL_FLUIDS.register("diesel_flowing", new ForgeFlowingFluid.Flowing(dieselProperties));

        ForgeFlowingFluid.Properties ethaneProperties = createGasProperties("ethane", () -> ETHANE, ModItems.BUCKET_ETHANE);
        ForgeFlowingFluid.Properties polyethyleneProperties = createGasProperties("polyethylene", () -> POLYETHYLENE, ModItems.BUCKET_POLYETHYLENE);

        ETHANE = ALL_FLUIDS.register("ethane", new ForgeFlowingFluid.Source(ethaneProperties));
        POLYETHYLENE = ALL_FLUIDS.register("polyethylene", new ForgeFlowingFluid.Source(polyethyleneProperties));
    }

    private static ForgeFlowingFluid.Properties createFluidProperties(String fluidName, Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing, Supplier<LiquidBlock> liquidBlock, Supplier<BucketItem> bucketItem) {
        String blockTexture = "block/" + fluidName;
        FluidAttributes.Builder attributeBuilder = FluidAttributes.builder(SilentsMechanisms.loc(blockTexture + "_still"), SilentsMechanisms.loc(blockTexture + "_flowing"));
        return new ForgeFlowingFluid.Properties(still, flowing, attributeBuilder);
    }

    private static ForgeFlowingFluid.Properties createGasProperties(String gasName, Supplier<? extends Fluid> still, Supplier<BucketItem> bucketItem) {
        String gasTexture = "block/" + gasName;
        FluidAttributes.Builder gasBuilder = FluidAttributes.builder(SilentsMechanisms.loc(gasTexture), SilentsMechanisms.loc(gasTexture)).gaseous();
        return new ForgeFlowingFluid.Properties(still, () -> null, gasBuilder);
    }


    public static void registerAllFluids() {
        ALL_FLUIDS.registerAllEntries();
    }

    private ModFluids() {}
}
