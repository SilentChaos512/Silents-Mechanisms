package net.silentchaos512.mechanisms.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.common.blocks.dryingracks.DryingRackBlock;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModItems;

import static net.silentchaos512.mechanisms.init.ModBlocks.*;

public class ModItemModelProvider extends ItemModelProvider {


    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator.getPackOutput(), SilentsMechanisms.MODID, fileHelper);
    }

    @Override
    protected void registerModels() {
        DryingRackBlock.ALL_RACKS.forEach(this::blockItemModel);

        blockItemModel(COAL_GENERATOR);
        blockItemModel(ALLOY_MACHINE_FRAME);
        blockItemModel(STONE_MACHINE_FRAME);
        blockItemModel(BATTERY_BOX, "battery_box_0");

        for (Metals.Ore metal : Metals.Ore.values()) {
            basicItem(metal.getRawOreItem());
            blockItemModel(metal.getOreBlock());
            blockItemModel(metal.getDeepslateOreBlock());
        }

        for (Metals.OreMetal metal : Metals.OreMetal.values()) {
            basicItem(metal.getIngot());
            basicItem(metal.getNugget());
            basicItem(metal.getDust());
            blockItemModel(metal.getStorageBlock());
        }
        for (Metals.Alloy metal : Metals.Alloy.values()) {
            basicItem(metal.getIngot());
            basicItem(metal.getNugget());
            basicItem(metal.getDust());
            blockItemModel(metal.getStorageBlock());
        }

        basicItem(ModItems.BEEF_JERKY);
        basicItem(ModItems.CHICKEN_JERKY);
        basicItem(ModItems.CIRCUIT_BOARD);
        basicItem(ModItems.COAL_DUST);
        basicItem(ModItems.COD_JERKY);
        basicItem(ModItems.COMPRESSED_IRON_INGOT);
        basicItem(ModItems.COPPER_DUST);
        basicItem(ModItems.DIESEL_BUCKET);
        basicItem(ModItems.ENERGY_CAPACITY_UPGRADE);
        basicItem(ModItems.ENERGY_EFFICIENCY_UPGRADE);
        basicItem(ModItems.ETHANE_BUCKET);
        basicItem(ModItems.GOLD_DUST);
        basicItem(ModItems.HEATING_ELEMENT);
        basicItem(ModItems.IRON_DUST);
        basicItem(ModItems.MUTTON_JERKY);
        basicItem(ModItems.OIL_BUCKET);
        basicItem(ModItems.OUTPUT_CHANCE_UPGRADE);
        basicItem(ModItems.PLASTIC_PELLETS);
        basicItem(ModItems.PLASTIC_SHEET);
        basicItem(ModItems.POLYETHYLENE_BUCKET);
        basicItem(ModItems.PORK_JERKY);
        basicItem(ModItems.PROCESSING_SPEED_UPGRADE);
        basicItem(ModItems.RABBIT_JERKY);
        basicItem(ModItems.RANGE_UPGRADE);
        basicItem(ModItems.SALMON_JERKY);
        basicItem(ModItems.UPGRADE_BASE);
        basicItem(ModItems.WRENCH);
        basicItem(ModItems.ZOMBIE_LEATHER);
        basicItem(ModItems.ALTERNATOR);
    }

    private void blockItemModel(Block block) {
        String name = NameUtils.fromBlock(block).getPath();
        this.blockItemModel(block, name);
    }

    private void blockItemModel(Block block, String parentName) {
        String name = NameUtils.fromBlock(block).getPath();
        withExistingParent(name, modLoc("block/" + parentName));
    }
}
