package net.silentchaos512.mechanisms.data.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ForgeMod;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.data.ModWorldGen;
import net.silentchaos512.mechanisms.init.Metals;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SMBiomeModifiers implements DataProvider {
    private final PackOutput.PathProvider outputProvider;
    private final Map<JsonObject, String> map;

    public SMBiomeModifiers(PackOutput packOutput) {
        this.outputProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "forge/biome_modifier");
        this.map = new LinkedHashMap<>();
    }

    @Override
    @Nonnull
    public CompletableFuture<?> run(@Nonnull CachedOutput pOutput) {
        final List<CompletableFuture<?>> features = new ArrayList<>();

        this.register();
        this.save(pOutput, features, this.map);

        return CompletableFuture.allOf(features.toArray(CompletableFuture[]::new));
    }

    private void save(CachedOutput output, final List<CompletableFuture<?>> features, Map<JsonObject, String> map) {
        map.forEach(((jsonObject, s) -> features.add(DataProvider.saveStable(output, jsonObject, this.outputProvider.json(SilentsMechanisms.location(s))))));
    }

    private void register() {
        this.register(Arrays.stream(Metals.Ore.values()).map(Metals.Ore::getPlacedFeatureKey).toList(), GenerationStep.Decoration.UNDERGROUND_ORES, "ore_generation");
        this.register(ModWorldGen.OIL_LAKE_FEATURE, GenerationStep.Decoration.LAKES, "oil_lake");
    }

    @SuppressWarnings("SameParameterValue")
    private void register(ResourceKey<PlacedFeature> feature, GenerationStep.Decoration step, String saveName) {
        register(List.of(feature), step, saveName);
    }

    private void register(List<ResourceKey<PlacedFeature>> featureKeys, GenerationStep.Decoration step, String saveName) {
        final JsonObject object = new JsonObject();
        object.addProperty("type", ForgeMod.ADD_FEATURES_BIOME_MODIFIER_TYPE.getId().toString());
        object.addProperty("biomes", "#" + BiomeTags.IS_OVERWORLD.location());

        if (featureKeys.size() > 1) {
            final JsonArray array = featureKeys.stream().map(ResourceKey::location).map(ResourceLocation::toString).collect(JsonArray::new, JsonArray::add, (JsonArray::addAll));
            object.add("features", array);
        } else {
            object.addProperty("features", featureKeys.get(0).location().toString());
        }

        object.addProperty("step", step.toString().toLowerCase(Locale.ROOT));

        this.map.put(object, saveName);
    }

    @Override
    @Nonnull
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
