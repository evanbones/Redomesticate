# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.9.4] - 2026-07-20

### Changed

- Reworked interaction handler to prioritize modded interactions when shift is held.

## [1.9.3] - 2026-07-08

### Fixed

- Fixed crash with Mekanism cardboard boxes.

## [1.9.2] - 2026-07-07

### Fixed

- Pets without beds now drop collar tags on death.
- Fixed Axolotl data being lost when bucketed.
- Fixed client-server desync for active pet collar enchantments.

## [1.9.1] - 2026-07-06

### Changed

- Pet Beds are now waterloggable.

### Fixed

- Properly fixed pets randomly attacking wild versions of themselves.

## [1.9.0] - 2026-06-22

### Changed

- Switched to `command_whitelist` instead of `command_blacklist` for mobs that can use the trinary command system.

### Fixed

- Reworked and improved pet bed syncing/claiming.

## [1.8.4] - 2026-06-22

### Fixed

- Fixed possible ConcurrentModificationException related to pet beds.

## [1.8.3] - 2026-06-14

### Fixed

- Fixed tamed Axolotls sometimes attacking owners.

## [1.8.2] - 2026-06-14

### Changed

- Added Companions entities to the default blacklist.

### Fixed

- Fixed pet beds not properly clearing.
- Fixed issues with modded tamable horses.

## [1.8.1] - 2026-06-07

### Changed

- Added more entities to the default blacklist (@JuanPacoPedrodelaMar).

## [1.8.0] - 2026-06-02

### Added

- Pets can now be configured to have a "home radius" where they won't wander outside.
- Added more modded entries to the default command blacklist.

### Fixed

- Fixed desync with Fox sitting states.

## [1.7.2] - 2026-05-31

### Fixed

- Fixed typo in the Ice and Fire CE integration.

## [1.7.1] - 2026-05-31

### Fixed

- Additional performance improvements.

### Changed

- Increased enchantability of Collar Tags.

## [1.7.0] - 2026-05-31

### Added

- Added config options to enable/disable every enchantment from the mod.

### Fixed

- Performance improvements.
- Fixed tamed Foxes not sitting when trinary commands are disabled.

## [1.6.1] - 2026-05-30

### Added

- Taming and transforming datapacks can now specify required entity data.
    - Format: `"required_data": "{MyData: 1b}"`

### Fixed

- Fixed further issues with Caverns & Chasms Rats.

## [1.6.0] - 2026-05-30

### Fixed

- Fixed tamed Fox behaviour.
- Fixed issues with Ice & Fire Community Edition.

## [1.5.0] - 2026-05-29

### Changed

- Improved Wayward Lantern performance.

### Fixed

- Fixed Cat sitting model.
- Fixed issues with tamed Wolf AI.
- Fixed crash with Caverns and Chasms Rats.
- Fixed pets sometimes changing variants when using Pet Beds.
- Fixed issues with Scorched Guns mobs.
- Fixed race condition with Wayward Lantern teleportation.

## [1.4.0] - 2026-05-26

### Added

- Ocelots are now tamable like they once were in Vanilla, using raw fishes (anything in the `#minecraft:cat_food` tag).
- Added a config option to disable pet teleportation.
- Added a config option to stop pets from attacking when their health dips below a specific threshold (default 20%).

### Changed

- Added collar tags to `#c:enchantables`.
- Adjusted loot table weights.

### Fixed

- Fixed Feather on a Stick held model not being cast.

## [1.3.1] - 2026-05-16

### Fixed

- Fixed baby animals being unkillable.
- Fixed shifting not bypassing swing through pets.

## [1.3.0] - 2026-05-14

### Added

- Added `command_blacklist` tag for entities that shouldn't use the trinary command system.

### Fixed

- Fixed invalid data crash on loot generation.

## [1.2.0] - 2026-05-14

### Fixed

- Fixed buggy Shield behaviour.
- Fixed various behaviour regressions from Domestication Innovation.

## [1.1.0] - 2026-05-14

### Fixed

- Fixed missing Command Drum recipe.
- Pet Beds now follow the vanilla dye order in the creative tab.
- Fixed Pet Beds not acting as Villager workstations on Fabric.
- Fixed missing tags for some curses.

## [1.0.1] - 2026-05-13

### Fixed

- Renamed "disk jockey" to "disc jockey".

## [1.0.0] - 2026-05-13

- Initial release.