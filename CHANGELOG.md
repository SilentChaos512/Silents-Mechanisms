# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
