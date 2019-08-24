# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Machine frames
- Electrum
### Changed
- Machines are now set to their inactive state (and lose progress) when they run out of power

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
