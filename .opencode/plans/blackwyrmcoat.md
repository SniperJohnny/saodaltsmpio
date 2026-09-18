# Blackwyrm Coat Implementation Plan
## Target: Minecraft 1.21.1 (Fabric) + GeckoLib

---

## Overview
Kirito's signature Blackwyrm Coat from SAO:
- **Chestplate** with full armor bar (20 armor points)
- **3D GeckoLib model** with walking animation (cape flows in wind)
- **Hides other armor pieces** when worn (only cape visible)
- **Required for dual wielding** (Elucidator + Dark Repulsor)
- **Passive effects**: 10% damage reduction + 0.5 heart regen per 10s (ignores saturation)

---

## Dependencies

### build.gradle
```groovy
dependencies {
    // ... existing ...
    modImplementation "software.bernie.geckolib:geckolib-fabric-1.21:4.2.+"
}
```

### fabric.mod.json
```json
"depends": {
    "geckolib": "*"
}
```

---

## File Structure

```
src/main/java/io/sniperjohnny/github/saodaltsmpio/
├── moditems/
│   ├── armor/
│   │   └── BlackwyrmCoatArmorMaterial.java    # Custom armor material
│   └── BlackwyrmCoatItem.java                  # GeckoLib armor item
├── client/
│   ├── model/
│   │   └── BlackwyrmCoatModel.java             # GeckoLib model class
│   └── renderer/
│       └── BlackwyrmCoatRenderer.java          # GeckoLib renderer
├── capability/
│   └── BlackwyrmCoatCapability.java            # For regen cooldown tracking
├── mixin/
│   └── BlackwyrmCoatMixin.java                 # Hide other armor, dual wield gate
└── ModArmorItems.java                          # New registry class

src/main/resources/assets/saodaltsmpio/
├── geckolib/
│   └── models/
│       └── blackwyrm_coat.geo.json             # Blockbench exported model
├── animations/
│   └── blackwyrm_coat.animation.json           # Walking animation
├── textures/models/armor/
│   └── blackwyrm_coat.png                      # Texture (64x64 or 128x128)
├── models/item/
│   └── blackwyrm_coat.json                     # Item model (generated)
└── lang/en_us.json                             # Translation
```

---

## Implementation Details

### 1. Armor Material (`BlackwyrmCoatArmorMaterial.java`)

```java
public class BlackwyrmCoatArmorMaterial implements ArmorMaterial {
    // Full armor bar: 20 points for chestplate
    // Base: [2, 5, 6, 2] for [boots, leggings, chest, helmet]
    // Multiplier: chest = 6 * 3.33 ≈ 20
    // Or use custom values directly
    
    @Override public int getDurabilityForType(ArmorItem.Type type) { return 5000; }
    @Override public int getDefenseForType(ArmorItem.Type type) { 
        return switch(type) {
            case CHESTPLATE -> 20; // Full armor bar
            default -> 0;
        };
    }
    @Override public int getEnchantmentValue() { return 25; }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_LEATHER; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.of(ModRecipeItems.Crystallite_ore); }
    @Override public String getName() { return "blackwyrm_coat"; }
    @Override public float getToughness() { return 3.0f; } // Netherite-level
    @Override public float getKnockbackResistance() { return 0.1f; }
}
```

### 2. GeckoLib Armor Item (`BlackwyrmCoatItem.java`)

```java
public class BlackwyrmCoatItem extends GeoArmorItem {
    public BlackwyrmCoatItem() {
        super(BlackwyrmCoatArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void createGeoClientCache(GeoItemClientCache cache) {
        cache.addModel(new BlackwyrmCoatModel());
        cache.addAnimationController(this::controller);
    }

    private <T extends GeoAnimatable> void controller(AnimationState<T> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
    }

    // Disable vanilla armor rendering
    @Override
    public HumanoidModel<?> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
        return null; // We render via GeckoLib renderer
    }
}
```

### 3. GeckoLib Model (`BlackwyrmCoatModel.java`)

```java
public class BlackwyrmCoatModel extends GeoModel<BlackwyrmCoatItem> {
    @Override public ResourceLocation getModelResource(BlackwyrmCoatItem object) {
        return Saodaltsmpio.id("geo/blackwyrm_coat.geo.json");
    }
    @Override public ResourceLocation getTextureResource(BlackwyrmCoatItem object) {
        return Saodaltsmpio.id("textures/models/armor/blackwyrm_coat.png");
    }
    @Override public ResourceLocation getAnimationResource(BlackwyrmCoatItem object) {
        return Saodaltsmpio.id("animations/blackwyrm_coat.animation.json");
    }
}
```

### 4. Renderer (`BlackwyrmCoatRenderer.java`)

```java
public class BlackwyrmCoatRenderer extends GeoArmorRenderer<BlackwyrmCoatItem> {
    public BlackwyrmCoatRenderer() {
        super(new BlackwyrmCoatModel());
    }
}
```

**Register in `SaodaltsmpioClient.onInitializeClient()`:**
```java
GeoItemRenderer.registerArmorRenderer(ModArmorItems.BLACKWYRM_COAT, BlackwyrmCoatRenderer::new);
```

### 5. Blockbench Model Requirements

**Model: `blackwyrm_coat.geo.json`**
- Single geometry: "cape" bone
- Parent to "body" bone
- Offset to hang from shoulders
- UV mapping for 64x64 or 128x128 texture

**Animation: `blackwyrm_coat.animation.json`**
```json
{
  "animations": {
    "idle": { "loop": true, "bones": { "cape": { "rotation": [0, 0, 0] } } },
    "walk": { "loop": true, "bones": { 
      "cape": { "rotation": ["variable.query.life_time * 100", "Math.sin(variable.query.life_time * 20) * 15", 0] } 
    }}
  }
}
```

---

## Special Effects

### A. Dual Wield Gate (Mixin on DualWieldManager)

```java
// In DualWieldManager.isDualWielding():
public static boolean isDualWielding(Player player) {
    // ... existing checks ...
    // ADD: Must wear Blackwyrm Coat
    if (!player.getItemBySlot(EquipmentSlot.CHEST).is(ModArmorItems.BLACKWYRM_COAT)) {
        return false;
    }
    return true;
}
```

### B. 10% Damage Reduction + Regen (Mixin on LivingEntity)

```java
@Mixin(LivingEntity.class)
public class BlackwyrmCoatMixin {
    // Damage reduction
    @Inject(method = "hurt", at = @At("HEAD"))
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity instanceof Player player && isWearingBlackwyrmCoat(player)) {
            // Apply 10% reduction after armor
            // Note: This runs before armor calculation, so we need post-armor hook
        }
    }

    // Better: Use LivingDamageEvent or LivingHurtEvent
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player && isWearingBlackwyrmCoat(player)) {
            event.setAmount(event.getAmount() * 0.9f); // 10% reduction
        }
    }

    // Regen every 10 seconds (200 ticks)
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onAiStep(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity instanceof Player player && isWearingBlackwyrmCoat(player)) {
            if (player.level().getGameTime() % 200 == 0) {
                player.heal(1.0f); // Half heart
            }
        }
    }

    private static boolean isWearingBlackwyrmCoat(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).is(ModArmorItems.BLACKWYRM_COAT);
    }
}
```

### C. Hide Other Armor (Mixin on PlayerRenderer or LayerRenderer)

```java
@Mixin(ArmorLayer.class) // or HumanoidArmorLayer
public class HideArmorMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(PoseStack stack, MultiBufferSource buffer, int light, 
                          LivingEntity entity, float limbSwing, float limbSwingAmount, 
                          float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, 
                          CallbackInfo ci) {
        if (entity instanceof Player player && isWearingBlackwyrmCoat(player)) {
            // Only render chestplate layer (index 1 = chestplate)
            // Cancel other layers: helmet(0), leggings(2), boots(3)
            // This requires checking which layer is being rendered
        }
    }
}
```

**Alternative:** Override `getArmorTexture` to return empty for other slots when coat worn.

---

## Registration

### ModArmorItems.java (new)
```java
public class ModArmorItems {
    public static final ArmorMaterial BLACKWYRM_COAT_MATERIAL = new BlackwyrmCoatArmorMaterial();
    public static Item BLACKWYRM_COAT = register("blackwyrm_coat", 
        new BlackwyrmCoatItem(BLACKWYRM_COAT_MATERIAL, ArmorItem.Type.CHESTPLATE, ...));

    // Register in Saodaltsmpio.onInitialize()
}
```

### Creative Tab
Add to `WeaponItemsCreativeModeTabs` or create new `ArmorItemsCreativeModeTabs`.

### Translation
```json
"item.saodaltsmpio.blackwyrm_coat": "Blackwyrm Coat"
```

---

## GeckoLib Setup Checklist

- [ ] Add GeckoLib dependency to build.gradle
- [ ] Add `geckolib` to fabric.mod.json depends
- [ ] Run `./gradlew genSources` after adding dependency
- [ ] Create Blockbench model with GeckoLib format
- [ ] Export `.geo.json`, `.animation.json`, texture
- [ ] Place in `src/main/resources/assets/saodaltsmpio/geckolib/models/`
- [ ] Create model/renderer classes
- [ ] Register renderer in client init

---

## Testing Checklist

- [ ] Coat appears in creative tab
- [ ] 3D model renders on player (not vanilla 2D)
- [ ] Walking animation plays (cape flows)
- [ ] Idle animation plays when standing
- [ ] Other armor pieces hidden when coat worn
- [ ] Armor value: 20 points (full bar)
- [ ] 10% damage reduction works
- [ ] Regen: 0.5 heart per 10 seconds (ignores saturation)
- [ ] Dual wielding disabled without coat
- [ ] Dual wielding works with coat + both swords
- [ ] Texture loads correctly
- [ ] No conflicts with Liberator system
- [ ] Durability: 5000 (or configured)

---

## Risk Mitigation

| Risk | Mitigation |
|------|------------|
| GeckoLib version mismatch | Pin exact version in gradle.properties |
| Model not rendering | Verify bone names match animation JSON |
| Other armor still visible | Test with full armor set; debug layer indices |
| Regen too OP | Configurable cooldown/amount |
| Dual wield gate breaks existing | Test both with/without coat |
| Animation stutter | Optimize animation controller logic |