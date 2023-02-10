package net.silentchaos512.mechanisms.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.registration.DirectRegistry;
import net.silentchaos512.mechanisms.utls.TranslateUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModFluids {
    public static final DirectRegistry<Fluid> ALL_FLUIDS = new DirectRegistry<>();
    public static final DirectRegistry<FluidType> ALL_FLUID_TYPES = new DirectRegistry<>();

    public static final FluidType OIL_FLUID_TYPE = registerTypeWithTexture("oil", true);
    public static final FluidType DIESEL_TYPE = registerTypeWithTexture("diesel", true);
    public static final FluidType ETHANE_TYPE = registerTypeWithTexture("ethane", false);
    public static final FluidType POLY_TYPE = registerTypeWithTexture("polyethylene", false);
    public static FlowingFluid OIL;
    public static FlowingFluid OIL_FLOWING;
    public static FlowingFluid DIESEL;
    public static FlowingFluid DIESEL_FLOWING;
    public static FlowingFluid ETHANE;
    public static FlowingFluid POLYETHYLENE;

    static {
        ForgeFlowingFluid.Properties oilProperties = createFluidProperties(() -> OIL_FLUID_TYPE, ModItems.OIL_BUCKET, () -> ModBlocks.FLUID_OIL, () -> OIL, () -> OIL_FLOWING);
        OIL = ALL_FLUIDS.register("oil", new ForgeFlowingFluid.Source(oilProperties));
        OIL_FLOWING = ALL_FLUIDS.register("oil_flowing", new ForgeFlowingFluid.Flowing(oilProperties));

        ForgeFlowingFluid.Properties dieselProperties = createFluidProperties(() -> DIESEL_TYPE, ModItems.BUCKET_DIESEL, () -> ModBlocks.FLUID_DIESEL, () -> DIESEL, () -> DIESEL_FLOWING);
        DIESEL = ALL_FLUIDS.register("diesel", new ForgeFlowingFluid.Source(dieselProperties));
        DIESEL_FLOWING = ALL_FLUIDS.register("diesel_flowing", new ForgeFlowingFluid.Flowing(dieselProperties));

        ForgeFlowingFluid.Properties ethaneProperties = createFluidProperties(() -> ETHANE_TYPE, ModItems.BUCKET_ETHANE, null, () -> ETHANE, () -> ETHANE);
        ForgeFlowingFluid.Properties polyethyleneProperties = createFluidProperties(() -> POLY_TYPE, ModItems.BUCKET_POLYETHYLENE, null, () -> POLYETHYLENE, () -> POLYETHYLENE);

        ETHANE = ALL_FLUIDS.register("ethane", new ForgeFlowingFluid.Source(ethaneProperties));
        POLYETHYLENE = ALL_FLUIDS.register("polyethylene", new ForgeFlowingFluid.Source(polyethyleneProperties));
    }

    private ModFluids() {
    }

    private static ForgeFlowingFluid.Properties createFluidProperties(Supplier<FluidType> fluidType, Item bucketItem, @Nullable Supplier<LiquidBlock> liquidBlock, Supplier<? extends Fluid> still, @Nullable Supplier<? extends Fluid> flowing) {
        return new ForgeFlowingFluid.Properties(fluidType, still, flowing).bucket(() -> bucketItem).block(liquidBlock).explosionResistance(100f);
    }

    @Deprecated
    private static ForgeFlowingFluid.Properties createGasProperties(Supplier<FluidType> fluidType, Supplier<? extends Fluid> still) {
        return new ForgeFlowingFluid.Properties(fluidType, still, () -> null).block(null);
    }

    private static FluidType registerTypeWithTexture(String typeName, boolean includeFlowing) {
        ResourceLocation stillTexture = includeFlowing ? SilentsMechanisms.location("block/" + typeName + "_still") : SilentsMechanisms.location("block/" + typeName);
        ResourceLocation flowingTexture = includeFlowing ? SilentsMechanisms.location("block/" + typeName + "_flowing") : stillTexture;
        return ALL_FLUID_TYPES.register(typeName, new FluidType(FluidType.Properties.create().descriptionId(TranslateUtils.translate("fluid", typeName).getString())) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public ResourceLocation getStillTexture() {
                        return stillTexture;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return flowingTexture;
                    }
                });
            }
        });
    }
}
