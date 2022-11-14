package net.silentchaos512.mechanisms.init;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.registries.RegisterEvent;
import net.silentchaos512.mechanisms.SilentsMechanisms;
import net.silentchaos512.mechanisms.registration.DirectRegistry;

//Added this suppresses warnings one so intellij won't give any stupid warnings for unused registries
@SuppressWarnings({"unused"})
public final class ModItems {
    public static final DirectRegistry<Item> ITEM_DIRECT_REGISTRY = new DirectRegistry<>();
    // ============== ITEM COLLECTIONS
    public static final Table<Metals.Alloy, Metals.AlloyType, Item> ALL_ALLOYS;
    public static final Table<Metals.OreMetal, Metals.OreMetalType, Item> ALL_ORE_METALS;

    //=============== STANDALONE ITEMS
    public static final Item.Properties GENERAL_PROPERTIES = new Item.Properties().tab(SilentsMechanisms.TAB);

    public static final Item COMPRESSED_IRON_INGOT = register("compressed_iron_ingot");
    public static final Item IRON_CHUNK = register("iron_chunks");
    public static final Item IRON_DUST = register("iron_dust");
    public static final Item GOLD_CHUNK = register("gold_chunks");
    public static final Item GOLD_DUST = register("gold_dust");
    public static final Item COAL_DUST = register("coal_dust");

    //FOODS & DRYING RACK RESULTS
    public static final Item ZOMBIE_LEATHER = register("zombie_leather");
    public static final Item BEEF_JERKY = registerFood("beef_jerky", Foods.COOKED_BEEF);
    public static final Item PORK_JERKY = registerFood("pork_jerky", Foods.COOKED_PORKCHOP);
    public static final Item CHICKEN_JERKY = registerFood("chicken_jerky", Foods.COOKED_CHICKEN);
    public static final Item MUTTON_JERKY = registerFood("mutton_jerky", Foods.COOKED_MUTTON);
    public static final Item RABBIT_JERKY = registerFood("rabbit_jerky", Foods.COOKED_RABBIT);
    public static final Item COD_JERKY = registerFood("cod_jerky", Foods.COOKED_COD);
    public static final Item SALMON_JERKY = registerFood("salmon_jerky", Foods.COOKED_SALMON);

    //ALL ITEMS BELLOWS ARE REGISTERED BUT IS UNUSED ATM, I  WILL ADD ITS FUNCTIONALITIES SOON

    //CRAFTING_COMPONENT
    public static final Item PLASTIC_SHEET = register("plastic_sheet");
    public static final Item PLASTIC_PELLETS = register("plastic_pellets");
    public static final Item HEATING_ELEMENT = register("heating_element");
    public static final Item CIRCUIT_BOARD = register("circuit_board");

    //UPGRADES
    public static final Item UPGRADE_BASE = register("upgrade_case");
    public static final Item ENERGY_CAPACITY_UPGRADE = register("energy_capacity_upgrade");
    public static final Item ENERGY_EFFICIENCY_UPGRADE = register("energy_efficiency_upgrade");
    public static final Item OUTPUT_CHANCE_UPGRADE = register("output_chance_upgrade");
    public static final Item PROCESSING_SPEED_UPGRADE = register("processing_speed_upgrade");
    public static final Item RANGE_UPGRADE = register("range_upgrade");

    //UTILS
    public static final Item WRENCH = register("wrench");
    public static final Item ALTERNATOR = register("alternator");

    //BUCKET ITEMS
    public static final Item OIL_BUCKET = register("oil_bucket", new BucketItem(() -> ModFluids.OIL, getProperties().stacksTo(1)));
    public static final Item BUCKET_DIESEL = register("diesel_bucket", new BucketItem(() -> ModFluids.DIESEL, getProperties().stacksTo(1)));
    public static final Item BUCKET_ETHANE = register("ethane_bucket", new Item(getProperties().stacksTo(1)));
    public static final Item BUCKET_POLYETHYLENE = register("polyethylene_bucket", new Item(getProperties().stacksTo(1)));

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

    private static Item.Properties getProperties() {
        return new Item.Properties().tab(SilentsMechanisms.TAB);
    }

    private static Item register(String name) {
        return register(name, new Item(new Item.Properties().tab(SilentsMechanisms.TAB)));
    }

    private static Item registerFood(String name, FoodProperties foodProperties) {
        return register(name, new Item(new Item.Properties().tab(SilentsMechanisms.TAB).food(foodProperties)));
    }

    private static <ITEM extends Item> Item register(String name, ITEM item) {
        return ITEM_DIRECT_REGISTRY.register(name, item);
    }

    public static void registerAllItems(RegisterEvent.RegisterHelper<Item> helper) {
        ITEM_DIRECT_REGISTRY.registerAll(helper);
        ModBlocks.BLOCK_DIRECT_REGISTRY.getMappings().forEach((block, id) -> {
            if (!(block instanceof LiquidBlock))
                helper.register(id, new BlockItem(block, new Item.Properties().tab(SilentsMechanisms.TAB)));
        });
    }

    public static void staticInitializing() {
    }
}
