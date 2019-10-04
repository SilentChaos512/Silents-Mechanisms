# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Solidifier, a machine that takes one liquid and outputs items. Recipes can be added with data packs.
- Recipe to make plastic pellets from polyethylene (solidifier)
- Recipe to make ice from water (solidifier)
- Recipes to upgrade basic alloy smelters and crushers
### Changed
- Oil lakes are now slightly more common in desert biomes
### Fixed
- Lava generator missing its loot table [#26]
- Other missing loot tables
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
