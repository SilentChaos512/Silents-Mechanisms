package net.silentchaos512.mechanisms.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.silentchaos512.mechanisms.SilentMechanisms;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, SilentMechanisms.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // TODO
    }
}
