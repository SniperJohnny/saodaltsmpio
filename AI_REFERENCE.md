# SAO Daltsmpio Mod - AI Reference File
## Project: saodaltsmpio | Minecraft 1.21.1 | Fabric | Java 21

---

## Mod Identity
- **MOD_ID**: `saodaltsmpio`
- **Author**: SniperJohnny
- **License**: MIT
- **Package**: `io.sniperjohnny.github.saodaltsmpio`

---

## Implemented Features

### 1. Liberator Shield/Sword System ✅ COMPLETE
**Items:**
- `liberator` (Shield) - 5000 durability, enchantable, offhand-only, blocking model
- `liberator_sword` (Sword) - Unbreakable, pre-enchanted (Sharpness V, Unbreaking III, Mending, Fire Aspect II, Looting III), cannot drop/container/creative

**Mechanics:**
- Right Shift (10-tick cooldown) toggles between forms
- Shield must be in offhand to work
- Shield drop/container/death → deletes sword from inventory
- Namespaced detection (works with renaming/enchanting)

**Files:**
- `moditems/LiberatorShieldItem.java`
- `moditems/LiberatorSwordItem.java`
- `networking/LiberatorNetworking.java`
- `client/LiberatorKeybinds.java`
- `client/LiberatorClientHandler.java`
- `mixin/LiberatorMixin.java`
- `ModWeaponItems.java` (registration)

### 2. Lambent Light ✅ COMPLETE
**Item:** `lambent_light` (Sword)
- Material: CrystalliteToolMaterial
- Damage: 4 (lower than Elucidator's 10, Dark Repulsor's 6)
- Attack Speed: -2.4
- Reach: +1.0 block (via ATTACK_RANGE attribute)
- Speed Boost: +7.5% movement speed when held (via attribute modifier)

**File:** `moditems/LambentLightItem.java`

### 3. Existing Weapons
- `elucidator` - MetallToolMaterial, damage 10, speed -2.4
- `dark_repulsor` - CrystalliteToolMaterial, damage 6, speed -2.4

---

## Planned Features

### Dual Wielding (Elucidator + Dark Repulsor) 📋 PLANNED
**Activation:** Elucidator in mainhand + Dark Repulsor in offhand ONLY
- Left-click: Elucidator attack (independent cooldown)
- Right-click: Dark Repulsor attack (independent cooldown)
- 5-tick global cooldown between ANY attacks
- Slot-based only (no persistence)
- Extensible skill system for future special abilities
- Anti-dupe safeguards: server validation, packet rate limiting, inventory listeners

**Planned Files:**
- `moditems/ElucidatorItem.java`, `DarkRepulsorItem.java`
- `dualwield/DualWieldManager.java`, `DualWieldSkill.java`, `skills/BasicAttackSkill.java`
- `networking/DualWieldNetworking.java`
- `client/DualWieldClientHandler.java`
- `mixin/DualWieldMixin.java`

---

## Naming Conventions
- Items: lowercase with underscores (`liberator`, `dark_repulsor`, `lambent_light`)
- Classes: PascalCase (`LiberatorShieldItem`, `LambentLightItem`)
- Constants: UPPER_SNAKE_CASE (`MAX_DURABILITY`, `SPEED_BOOST_AMOUNT`)
- ResourceLocations: `Saodaltsmpio.id("path")` → `saodaltsmpio:path`
- Translations: `item.saodaltsmpio.<item_id>`

---

## Key Patterns
- **Item registration**: `ModWeaponItems.register(id, new ItemClass())`
- **Attributes**: Use `SwordItem.createAttributes(material, damage, speed)` + `.add(Attribute, Modifier)`
- **Networking**: `ServerPlayNetworking` / `ClientPlayNetworking` with `PacketByteBufs`
- **Keybinds**: `KeyBindingHelper.registerKeyBinding()` in client init
- **Client ticks**: `ClientTickEvents.END_CLIENT_TICK.register()`
- **Server ticks**: `ServerTickEvents.END_SERVER_TICK.register()`
- **Model overrides**: `ItemProperties.register(item, ResourceLocation, predicate)`
- **Mixins**: Target `ServerPlayer`, `Player` in `saodaltsmpio.mixins.json`

---

## Anti-Dupe Rules (Critical)
1. Server re-validates items in slots before every action
2. Packet rate limiting (50ms minimum)
3. Inventory/container listeners clear state
4. Death/disconnect cleanup
5. Creative mode disables special mechanics
6. Snapshot items before `hurtAndBreak()`, verify after

---

## Creative Tabs
- `saodaltsmpio_weapon_item_group` - Weapons (Elucidator, Dark Repulsor, Liberator Shield, Lambent Light)
- `saodaltsmpio_tool_item_group` - Tools
- `saodaltsmpio_food_item_group` - Food
- `saodaltsmpio_recipe_item_group` - Recipe Ingredients

---

## Translation Keys (en_us.json)
```
item.saodaltsmpio.elucidator=Elucidator
item.saodaltsmpio.dark_repulsor=Dark Repulsor
item.saodaltsmpio.liberator=Liberator
item.saodaltsmpio.liberator_sword=Liberator Sword
item.saodaltsmpio.lambent_light=Lambent Light
```

---

## Models (Exist in assets/saodaltsmpio/models/item/)
- `liberator.json` / `liberator_blocking.json` - Shield models
- `liberator_sword.json` - Sword model
- `elucidator.json`, `dark_repulsor.json` - Existing weapons
- `lambent_light.json` - NEEDS CREATION

---

## Update Log
| Date | Change | By |
|------|--------|----|
| 2026-09-18 | Liberator system implemented | AI |
| 2026-09-18 | Lambent Light implemented | AI |
| 2026-09-18 | Dual wielding planned + anti-dupe spec | AI |

---

**AI INSTRUCTION**: Update this file after any implementation. Keep it concise. No verbose comments.