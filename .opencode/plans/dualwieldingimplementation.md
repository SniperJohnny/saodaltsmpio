# Dual Wielding Implementation Plan (Elucidator + Dark Repulsor)
## Target: Minecraft 1.21.1 (Fabric)

---

## Overview
A dual-wielding system that activates **only when**:
- **Elucidator** in mainhand
- **Dark Repulsor** in offhand

**Behavior:**
- Left-click: Normal attack with Elucidator (uses its attack speed/damage)
- Right-click: Attack with Dark Repulsor (uses its attack speed/damage)
- Independent cooldowns per weapon
- 5-tick global cooldown between ANY attacks (prevents same-tick double attack)
- Slot-based only (no persistence if items moved)
- Extensible skill system for future special abilities

---

## Item Stats Reference

| Property | Elucidator (Mainhand) | Dark Repulsor (Offhand) |
|----------|----------------------|------------------------|
| Material | MetallToolMaterial | CrystalliteToolMaterial |
| Base Damage | 4 + 10 attr = 14 | 6 + 6 attr = 12 |
| Attack Speed | -2.4 (attr) | -2.4 (attr) |
| Durability | 3294 | 3094 |
| Speed | 14 | 16 |

---

## File Structure

```
src/main/java/io/sniperjohnny/github/saodaltsmpio/
├── moditems/
│   ├── ElucidatorItem.java          # Custom item for skill extension
│   └── DarkRepulsorItem.java        # Custom item for skill extension
├── dualwield/
│   ├── DualWieldManager.java        # Core logic: detection, cooldowns, attack handling
│   ├── DualWieldSkill.java          # Interface for future skills
│   └── skills/
│       └── BasicAttackSkill.java    # Default normal attack skill
├── networking/
│   └── DualWieldNetworking.java     # Sync attack packets (if needed)
├── client/
│   └── DualWieldClientHandler.java  # Client-side input handling
└── mixin/
    └── DualWieldMixin.java          # Attack interception (LivingEntity/Player)
```

---

## Detailed Implementation

### 1. Custom Item Classes (Extensible)

**ElucidatorItem.java**
```java
public class ElucidatorItem extends SwordItem {
    public ElucidatorItem() {
        super(MetallToolMaterial.INSTANCE,
            new Item.Properties()
                .fireResistant()
                .attributes(SwordItem.createAttributes(MetallToolMaterial.INSTANCE, 10, -2.4F)));
    }
    
    // Override for future skill data
    public ResourceLocation getSkillId() {
        return Saodaltsmpio.id("elucidator_basic");
    }
}
```

**DarkRepulsorItem.java**
```java
public class DarkRepulsorItem extends SwordItem {
    public DarkRepulsorItem() {
        super(CrystalliteToolMaterial.INSTANCE,
            new Item.Properties()
                .fireResistant()
                .attributes(SwordItem.createAttributes(CrystalliteToolMaterial.INSTANCE, 6, -2.4F)));
    }
    
    public ResourceLocation getSkillId() {
        return Saodaltsmpio.id("dark_repulsor_basic");
    }
}
```

**ModWeaponItems.java** - Replace registration:
```java
public static Item ELUCIDATOR = register("elucidator", new ElucidatorItem());
public static Item DARK_REPULSOR = register("dark_repulsor", new DarkRepulsorItem());
```

---

### 2. Dual Wield Skill System

**DualWieldSkill.java** (Interface)
```java
public interface DualWieldSkill {
    ResourceLocation getId();
    
    // Called when player left-clicks (mainhand)
    void onMainhandAttack(Player player, ItemStack mainhand, ItemStack offhand);
    
    // Called when player right-clicks (offhand)
    void onOffhandAttack(Player player, ItemStack mainhand, ItemStack offhand);
    
    // Check if skill can execute (cooldowns, requirements)
    boolean canExecute(Player player, boolean isMainhand);
    
    // Get attack speed for this skill (ticks)
    int getAttackSpeed(Player player, boolean isMainhand);
    
    default boolean isDualWieldActive(Player player) {
        return DualWieldManager.isDualWielding(player);
    }
}
```

**BasicAttackSkill.java** (Default implementation)
```java
public class BasicAttackSkill implements DualWieldSkill {
    private static final int GLOBAL_COOLDOWN_TICKS = 5;
    private int globalCooldown = 0;
    private int mainhandCooldown = 0;
    private int offhandCooldown = 0;
    
    @Override
    public ResourceLocation getId() {
        return Saodaltsmpio.id("basic_dual_wield");
    }
    
    @Override
    public void onMainhandAttack(Player player, ItemStack mainhand, ItemStack offhand) {
        if (!canExecute(player, true)) return;
        
        // Perform attack with Elucidator
        player.attack(mainhand);
        mainhand.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        
        // Set cooldowns
        mainhandCooldown = getAttackSpeed(player, true);
        globalCooldown = GLOBAL_COOLDOWN_TICKS;
    }
    
    @Override
    public void onOffhandAttack(Player player, ItemStack mainhand, ItemStack offhand) {
        if (!canExecute(player, false)) return;
        
        // Perform attack with Dark Repulsor
        player.attack(offhand);
        offhand.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.OFFHAND));
        
        // Set cooldowns
        offhandCooldown = getAttackSpeed(player, false);
        globalCooldown = GLOBAL_COOLDOWN_TICKS;
    }
    
    @Override
    public boolean canExecute(Player player, boolean isMainhand) {
        if (!isDualWieldActive(player)) return false;
        if (globalCooldown > 0) return false;
        if (isMainhand && mainhandCooldown > 0) return false;
        if (!isMainhand && offhandCooldown > 0) return false;
        return true;
    }
    
    @Override
    public int getAttackSpeed(Player player, boolean isMainhand) {
        // Use item's attack speed attribute
        ItemStack stack = isMainhand ? player.getMainHandItem() : player.getOffhandItem();
        double speed = stack.getAttributeModifiers(EquipmentSlot.MAINHAND)
            .get(Attributes.ATTACK_SPEED)
            .stream()
            .mapToDouble(AttributeModifier::getAmount)
            .sum();
        // Convert to ticks: 20 / (4 + speed) roughly
        return Math.max(1, (int)(20 / (4 + speed)));
    }
    
    public void tickCooldowns() {
        if (globalCooldown > 0) globalCooldown--;
        if (mainhandCooldown > 0) mainhandCooldown--;
        if (offhandCooldown > 0) offhandCooldown--;
    }
}
```

---

### 3. Dual Wield Manager

**DualWieldManager.java**
```java
public class DualWieldManager {
    private static final Map<UUID, DualWieldSkill> PLAYER_SKILLS = new HashMap<>();
    private static final BasicAttackSkill DEFAULT_SKILL = new BasicAttackSkill();
    
    public static boolean isDualWielding(Player player) {
        ItemStack mainhand = player.getMainHandItem();
        ItemStack offhand = player.getOffhandItem();
        
        return mainhand.is(ModWeaponItems.ELUCIDATOR) 
            && offhand.is(ModWeaponItems.DARK_REPULSOR);
    }
    
    public static DualWieldSkill getSkill(Player player) {
        if (!isDualWielding(player)) return null;
        return PLAYER_SKILLS.computeIfAbsent(player.getUUID(), uuid -> DEFAULT_SKILL);
    }
    
    public static void tickAll() {
        DEFAULT_SKILL.tickCooldowns();
        // Tick any player-specific skills
    }
    
    public static void onMainhandAttack(Player player) {
        if (!isDualWielding(player)) return;
        DualWieldSkill skill = getSkill(player);
        if (skill != null) {
            skill.onMainhandAttack(player, player.getMainHandItem(), player.getOffhandItem());
        }
    }
    
    public static void onOffhandAttack(Player player) {
        if (!isDualWielding(player)) return;
        DualWieldSkill skill = getSkill(player);
        if (skill != null) {
            skill.onOffhandAttack(player, player.getMainHandItem(), player.getOffhandItem());
        }
    }
    
    public static void clearSkill(Player player) {
        PLAYER_SKILLS.remove(player.getUUID());
    }
}
```

---

### 4. Client Input Handler

**DualWieldClientHandler.java**
```java
public class DualWieldClientHandler {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(DualWieldClientHandler::onClientTick);
    }
    
    private static void onClientTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        
        if (!DualWieldManager.isDualWielding(player)) return;
        
        // Left-click (attack)
        if (minecraft.options.keyAttack.consumeClick()) {
            DualWieldNetworking.sendMainhandAttack();
        }
        
        // Right-click (use/attack)
        if (minecraft.options.keyUse.consumeClick()) {
            // Only intercept if not blocking/shielding
            if (!player.isUsingItem()) {
                DualWieldNetworking.sendOffhandAttack();
            }
        }
    }
}
```

---

### 5. Server Networking

**DualWieldNetworking.java**
```java
public class DualWieldNetworking {
    public static final ResourceLocation ATTACK_MAINHAND = Saodaltsmpio.id("dualwield_attack_main");
    public static final ResourceLocation ATTACK_OFFHAND = Saodaltsmpio.id("dualwield_attack_off");
    
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ATTACK_MAINHAND, (server, player, handler, buf, sender) -> {
            server.execute(() -> DualWieldManager.onMainhandAttack(player));
        });
        
        ServerPlayNetworking.registerGlobalReceiver(ATTACK_OFFHAND, (server, player, handler, buf, sender) -> {
            server.execute(() -> DualWieldManager.onOffhandAttack(player));
        });
    }
    
    public static void sendMainhandAttack() {
        ClientPlayNetworking.send(ATTACK_MAINHAND, PacketByteBufs.create());
    }
    
    public static void sendOffhandAttack() {
        ClientPlayNetworking.send(ATTACK_OFFHAND, PacketByteBufs.create());
    }
}
```

**Register in Saodaltsmpio.onInitialize()**

---

### 6. Server Tick for Cooldowns

**In Saodaltsmpio.onInitialize() or separate event:**
```java
ServerTickEvents.END_SERVER_TICK.register(server -> {
    DualWieldManager.tickAll();
});
```

---

### 7. Attack Interception (Mixin)

**DualWieldMixin.java** - Intercept vanilla attack to prevent dual attacks
```java
@Mixin(Player.class)
public class DualWieldMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci) {
        Player player = (Player)(Object)this;
        if (DualWieldManager.isDualWielding(player)) {
            // Cancel vanilla attack - we handle it via packets
            ci.cancel();
        }
    }
    
    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    private void onUseItem(Level level, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        Player player = (Player)(Object)this;
        if (DualWieldManager.isDualWielding(player) && hand == InteractionHand.OFF_HAND) {
            // Cancel vanilla offhand use - we handle right-click attack
            ci.cancel();
        }
    }
}
```

---

### 8. Future Skill System Extension

To add a special skill (e.g., Dark Repulsor dash on right-click hold):

```java
public class DarkRepulsorDashSkill implements DualWieldSkill {
    private int chargeTicks = 0;
    private static final int MAX_CHARGE = 20;
    
    @Override
    public void onOffhandAttack(Player player, ItemStack mainhand, ItemStack offhand) {
        if (chargeTicks >= MAX_CHARGE) {
            // Execute dash
            Vec3 look = player.getLookAngle();
            player.setDeltaMovement(look.scale(2.0));
            chargeTicks = 0;
        } else {
            // Normal attack
            BASIC_SKILL.onOffhandAttack(player, mainhand, offhand);
        }
    }
    
    @Override
    public boolean canExecute(Player player, boolean isMainhand) {
        // Custom logic
        return true;
    }
}

// Register skill per player (e.g., via capability or keybind)
DualWieldManager.PLAYER_SKILLS.put(player.getUUID(), new DarkRepulsorDashSkill());
```

---

## Registration Checklist

| Component | Registration Location |
|-----------|----------------------|
| ElucidatorItem / DarkRepulsorItem | ModWeaponItems.java |
| DualWieldNetworking | Saodaltsmpio.onInitialize() |
| DualWieldClientHandler | SaodaltsmpioClient.onInitializeClient() |
| DualWieldMixin | saodaltsmpio.mixins.json |
| Server cooldown tick | ServerTickEvents.END_SERVER_TICK |

---

## Testing Checklist

- [ ] Elucidator in mainhand + Dark Repulsor in offhand → dual wield active
- [ ] Only Dark Repulsor in offhand → NO dual wield
- [ ] Only Elucidator in mainhand → NO dual wield
- [ ] Left-click → Elucidator attacks, uses durability, respects attack speed
- [ ] Right-click → Dark Repulsor attacks, uses durability, respects attack speed
- [ ] 5-tick global cooldown prevents same-tick double attack
- [ ] Independent cooldowns per weapon work
- [ ] Swapping items mid-combat disables dual wield immediately
- [ ] Durability decreases on both weapons
- [ ] Enchantments on both weapons apply
- [ ] No vanilla attack/interact interference
- [ ] Skill system extensible (can swap skill implementation)

---

## Risk Mitigation

| Risk | Mitigation |
|------|------------|
| Attack desync | Server-authoritative; client only sends input packets |
| Cooldown exploits | Server validates all cooldowns |
| Vanilla attack conflict | Mixin cancels vanilla attack when dual wield active |
| Skill system complexity | Start simple with BasicAttackSkill; interface allows extension |
| Right-click conflicts | Check `player.isUsingItem()` to allow normal offhand use (food, shield, etc.) |

---

## Anti-Dupe Safeguards (Critical)

### 1. Server-Side Item Validation (Every Attack)
```java
// In DualWieldManager.onMainhandAttack() / onOffhandAttack():
public static void onMainhandAttack(Player player) {
    // RE-VALIDATE items still in correct slots (prevents swap dupes)
    if (!isDualWielding(player)) return;
    
    ItemStack mainhand = player.getMainHandItem();
    ItemStack offhand = player.getOffhandItem();
    
    // Verify exact item instances match (not just type)
    if (!mainhand.is(ModWeaponItems.ELUCIDATOR) || !offhand.is(ModWeaponItems.DARK_REPULSOR)) {
        return; // Items moved/swapped - abort
    }
    
    DualWieldSkill skill = getSkill(player);
    if (skill != null) {
        skill.onMainhandAttack(player, mainhand, offhand);
    }
}
```

### 2. Attack Packet Rate Limiting (Server)
```java
// In DualWieldNetworking register:
private static final Map<UUID, Long> LAST_ATTACK_PACKET = new HashMap<>();
private static final long MIN_PACKET_INTERVAL_MS = 50; // 1 tick = 50ms

ServerPlayNetworking.registerGlobalReceiver(ATTACK_MAINHAND, (server, player, handler, buf, sender) -> {
    long now = System.currentTimeMillis();
    Long last = LAST_ATTACK_PACKET.get(player.getUUID());
    if (last != null && now - last < MIN_PACKET_INTERVAL_MS) return; // Drop spam
    LAST_ATTACK_PACKET.put(player.getUUID(), now);
    
    server.execute(() -> DualWieldManager.onMainhandAttack(player));
});
```

### 3. Inventory Change Listener (Clear Skill on Slot Change)
```java
// In DualWieldMixin or separate event handler:
@SubscribeEvent
public static void onInventoryChanged(PlayerContainerEvent event) {
    Player player = event.getEntity();
    if (DualWieldManager.isDualWielding(player)) return; // Still valid
    DualWieldManager.clearSkill(player); // Items moved - clear cooldowns/skill
}

// Also listen for PlayerInteractEvent.RightClickBlock (opening containers)
@SubscribeEvent
public static void onContainerOpen(PlayerInteractEvent.RightClickBlock event) {
    if (event.getHand() == InteractionHand.MAIN_HAND) return;
    DualWieldManager.clearSkill(event.getEntity()); // Clear on container open
}
```

### 4. Death/Disconnect Cleanup
```java
// In DualWieldMixin or event:
@SubscribeEvent
public static void onPlayerDeath(LivingDeathEvent event) {
    if (event.getEntity() instanceof Player player) {
        DualWieldManager.clearSkill(player);
    }
}

@SubscribeEvent
public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
    DualWieldManager.clearSkill(event.getEntity());
}
```

### 5. Prevent Item Duplication During `hurtAndBreak`
```java
// In BasicAttackSkill.onMainhandAttack():
@Override
public void onMainhandAttack(Player player, ItemStack mainhand, ItemStack offhand) {
    if (!canExecute(player, true)) return;
    
    // CRITICAL: Capture item references BEFORE any mutation
    ItemStack mainhandSnapshot = mainhand.copy();
    ItemStack offhandSnapshot = offhand.copy();
    
    // Perform attack
    player.attack(mainhand);
    
    // Apply durability damage SAFELY
    if (!mainhand.isEmpty() && mainhand.getItem() == mainhandSnapshot.getItem()) {
        mainhand.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }
    // If item was swapped during attack, the above check fails silently - no dupe
    
    mainhandCooldown = getAttackSpeed(player, true);
    globalCooldown = GLOBAL_COOLDOWN_TICKS;
}
```

### 6. Cooldown State Persistence (Prevent Relog Cooldown Bypass)
```java
// Add to player capability or persistent data:
public static class DualWieldData {
    public int globalCooldown = 0;
    public int mainhandCooldown = 0;
    public int offhandCooldown = 0;
    public long lastAttackTime = 0;
}

// Save/load via AttachCapabilitiesEvent<Entity> for Player
```

### 7. Creative Mode Protection
```java
// In DualWieldManager.isDualWielding():
public static boolean isDualWielding(Player player) {
    if (player.isCreative()) return false; // Disable in creative entirely
    
    ItemStack mainhand = player.getMainHandItem();
    ItemStack offhand = player.getOffhandItem();
    
    return mainhand.is(ModWeaponItems.ELUCIDATOR) 
        && offhand.is(ModWeaponItems.DARK_REPULSOR);
}
```

### 8. Attack Validation Checklist (Server-Side)
Every attack must pass ALL:
- [ ] Player not in creative/spectator
- [ ] Elucidator in mainhand (exact item registry match)
- [ ] Dark Repulsor in offhand (exact item registry match)
- [ ] Global cooldown expired (server tick)
- [ ] Weapon-specific cooldown expired (server tick)
- [ ] Player alive, not sleeping, not in dialogue
- [ ] Packet rate limit not exceeded (50ms minimum)

---

## Testing Anti-Dupe Scenarios

| Scenario | Expected Behavior |
|----------|-------------------|
| Rapid click spam (auto-clicker) | Server drops packets via rate limit |
| Swap mainhand to different sword mid-attack | Attack aborted, no durability loss on wrong item |
| Open chest while attacking | Skill cleared, cooldowns reset |
| Disconnect during attack | Skill cleared on logout |
| Death during attack | Skill cleared on death |
| Creative mode with both items | Dual wield disabled |
| Drop item via Q key mid-combat | Skill cleared via inventory change |
| Swap offhand to shield mid-attack | Attack aborted, dual wield disabled |
| Relog to bypass cooldowns | Cooldowns persisted via capability |

---

## Implementation Priority for Anti-Dupe

1. **Must Have (v1)**: Server item validation, packet rate limiting, inventory change listener
2. **Should Have (v1)**: Death/disconnect cleanup, creative mode protection
3. **Nice to Have (v2)**: Cooldown persistence via capability, attack snapshot validation