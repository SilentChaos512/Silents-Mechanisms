package net.silentchaos512.mechanisms.init;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
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

//Added this suppresses warnings one so intellij won't give any stupid warnings for unused registries
@Mod.EventBusSubscriber(modid = SilentsMechanisms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings({"unused", "deprecation"})
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SilentsMechanisms.MODID);
    // ============== ITEM COLLECTIONS
    public static final Table<Metals.Alloy, Metals.AlloyType, ItemRegistryObject<Item>> ALL_ALLOYS;
    public static final Table<Metals.OreMetal, Metals.OreMetalType, ItemRegistryObject<Item>> ALL_ORE_METALS;

    //=============== STANDALONE ITEMS
    public static final Item.Properties GENERAL_PROPERTIES = new Item.Properties().tab(SilentsMechanisms.TAB);

    public static final ItemRegistryObject<Item> COMPRESSED_IRON_INGOT = register("compressed_iron_ingot");
    public static final ItemRegistryObject<Item> IRON_CHUNK = register("iron_chunks");
    public static final ItemRegistryObject<Item> IRON_DUST = register("iron_dust");
    public static final ItemRegistryObject<Item> GOLD_CHUNK = register("gold_chunks");
    public static final ItemRegistryObject<Item> GOLD_DUST = register("gold_dust");
    public static final ItemRegistryObject<Item> COAL_DUST = register("coal_dust");

    //FOODS & DRYING RACK RESULTS
    public static final ItemRegistryObject<Item> ZOMBIE_LEATHER = register("zombie_leather");
    public static final ItemRegistryObject<Item> BEEF_JERKY = registerFood("beef_jerky", Foods.COOKED_BEEF);
    public static final ItemRegistryObject<Item> PORK_JERKY = registerFood("pork_jerky", Foods.COOKED_PORKCHOP);
    public static final ItemRegistryObject<Item> CHICKEN_JERKY = registerFood("chicken_jerky", Foods.COOKED_CHICKEN);
    public static final ItemRegistryObject<Item> MUTTON_JERKY = registerFood("mutton_jerky", Foods.COOKED_MUTTON);
    public static final ItemRegistryObject<Item> RABBIT_JERKY = registerFood("rabbit_jerky", Foods.COOKED_RABBIT);
    public static final ItemRegistryObject<Item> COD_JERKY = registerFood("cod_jerky", Foods.COOKED_COD);
    public static final ItemRegistryObject<Item> SALMON_JERKY = registerFood("salmon_jerky", Foods.COOKED_SALMON);

    //ALL ITEMS BELLOWS ARE REGISTERED BUT IS UNUSED ATM, I  WILL ADD ITS FUNCTIONALITIES SOON

    //CRAFTING_COMPONENT
    public static final ItemRegistryObject<Item> PLASTIC_SHEET = register("plastic_sheet");
    public static final ItemRegistryObject<Item> PLASTIC_PELLETS = register("plastic_pellets");
    public static final ItemRegistryObject<Item> HEATING_ELEMENT = register("heating_element");
    public static final ItemRegistryObject<Item> CIRCUIT_BOARD = register("circuit_board");

    //UPGRADES
    public static final ItemRegistryObject<Item> UPGRADE_BASE = register("upgrade_case");
    public static final ItemRegistryObject<Item> ENERGY_CAPACITY_UPGRADE = register("energy_capacity_upgrade");
    public static final ItemRegistryObject<Item> ENERGY_EFFICIENCY_UPGRADE = register("energy_efficiency_upgrade");
    public static final ItemRegistryObject<Item> OUTPUT_CHANCE_UPGRADE = register("output_chance_upgrade");
    public static final ItemRegistryObject<Item> PROCESSING_SPEED_UPGRADE = register("processing_speed_upgrade");
    public static final ItemRegistryObject<Item> RANGE_UPGRADE = register("range_upgrade");

    //UTILS
    public static final ItemRegistryObject<Item> WRENCH = register("wrench");
    public static final ItemRegistryObject<Item> ALTERNATOR = register("alternator");

    //BUCKET ITEMS
    public static final ItemRegistryObject<BucketItem> OIL_BUCKET = register("oil_bucket", () -> new BucketItem(ModFluids.OIL, GENERAL_PROPERTIES.stacksTo(1)));
    public static final ItemRegistryObject<BucketItem> BUCKET_DIESEL = register("diesel_bucket", () -> new BucketItem(ModFluids.DIESEL, GENERAL_PROPERTIES.stacksTo(1)));
    public static final ItemRegistryObject<BucketItem> BUCKET_ETHANE = register("ethane_bucket", () -> new BucketItem(ModFluids.ETHANE, GENERAL_PROPERTIES.stacksTo(1)));
    public static final ItemRegistryObject<BucketItem> BUCKET_POLYETHYLENE = register("polyethylene_bucket", () -> new BucketItem(ModFluids.POLYETHYLENE, GENERAL_PROPERTIES.stacksTo(1)));

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
        ModBlocks.BLOCK_REGISTRY.getEntries().stream().map(Supplier::get).forEach(block -> {
            if (!(block instanceof LiquidBlock))
                registry.register(new BlockItem(block, new Item.Properties().tab(SilentsMechanisms.TAB)).setRegistryName(SilentsMechanisms.loc(block.getRegistryName().getPath())));
        });
    }

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
