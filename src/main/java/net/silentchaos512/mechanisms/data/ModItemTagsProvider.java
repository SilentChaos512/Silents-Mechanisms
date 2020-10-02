package net.silentchaos512.mechanisms.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModTags;
import net.silentchaos512.mechanisms.item.CraftingItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator generatorIn, ModBlockTagsProvider blockTags) {
        super(generatorIn, blockTags);
    }

    @Override
    protected void registerTags() {
        // Empties
        builder(forgeId("nuggets/coal"));
        builder(forgeId("storage_blocks/charcoal"));

        getOrCreateBuilder(ModTags.Items.PLASTIC).add(CraftingItems.PLASTIC_SHEET.asItem());

        getOrCreateBuilder(ModTags.Items.STEELS)
                .addTag(Metals.ALUMINUM_STEEL.getIngotTag().get())
                .addTag(Metals.BISMUTH_STEEL.getIngotTag().get())
                .addTag(Metals.STEEL.getIngotTag().get());
        getOrCreateBuilder(ModTags.Items.COAL_GENERATOR_FUELS)
                .addTag(ItemTags.COALS)
                .addTag(itemTag(forgeId("nuggets/coal")))
                .addTag(itemTag(forgeId("storage_blocks/charcoal")))
                .addTag(Tags.Items.STORAGE_BLOCKS_COAL);
        copy(ModTags.Blocks.DRYING_RACKS, ModTags.Items.DRYING_RACKS);

        getOrCreateBuilder(ModTags.Items.DUSTS_COAL).add(CraftingItems.COAL_DUST.asItem());

        for (Metals metal : Metals.values()) {
            metal.getOreTag().ifPresent(tag ->
                    copy(tag, metal.getOreItemTag().get()));
            metal.getStorageBlockTag().ifPresent(tag ->
                    copy(tag, metal.getStorageBlockItemTag().get()));
            metal.getChunksTag().ifPresent(tag ->
                    getOrCreateBuilder(tag).add(metal.getChunks().get()));
            metal.getDustTag().ifPresent(tag ->
                    getOrCreateBuilder(tag).add(metal.getDust().get()));
            metal.getIngotTag().ifPresent(tag ->
                    metal.getIngot().ifPresent(item ->
                            getOrCreateBuilder(tag).add(item)));
            metal.getNuggetTag().ifPresent(tag ->
                    metal.getNugget().ifPresent(item ->
                            getOrCreateBuilder(tag).add(item)));
        }

        copy(Tags.Blocks.ORES, Tags.Items.ORES);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        groupBuilder(ModTags.Items.CHUNKS, Metals::getChunksTag);
        groupBuilder(Tags.Items.DUSTS, Metals::getDustTag,
                ModTags.Items.DUSTS_COAL);
        groupBuilder(Tags.Items.INGOTS, Metals::getIngotTag);
        groupBuilder(Tags.Items.NUGGETS, Metals::getNuggetTag);
    }

    @SafeVarargs
    private final void groupBuilder(ITag.INamedTag<Item> tag, Function<Metals, Optional<ITag.INamedTag<Item>>> tagGetter, ITag.INamedTag<Item>... extras) {
        Builder<Item> builder = getOrCreateBuilder(tag);
        for (Metals metal : Metals.values()) {
            tagGetter.apply(metal).ifPresent(builder::addTag);
        }
        for (ITag.INamedTag<Item> extraTag : extras) {
            builder.addTag(extraTag);
        }
    }

    private void builder(ResourceLocation id, IItemProvider... items) {
        getOrCreateBuilder(itemTag(id)).add(Arrays.stream(items).map(IItemProvider::asItem).toArray(Item[]::new));
    }

    private static ITag.INamedTag<Item> itemTag(ResourceLocation id) {
        return ItemTags.makeWrapperTag(id.toString());
    }

    private static ResourceLocation forgeId(String path) {
        return new ResourceLocation("forge", path);
    }

    @Override
    public String getName() {
        return "Silent's Mechanisms - Item Tags";
    }

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    @Override
    public void act(DirectoryCache cache) {
        // Temp fix that removes the broken safety check
        this.tagToBuilder.clear();
        this.registerTags();
        this.tagToBuilder.forEach((p_240524_4_, p_240524_5_) -> {
            JsonObject jsonobject = p_240524_5_.serialize();
            Path path = this.makePath(p_240524_4_);
            if (path == null)
                return; //Forge: Allow running this data provider without writing it. Recipe provider needs valid tags.

            try {
                String s = GSON.toJson((JsonElement) jsonobject);
                String s1 = HASH_FUNCTION.hashUnencodedChars(s).toString();
                if (!Objects.equals(cache.getPreviousHash(path), s1) || !Files.exists(path)) {
                    Files.createDirectories(path.getParent());

                    try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                        bufferedwriter.write(s);
                    }
                }

                cache.recordHash(path, s1);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save tags to {}", path, ioexception);
            }

        });
    }
}
