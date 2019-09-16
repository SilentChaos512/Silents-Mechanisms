package net.silentchaos512.mechanisms.network;

import net.minecraft.network.PacketBuffer;
import net.silentchaos512.mechanisms.crafting.refining.RefiningRecipe;
import net.silentchaos512.mechanisms.crafting.refining.RefiningRecipeManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SyncRefiningRecipesPacket extends LoginPacket {
    private List<RefiningRecipe> recipes;

    public SyncRefiningRecipesPacket() {
        this(RefiningRecipeManager.getValues());
    }

    public SyncRefiningRecipesPacket(Collection<RefiningRecipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    public static SyncRefiningRecipesPacket decode(PacketBuffer buffer) {
        SyncRefiningRecipesPacket msg = new SyncRefiningRecipesPacket();
        msg.recipes = new ArrayList<>();
        int count = buffer.readVarInt();

        for (int i = 0; i < count; ++i) {
            msg.recipes.add(RefiningRecipe.read(buffer));
        }

        return msg;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeVarInt(this.recipes.size());
        this.recipes.forEach(recipe -> recipe.write(buffer));
    }

    public List<RefiningRecipe> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }
}
