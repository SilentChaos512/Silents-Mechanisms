package net.silentchaos512.mechanisms.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.block.dryingrack.DryingRackBlock;
import net.silentchaos512.mechanisms.init.Metals;
import net.silentchaos512.mechanisms.init.ModTags;
import net.silentchaos512.mechanisms.init.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.data.TagsProvider.Builder;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, SilentMechanisms.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.Blocks.DRYING_RACKS).add(Registration.getBlocks(DryingRackBlock.class).toArray(new Block[0]));

        for (Metals metal : Metals.values()) {
            metal.getOreTag().ifPresent(tag ->
                    tag(tag).add(metal.getOre().get()));
            metal.getStorageBlockTag().ifPresent(tag ->
                    tag(tag).add(metal.getStorageBlock().get()));
        }

        groupBuilder(Tags.Blocks.ORES, Metals::getOreTag);
        groupBuilder(Tags.Blocks.STORAGE_BLOCKS, Metals::getStorageBlockTag);
    }

    private void groupBuilder(ITag.INamedTag<Block> tag, Function<Metals, Optional<ITag.INamedTag<Block>>> tagGetter) {
        Builder<Block> builder = tag(tag);
        for (Metals metal : Metals.values()) {
            tagGetter.apply(metal).ifPresent(builder::addTag);
        }
    }

    @Override
    public String getName() {
        return "Silent's Mechanisms - Block Tags";
    }

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    @Override
    public void run(DirectoryCache cache) {
        // Temp fix that removes the broken safety check
        this.builders.clear();
        this.addTags();
        this.builders.forEach((p_240524_4_, p_240524_5_) -> {
            JsonObject jsonobject = p_240524_5_.serializeToJson();
            Path path = this.getPath(p_240524_4_);
            if (path == null)
                return; //Forge: Allow running this data provider without writing it. Recipe provider needs valid tags.

            try {
                String s = GSON.toJson((JsonElement) jsonobject);
                String s1 = SHA1.hashUnencodedChars(s).toString();
                if (!Objects.equals(cache.getHash(path), s1) || !Files.exists(path)) {
                    Files.createDirectories(path.getParent());

                    try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                        bufferedwriter.write(s);
                    }
                }

                cache.putNew(path, s1);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save tags to {}", path, ioexception);
            }

        });
    }
}
