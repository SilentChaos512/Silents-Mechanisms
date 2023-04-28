package net.silentchaos512.mechanisms.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.Metals;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), SilentsMechanisms.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Metals.Ore metal : Metals.Ore.values()) {
            simpleBlock(metal.getOreBlock());
            simpleBlock(metal.getDeepslateOreBlock());
        }

        for (Metals.OreMetal metal : Metals.OreMetal.values()) {
            simpleBlock(metal.getStorageBlock());
        }

        for (Metals.Alloy metal : Metals.Alloy.values()) {
            simpleBlock(metal.getStorageBlock());
        }
    }
}
