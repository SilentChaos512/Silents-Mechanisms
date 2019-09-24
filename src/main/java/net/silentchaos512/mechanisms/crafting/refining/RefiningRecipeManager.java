package net.silentchaos512.mechanisms.crafting.refining;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.network.NetworkEvent;
import net.silentchaos512.mechanisms.SilentMechanisms;
import net.silentchaos512.mechanisms.network.SyncRefiningRecipesPacket;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public final class RefiningRecipeManager implements IResourceManagerReloadListener {
    public static final RefiningRecipeManager INSTANCE = new RefiningRecipeManager();

    public static final Marker MARKER = MarkerManager.getMarker("RefiningRecipeManager");

    private static final String DATA_PATH = "silents_mechanisms_refinement";
    private static final Map<ResourceLocation, RefiningRecipe> MAP = Collections.synchronizedMap(new LinkedHashMap<>());
    private static final Collection<ResourceLocation> ERROR_LIST = new ArrayList<>();

    private RefiningRecipeManager() {}

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        Collection<ResourceLocation> resources = getAllResources(resourceManager);
        if (resources.isEmpty()) return;

        MAP.clear();
        ERROR_LIST.clear();
        SilentMechanisms.LOGGER.info(MARKER, "Reloading refining recipe files");

        for (ResourceLocation id : resources) {
            String path = id.getPath().substring(DATA_PATH.length() + 1, id.getPath().length() - ".json".length());
            ResourceLocation name = new ResourceLocation(id.getNamespace(), path);

            try (IResource iresource = resourceManager.getResource(id)) {
                if (SilentMechanisms.LOGGER.isTraceEnabled()) {
                    SilentMechanisms.LOGGER.trace(MARKER, "Found likely refine recipe: {}, trying to read as {}", id, name);
                }

                JsonObject json = JSONUtils.fromJson(gson, IOUtils.toString(iresource.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
                if (json == null) {
                    SilentMechanisms.LOGGER.error(MARKER, "Could not load refining recipe {} as it's null or empty", name);
                } else if (!CraftingHelper.processConditions(json, "conditions")) {
                    SilentMechanisms.LOGGER.info("Skipping loading refining recipe {} as it's conditions were not met", name);
                } else {
                    RefiningRecipe recipe = RefiningRecipe.deserialize(name, json);
                    addRecipe(recipe);
                }
            } catch (IllegalArgumentException | JsonParseException ex) {
                SilentMechanisms.LOGGER.error(MARKER, "Parsing error loading refining recipe {}", name, ex);
                ERROR_LIST.add(name);
            } catch (IOException ex) {
                SilentMechanisms.LOGGER.error(MARKER, "Could not read refining recipe {}", name, ex);
                ERROR_LIST.add(name);
            }
        }

        SilentMechanisms.LOGGER.info(MARKER, "Registered {} refining recipes", MAP.size());
    }

    private static Collection<ResourceLocation> getAllResources(IResourceManager resourceManager) {
        return new ArrayList<>(resourceManager.getAllResourceLocations(DATA_PATH, s -> s.endsWith(".json")));
    }

    private static void addRecipe(RefiningRecipe recipe) {
        if (MAP.containsKey(recipe.getId())) {
            throw new IllegalStateException("Duplicate refining recipe " + recipe.getId());
        } else {
            MAP.put(recipe.getId(), recipe);
        }
    }

    public static Collection<RefiningRecipe> getValues() {
        synchronized (MAP) {
            return MAP.values();
        }
    }

    @Nullable
    public static RefiningRecipe get(ResourceLocation id) {
        return MAP.get(id);
    }

    @Nullable
    public static RefiningRecipe get(String id) {
        ResourceLocation recipeId = ResourceLocation.tryCreate(id);
        return recipeId != null ? get(recipeId) : null;
    }

    public static void handleSyncPacket(SyncRefiningRecipesPacket packet, Supplier<NetworkEvent.Context> context) {
        MAP.clear();
        packet.getRecipes().forEach(r -> MAP.put(r.getId(), r));
        SilentMechanisms.LOGGER.info("Read {} refining recipes from server", MAP.size());
        context.get().setPacketHandled(true);
    }

    public static Collection<ITextComponent> getErrorMessages(ServerPlayerEntity player) {
        if (!ERROR_LIST.isEmpty()) {
            String listStr = ERROR_LIST.stream().map(ResourceLocation::toString).collect(Collectors.joining(", "));
            return ImmutableList.of(
                    new StringTextComponent("[Silent's Mechanisms] The following refining recipes failed to load, check your log file:")
                            .applyTextStyle(TextFormatting.RED),
                    new StringTextComponent(listStr)
            );
        }
        return ImmutableList.of();
    }
}
