package net.silentchaos512.mechanisms.init;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.mechanisms.SilentsMechanisms;

import java.util.function.Supplier;

//Added this one so intellij won't give any stupid warnings for unused registries
@Mod.EventBusSubscriber(modid = SilentsMechanisms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SilentsMechanisms.MODID);
    public static final Table<Metals.Alloy, Metals.AlloyType, ItemRegistryObject<Item>> ALL_ALLOYS;
    public static final Table<Metals.OreMetal, Metals.OreMetalType, ItemRegistryObject<Item>> ALL_ORE_METALS;
    public static final ItemRegistryObject<Item> COMPRESSED_IRON_INGOT = register("compressed_iron_ingot");
    public static final ItemRegistryObject<Item> IRON_CHUNK = register("iron_chunks");
    public static final ItemRegistryObject<Item> IRON_DUST = register("iron_dust");
    public static final ItemRegistryObject<Item> GOLD_CHUNK = register("gold_chunks");
    public static final ItemRegistryObject<Item> GOLD_DUST = register("gold_dust");

    //FOODS & DRYING RACK RESULTS
    public static final ItemRegistryObject<Item> ZOMBIE_LEATHER = register("zombie_leather");
    public static final ItemRegistryObject<Item> BEEF_JERKY = registerFood("beef_jerky", Foods.COOKED_BEEF);
    public static final ItemRegistryObject<Item> PORK_JERKY = registerFood("pork_jerky", Foods.COOKED_PORKCHOP);
    public static final ItemRegistryObject<Item> CHICKEN_JERKY = registerFood("chicken_jerky", Foods.COOKED_CHICKEN);
    public static final ItemRegistryObject<Item> MUTTON_JERKY = registerFood("mutton_jerky", Foods.COOKED_MUTTON);
    public static final ItemRegistryObject<Item> RABBIT_JERKY = registerFood("rabbit_jerky", Foods.COOKED_RABBIT);
    public static final ItemRegistryObject<Item> COD_JERKY = registerFood("cod_jerky", Foods.COOKED_COD);
    public static final ItemRegistryObject<Item> SALMON_JERKY = registerFood("salmon_jerky", Foods.COOKED_SALMON);

    static {
        ALL_ALLOYS = HashBasedTable.create();
        for (Metals.Alloy alloy : Metals.Alloy.values()) {
            for (Metals.AlloyType alloyType : Metals.AlloyType.values()) {
                ALL_ALLOYS.put(alloy, alloyType, register(alloy.toString().toLowerCase() + '_' + alloyType.toString().toLowerCase()));
            }
        }

        ALL_ORE_METALS = HashBasedTable.create();
        for (Metals.OreMetal oreMetal : Metals.OreMetal.values()) {
            for (Metals.OreMetalType oreMetalType : Metals.OreMetalType.values()) {
                ALL_ORE_METALS.put(oreMetal, oreMetalType, register(oreMetal.name().toLowerCase() + '_' + oreMetalType.name().toLowerCase()));
            }
        }
    }

    private static ItemRegistryObject<Item> register(String name) {
        return register(name, () -> new Item(new Item.Properties().tab(SilentsMechanisms.TAB)));
    }

    private static ItemRegistryObject<Item> registerFood(String name, FoodProperties foodProperties) {
        return register(name, () -> new Item(new Item.Properties().tab(SilentsMechanisms.TAB).food(foodProperties)));
    }

    private static <ITEM extends Item> ItemRegistryObject<ITEM> register(String name, Supplier<ITEM> itemSupplier) {
        return new ItemRegistryObject<>(ITEMS.register(name, itemSupplier));
    }

    @SubscribeEvent
    public static void onItemRegistration(RegistryEvent.Register<Item> registration) {
        IForgeRegistry<Item> registry = registration.getRegistry();
        ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).forEach(block -> registry.register(new BlockItem(block, new Item.Properties().tab(SilentsMechanisms.TAB)).setRegistryName(SilentsMechanisms.loc(block.getRegistryName().getPath()))));
    }

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
