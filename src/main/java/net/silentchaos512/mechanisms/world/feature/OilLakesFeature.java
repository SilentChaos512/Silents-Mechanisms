package net.silentchaos512.mechanisms.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.LakesFeature;
import net.silentchaos512.utils.MathUtils;

import java.util.Random;

public class OilLakesFeature extends LakesFeature {
    public static final OilLakesFeature INSTANCE = new OilLakesFeature(BlockStateFeatureConfig.field_236455_a_);

    public OilLakesFeature(Codec<BlockStateFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateFeatureConfig config) {
        // Occasionally allow surface lakes
        if (MathUtils.tryPercentage(0.1)) {
            return super.generate(worldIn, generator, rand, pos, config);
        }

        // Place around Y 20-40 normally
        BlockPos newPos = new BlockPos(pos.getX(), rand.nextInt(20) + 20, pos.getZ());
        return super.generate(worldIn, generator, rand, newPos, config);
    }
}
