# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.8.2] - 2020-10-03
- Ported to 1.16.3. Not compatible with 1.16.2, unfortunately.
### Added
- Infuser. Takes an item and a fluid as inputs, outputs an item.
- Nether gold and ancient debris crushing recipes

## [0.8.1] - 2020-07-23
### Fixed
- Error messages about Silent Gear parts failing to load (removed legacy parts) [#96]

## [0.8.0] - 2020-07-22
- Ported to 1.16.1 (with concurrent 1.15.2 releases for now)
### Added
- Empty canister item, possible fix for [#69]. Existing canisters can be converted by placing them into a crafting grid.
### Changed
- Some recipes tweaked

## [0.7.6] - 2020-07-04
### Added
- Lava to obsidian solidifier recipe
### Changed
- Server config is now common config [#94]

## [0.7.5] - 2020-07-02
### Changed
- Machines can now have items inserted and extracted from any side
### Fixed
- Iron/gold smelting/blasting recipes missing [#90]

## [0.7.4] - 2020-06-30
### Changed
- Switched over to Forge's config system. Configs may get reset.
### Fixed
- Refined iron recipe missing [#89]

## [0.7.3] - 2020-06-28
Regenerated all tags and crushing/smelting recipes with data generators, tweaked some values
### Added
- Tags specifically for ore chunks (`silents_mechanisms:chunks`), removed ore chunks from dust tags [#86]
### Changed
- Updated some textures (futurenp)

## [0.7.2] - 2020-06-02
### Fixed
- CC: Tweaked compatibility updated (requires version 1.88+) [#83, #84]
- Alloy machine frame recipe will now accept tagged steels (Mekanism, etc.) [#81]

## [0.7.1] - 2020-05-05
### Added
- Fog color for oil and diesel (DcZipPL) [#74]
- Pipes (not functioning yet)
### Fixed
- Machine recipes showing their items in the recipe book [#77]
- Crash in WireNetworkManager [#75]
- Blaze rod duplication [#64]

## [0.7.0] - 2020-02-04
Ported to Minecraft 1.15.2

## [0.6.13] - 2020-02-04
### Changed
- Updated more textures (futurenp)
### Fixed
- Wires not connecting to some blocks (more work needed) [#62]
- Drying rack not interacting with some item transfer methods (missing item handler capability) [#55]

## [0.6.12] - 2020-01-21
### Removed
- Some old recipes leftover from earlier versions. These were left in-place because ores were added later on.
### Changed
- Many textures (machines and crafting items mostly) by Futurenp

## [0.6.11] - 2019-12-31
### Added
- Temporary texture for hand pump
- Russian translation (Smollet777)
### Removed
- "Inventory" text from machine screens [#41]
### Changed
- Increased zinc and bismuth defaults to 4 veins per chunk and vein size of 8 (you will need to update your config file to get these changes) [#58]
- Fluid fuel generators (lava, diesel) now consume 100 mB (configurable) of fuel at once. This will reduce lag, but will decrease fuel efficiency when power demand is low. [#48]
    - Consider using CC Tweaked computers to control generators for better efficiency.
- Mixer will no longer put the same fluid in different tanks [#24]
### Fixed
- Ethane and polyethylene buckets missing fluid capability (not working with Refined Storage) [#49, #41]
- Pump not allowing fluids to be extracted [#43]
- Fluid machines allowing extraction from input tanks [#41]
- Water color in machine screens [#41]

## [0.6.10] - 2019-12-27
### Fixed
- Crash when getting energy capabilities of removed blocks [#57]
- Platinum ore missing block tags [#52]
- Wire model incorrect when connecting to block below [#51]
- Ethane and polyethylene are now properly flagged as gaseous (I think) [#49]
- Ore gen master switch config being ignored

## [0.6.9] - 2019-11-20
### Added
- Ethane and polyethylene buckets, for compatibility reasons. These buckets cannot place these fluids as blocks, as the blocks do not exist. [#49, #41]
### Fixed
- Upgrades affecting comparator output level on machines [#47]
- Lava generator not accepting lava canisters via hoppers [#46]
- Machine frame blocks rendering oddly underwater [#32]

## [0.6.8] - 2019-11-04 
### Added
- Config to set chance of oil lakes (can be disabled by setting to 0)
### Changed
- Machines no longer lose progress when out of power (Vaelzan) [#42]
### Fixed
- Possibly fixed a wire-related crash [#38, #45?]

## [0.6.7] - 2019-10-25
### Added
- New textures for diesel/lava generators, refinery, mixer, and solidifier (oitsjustjose) [#39]
### Changed
- Updated German translation (pandory-network) [#34]
### Fixed
- Battery box bottom texture [#40]

## [0.6.6] - 2019-10-20
Significant rewrite of wire code to fix several issues. Watch for bugs!
### Added
- Crusher recipes to create dusts from ingots
### Changed
- Wires now store a small amount of energy (related to [#35, #36])
### Fixed
- Wires creating stack overflows when interacting with Useless Mod energy cables [#35, #36]
- Lava and diesel generators eating buckets [#33]

## [0.6.5] - 2019-10-12
### Added
- Crusher recipe for RF Tools Base's dimensional ore
- Advancement translations (thanks xMGZx) [#31, #17]
- Drying racks occasionally emit smoke particles when working
### Changed
- Wires are slightly more intelligent with their connections, reducing the amount of wrenching needed. Energy producing and storing blocks will default to an "in" connections, while machines will default to "out".
- Standard tier machine speed to 2.0x (up from 1.5x)
- Drying rack can be waterlogged (will not function when waterlogged)
- Set minimum Forge version to 28.1.5
### Fixed
- Drying racks interacting weirdly with hoppers (duplicating items, not updating on client) [#30]
- Single wires not functioning [#11]

## [0.6.4] - 2019-10-10
### Added
- Diesel generator. Produces 120 FE/t, 10 ticks/mB (1.2M FE per bucket).
### Fixed
- Added missing electrum recipes [#27]
- Added missing textures for new metal blocks

## [0.6.3] - 2019-10-05
This update finally completes the plastic production process! All recipes are visible in JEI, as always.
### Added
- Solidifier, a machine that takes one liquid and outputs items. Recipes can be added with data packs.
- Recipe to make plastic pellets from polyethylene (solidifier)
- Recipe to make ice from water (solidifier)
- Recipes to upgrade basic alloy smelters and crushers
- Recipes for most machine upgrades (may change)
- Platinum (with ore), enderium, signalum, and lumium (support for Solar Generation)
### Changed
- Oil lakes are now slightly more common in desert biomes
### Fixed
- Lava generator missing its loot table [#26]
- Other missing loot tables (also added a warning system so the game will yell at me when I forget more in the future)
- Might fix another wire-related crash [#23]

## [0.6.2] - 2019-10-01
### Added
- Mixer, combines up to four fluids into one. Recipes can be added via data packs, as with all other machines.
### Fixed
- Possibly fixed wire placing crash [#23]
- Divide by zero crashes [#22]
- Silent Lib not being correctly listed as a dependency in the mods.toml

## [0.6.1] - 2019-09-27
### Added
- Canisters, stackable fluid containers (64 per slot). This will hold any fluid, but probably won't be recognized by other mods.
    - Fluid color is currently pulled from some JEI code until I write my own solution. If you report colorless fluids in canisters I will make fun of you :p
- Pump, picks up fluids from the world. Can fill buckets or canisters. Requires some FE to work
- Refinery is working
- Refinery can fill buckets or fluid canisters
- Temporary refinery textures
- Plastic pellets and sheets (not craftable yet, we're getting there...)

## [0.6.0] - 2019-09-23
Even more recipe work!
### Added
- Machine upgrades. Not craftable yet, but 3 of the 4 are functioning. Most machines (except basic ones) will have upgrade slots. Only one upgrade is allowed in each slot, but they can stack elsewhere.
- Oil, generates in "lakes", usually underground. No function at the moment.
- Diesel, will be one product of refining oil. Currently no way to produce it.
- Refinery, not functional. Will refine oil into other fluids. Has a "recipe" system which will allow mods or datapacks to use it.
### Changed
- Coal generator now also accepts `forge:nuggets/coal` [#18]
### Fixed
- Ores generating in wrong generation stage [#19]
- Add loot table for basic crusher

## [0.5.2] - 2019-09-08
More recipe progression/rebalancing, more to come
### Added
- Basic crusher
- Coal dust
- Advancements (names and descriptions missing)
### Changed
- Steel-like ingot recipes now require coal dust (meaning you need a basic crusher)
### Fixed
- Crash when opening machines [#14]
- Coal generator being uncraftable with new recipe progression [#13]
- Lava generator not accepting lava if output slot is empty [#12]
- Add loot table for basic alloy smelter

## [0.5.1] - 2019-09-07
### Added
- Basic alloy smelter
- ComputerCraft computers can now get and set the redstone mode on machines
### Changed
- Rebalanced some recipes, more to come
### Fixed
- Actually added steel recipe this time
- Lava generator consuming lava buckets when full

## [0.5.0] - 2019-08-30
Updated for Forge 28.0.74+
### Added
- Lava Generator. Creates Forge Energy from lava (anything in the `minecraft:lava` fluid tag). Should interact with fluid pipes from other mods (not tested yet), or you can place lava buckets into its input slot.
    - Yes, the model is broken and the textures are temporary!
    - Currently generates 500K FE per lava bucket (feedback appreciated)
- Steel recipe [#9]
- ComputerCraft (CC Tweaked) computers can now read energy levels of machines and generators
### Changed
- Generators can now be controlled by redstone like machines (defaults to ignore redstone)
- Coal generator now accepts only coal and charcoal (`minecraft:coals`) and coal blocks (`forge:storage_blocks/coal`) by default. Override the `silents_mechanisms:coal_generator_fuels` item tag if you need to change this.

## [0.4.2] - 2019-08-24
### Added
- Machines can now be controlled by redstone (defaults to ignore redstone). Does not work on generators yet, expect it next update.
- Machine frames (no use yet), Electrum
### Changed
- Machines are now set to their inactive state (and lose progress) when they run out of power
### Fixed
- Alloy smelter matching the wrong recipes

## [0.4.1] - 2019-08-23
### Added
- Wrench item. Rotates some blocks and sets the connection type of wires
- Wires are now implemented. They still require some work, but are usable.
### Changed
- Updated Polish translation (Dolores_McDoodle)
### Fixed
- Blocks losing their inventory when broken [#4]

## [0.4.0] - 2019-08-22
- Big thanks to EnbyChu for making all the textures for this update!
- This is a very large update (over 400 JSON files) because of the addition of ores. Expect recipes to be changes to use the new metals. 
### Added
- Metal ores (with configs, including a convenient "master switch" to disable them all at once)
- Metal blocks and nuggets
- Zinc, bismuth, brass, bismuth brass, aluminum (bauxite), and uranium
### Changed
- Machine textures redone

## [0.3.5] - 2019-08-19
### Added
- Missing crushing recipes for silver and lead
- Blaze rod crushing recipe (double the powder)
- Polish translation (Dolores_McDoodle)

## [0.3.4] - 2019-08-18
### Added
- Crushing recipes for remaining vanilla ores and Useless Mod ores
### Fixed
- Machine energy values displaying incorrectly when playing on a server [#2]
- Invalidate energy capabilities on removed tile entities [#3]

## [0.3.3] - 2019-08-17
### Fixed
- Server crash [#1]

## [0.3.2] - 2019-08-17
### Fixed
- Compressor and electric furnace not dropping when broken (missing loot tables)

## [0.3.1] - 2019-08-16
### Added
- JEI support for drying rack
### Changed
- Tweaked display of some recipes in JEI (cut empty space on crusher category, fix clipping on compressor category)

## [0.3.0] - 2019-08-06
Updated for Forge 28.0.45
### Added
- Drying racks. These have their own recipes, so mods or data packs can add recipes.
- Bronze and invar (made in the alloy smelter)
- Recipe for alloy smelter
- JEI support for alloy smelter
- Some missing models, textures, and localized names
### Changed
- Some item textures (EnbyChu)

## [0.2.0] - 2019-07-22
### Added
- Ore chunks, dusts, and ingots for copper, tin, silver, lead, and nickel. This does NOT add ores! The recipes will load if another mod defines an ore with the correct tag.
- Alloy smelter. Currently not craftable and missing textures, but it works.
## Removed
- Debug text from coal generator and battery box GUI
### Changed
- Coal generator now creates 60 FE/tick (from 25)
- Machines now use 30 FE/tick (from 25)

## [0.1.0] - 2019-07-06
- First release
