package net.silentchaos512.mechanisms.data.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.init.Metals;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class SMBiomeModifiers implements DataProvider {
    private final PackOutput.PathProvider outputProvider;

    public SMBiomeModifiers(PackOutput packOutput) {
        this.outputProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "forge/biome_modifier");
    }

    @Override
    @Nonnull
    public CompletableFuture<?> run(@Nonnull CachedOutput pOutput) {
        final List<CompletableFuture<?>> features = new ArrayList<>();

        final JsonArray array = new JsonArray();
        Stream.of(Metals.Ore.values()).map(Metals.Ore::getPlacedFeatureKey).map(ResourceKey::location).map(ResourceLocation::toString).forEach(array::add);

        final JsonObject object = new JsonObject();
        object.addProperty("type", "forge:add_features");
        object.addProperty("biomes", "#" + BiomeTags.IS_OVERWORLD.location());
        object.add("features", array);
        object.addProperty("step", GenerationStep.Decoration.UNDERGROUND_ORES.getName().toLowerCase(Locale.ROOT));

        features.add(DataProvider.saveStable(pOutput, object, this.outputProvider.json(SilentsMechanisms.location("ore_generation"))));

        return CompletableFuture.allOf(features.toArray(CompletableFuture[]::new));
    }

    @Override
    @Nonnull
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
