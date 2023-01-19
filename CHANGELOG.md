## 1.0.0.16
* Invert check for ground when filling logs under for trees.

## 1.0.0.15
* Use BlockPredicates to determine whether we've hit ground and to determine whether leaves can place at a position.

## 1.0.0.14
* Get logs from the trunk palette correctly.
* Store leaves & log targets in a ObjectOpenHashSet in `TreeFromStructureNBTFeature`.

## 1.0.0.13
* Add the ability to have several leaves & log targets in `TreeFromStructureNBTFeature`.

## 1.0.0.12
* Fix StructureBoxEditor

## 1.0.0.11
* Add ability to edit structure boxes with a golden axe. Use LEFT_CTRL + SCROLL_WHEEL when to move the box in that direction, use LEFT_SHIFT + SCROLL_WHEEL to inflate the box in that direction.

## 1.0.0.10
* Place leaves on trunks.

## 1.0.0.9
* Fix canopy anchor pos.

## 1.0.0.8
* Add the ability to use yellow wool to anchor canopies from trunks.

## 1.0.0.7
* Switch to Access Transformers/Wideners.

## 1.0.0.6
* Use correct `/corgilib worldregistryexport` command data export path.
* Fix `RegistryAccessor` mixin being called exclusively on clients. Fixes servers crashing.

## 1.0.0.5
* Fix & optimize `/corgilib worldRegistryExport` command.

## 1.0.0.4
* Register `AnyCondition` condition.
* Better registry ID for `IsMobCategoryCondition`.

## 1.0.0.3
* Prevent duplicate initializations on fabric.

## 1.0.0.2
* Relocate Jankson on forge build.

## 1.0.0.1
* Allow CorgiLib Fabric to be initialized from elsewhere.
* Clean up network package.

## 1.0.0
* First Release.