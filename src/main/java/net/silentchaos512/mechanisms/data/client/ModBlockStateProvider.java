package net.silentchaos512.mechanisms.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackBlock;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModBlocks;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, SilentMechanisms.MOD_ID, exFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Silent's Mechanisms - Block States and Models";
    }

    @Override
    protected void registerStatesAndModels() {
        Arrays.stream(Metals.values()).forEach(metal -> {
            metal.getOre().ifPresent(this::simpleBlock);
            metal.getStorageBlock().ifPresent(this::simpleBlock);
        });

        models().withExistingParent("drying_rack", mcLoc("block/block"))
                .texture("0", "#wood")
                .texture("particle", "#wood")
                .element()
                .from(0, 12, 0)
                .to(16, 16, 4)
                .face(Direction.DOWN).uvs(0, 0, 16, 4).texture("#0").end()
                .face(Direction.UP).uvs(0, 0, 16, 4).texture("#0").end()
                .face(Direction.NORTH).uvs(0, 0, 16, 4).texture("#0").end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 4).texture("#0").end()
                .face(Direction.WEST).uvs(0, 0, 4, 4).texture("#0").end()
                .face(Direction.EAST).uvs(0, 0, 4, 4).texture("#0").end()
                .end();

        dryingRack(ModBlocks.ACACIA_DRYING_RACK.get(), "block/acacia_planks");
        dryingRack(ModBlocks.BIRCH_DRYING_RACK.get(), "block/birch_planks");
        dryingRack(ModBlocks.DARK_OAK_DRYING_RACK.get(), "block/dark_oak_planks");
        dryingRack(ModBlocks.JUNGLE_DRYING_RACK.get(), "block/jungle_planks");
        dryingRack(ModBlocks.OAK_DRYING_RACK.get(), "block/oak_planks");
        dryingRack(ModBlocks.SPRUCE_DRYING_RACK.get(), "block/spruce_planks");

        simpleBlock(ModBlocks.STONE_MACHINE_FRAME.get(), models()
                .withExistingParent("stone_machine_frame", modLoc("block/machine_frame"))
                .texture("all", "block/machine_frame/stone"));
        simpleBlock(ModBlocks.ALLOY_MACHINE_FRAME.get(), models()
                .withExistingParent("alloy_machine_frame", modLoc("block/machine_frame"))
                .texture("all", "block/machine_frame/alloy"));
    }

    private void dryingRack(DryingRackBlock block, String texture) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            String name = NameUtils.from(block).getPath();
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .withExistingParent(name, modLoc("block/drying_rack"))
                            .texture("wood", mcLoc(texture)))
                    .rotationY((int) state.get(DryingRackBlock.FACING).getHorizontalAngle())
                    .build();
        }, DryingRackBlock.WATERLOGGED);
    }
}
