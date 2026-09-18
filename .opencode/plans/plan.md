# Liberator Shield/Sword Implementation Plan
## Target: Minecraft 1.21.1 (Fabric)

---

## Overview
A linked shield/sword system where:
- **Liberator Shield** (offhand only): 5000 durability, enchantable, blocks with custom model
- **Liberator Sword** (mainhand): Unbreakable, pre-enchanted, cannot be dropped/stored
- **Toggle**: Right Shift (10 tick cooldown) swaps between forms
- **Linkage**: If shield leaves player (dropped/stored/death), sword is deleted

---

## Implementation Status: ✅ COMPLETE

All items below have been implemented and are in the codebase.

---

## Item Definitions

### Liberator Shield (`liberator`)
| Property | Value |
|----------|-------|
| Base | `ShieldItem` |
| Durability | 5000 |
| Enchantable | Yes (standard shield enchantments) |
| Model | `liberator.json` (normal), `liberator_blocking.json` (blocking) |
| Slot | Offhand only (enforced by logic) |

### Liberator Sword (`liberator_sword`)
| Property | Value |
|----------|-------|
| Base | `SwordItem` |
| Durability | Unbreakable (`Item.Properties().fireResistant().rarity(...)` + override `isDamageable()`) |
| Enchantments | Pre-defined (Sharpness V, Unbreaking III, Mending, etc.) - **NO transfer** |
| Model | `liberator_sword.json` |
| Restrictions | Cannot drop, cannot place in containers, cannot move to creative inventory, deleted on death |

---

## Implemented Files

```
src/main/java/io/sniperjohnny/github/saodaltsmpio/
├── moditems/
│   ├── LiberatorShieldItem.java          ✅ Implemented
│   └── LiberatorSwordItem.java           ✅ Implemented
├── networking/
│   └── LiberatorNetworking.java          ✅ Implemented
├── client/
│   ├── LiberatorKeybinds.java            ✅ Implemented
│   └── LiberatorClientHandler.java       ✅ Implemented
├── mixin/
│   └── LiberatorMixin.java               ✅ Implemented
└── ModWeaponItems.java                   ✅ Updated with registration

src/main/resources/assets/saodaltsmpio/
├── models/item/
│   ├── liberator.json           (exists - used)
│   ├── liberator_sword.json     (exists - used)
│   └── liberator_blocking.json  (exists - used)
└── lang/en_us.json              ✅ Updated with translations
```

---

## Key Implementation Details

### 1. LiberatorShieldItem.java
- Extends `ShieldItem`
- 5000 durability via `Item.Properties().durability(5000)`
- Override `use()` to prevent mainhand usage
- Blocking model via `ItemProperties.register()` in client

### 2. LiberatorSwordItem.java
- Extends `SwordItem`
- Unbreakable: `isDamageable()` → `false`, `canBeHurt()` → `false`
- Restrictions: `onDroppedByPlayer()` destroys stack, `canPlaceInContainer()` → `false`, `canBePickedUp()` → `false`
- Not in creative tab

### 3. Keybinding (Right Shift)
- `LiberatorKeybinds.java` registers `GLFW_KEY_RIGHT_SHIFT`
- Uses `KeyBindingHelper.registerKeyBinding()` (Fabric API)

### 4. Client Handler
- `LiberatorClientHandler.java` with 10-tick cooldown
- Only sends swap packet if shield in offhand
- Registered via `ClientTickEvents.END_CLIENT_TICK`

### 5. Networking
- `LiberatorNetworking.java` with C2S packet
- Server-authoritative swap logic
- Creates fresh pre-enchanted sword on shield→sword swap
- Registered in `Saodaltsmpio.onInitialize()`

### 6. Shield-Sword Linkage (Mixin)
- `LiberatorMixin.java` on `ServerPlayer`
- Intercepts `drop()`, `moveItemStackTo()`, `die()`
- Deletes Liberator Sword from inventory when shield leaves player

### 7. Model Registration
- `ItemProperties.register()` for blocking predicate
- Uses `liberator_blocking.json` when blocking

### 8. Creative Tab & Translations
- Only `LIBERATOR_SHIELD` in creative tab
- `en_us.json` updated with both item names

---

## Dual Wielding (Elucidator + Dark Repulsor)

**Status:** 📋 **PLANNED** - See `dualwieldingimplementation.md`

### Requirements
- Only active when Elucidator in mainhand AND Dark Repulsor in offhand
- Left-click: Elucidator attack (independent cooldown)
- Right-click: Dark Repulsor attack (independent cooldown)
- 5-tick global cooldown between ANY attacks
- Slot-based only (no persistence)
- Extensible skill system for future special abilities

### Planned Files
```
src/main/java/io/sniperjohnny/github/saodaltsmpio/
├── moditems/
│   ├── ElucidatorItem.java          # Custom item for skill extension
│   └── DarkRepulsorItem.java        # Custom item for skill extension
├── dualwield/
│   ├── DualWieldManager.java        # Core logic
│   ├── DualWieldSkill.java          # Skill interface
│   └── skills/
│       └── BasicAttackSkill.java    # Default implementation
├── networking/
│   └── DualWieldNetworking.java     # Attack packets
├── client/
│   └── DualWieldClientHandler.java  # Input handling
└── mixin/
    └── DualWieldMixin.java          # Attack interception
```

---

## Testing Checklist (Liberator)

- [x] Shield in offhand → Right Shift → Sword appears in mainhand
- [x] Sword in mainhand → Right Shift → Shield appears in offhand
- [x] 10 tick cooldown enforced (no rapid toggle)
- [x] Shield dropped → Sword deleted from inventory
- [x] Shield placed in chest → Sword deleted
- [x] Shield moved to any container → Sword deleted
- [x] Player death → Sword deleted, shield drops normally
- [x] Sword cannot be dropped (destroyed on drop attempt)
- [x] Sword cannot be placed in any container
- [x] Sword cannot be moved to creative inventory
- [x] Shield blocking shows `liberator_blocking.json` model
- [x] Shield has 5000 durability, takes damage on block
- [x] Sword is unbreakable (no durability loss)
- [x] Sword has pre-defined enchantments (Sharpness V, etc.)
- [x] Enchantments don't transfer between forms
- [x] Shield enchantable via anvil/enchanting table
- [x] Only shield in creative tab
- [x] Namespaced key detection works with renaming/enchanting

---

## License & Author
- **License:** MIT (updated in `fabric.mod.json` and `LICENSE` file)
- **Author:** SniperJohnny (updated in `fabric.mod.json`)

---

## Dependencies
- Fabric API (already in `fabric.mod.json`)
- No additional mods required

---

## Migration Notes for 1.21.1
- Uses `ToolMaterials` (already used in codebase)
- `Item.Properties().durability()` for shield durability
- `Component.translatable()` for names
- `ServerPlayNetworking` / `ClientPlayNetworking` for packets
- `ClientTickEvents` / `ServerTickEvents` for ticks
- `ItemProperties.register()` for model overrides
- Mixin targets: `ServerPlayer`, `Player` (1.21.1 mappings)
- `KeyBindingHelper` for keybinding registration

---

## Risk Mitigation (Liberator)

| Risk | Mitigation |
|------|------------|
| Desync between client/server | Server-authoritative swap; client only sends request |
| Sword duplication | Server creates fresh sword on swap; no NBT transfer |
| Shield in mainhand abuse | Block `use()` in mainhand; server validates offhand |
| Creative menu exploits | Sword not in creative; `canBePickedUp` = false |
| Death dupe | Server-side death handler deletes sword immediately |
| Renaming/enchanting breaks detection | Uses `.is(Item)` registry check (works with any NBT) |