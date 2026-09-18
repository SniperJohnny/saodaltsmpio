package io.sniperjohnny.github.saodaltsmpio.client;

import io.sniperjohnny.github.saodaltsmpio.moditems.ModWeaponItems;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class LiberatorClientHandler {
    private static int cooldown = 0;
    private static final int COOLDOWN_TICKS = 10;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(LiberatorClientHandler::onClientTick);
    }

    private static void onClientTick(Minecraft minecraft) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        LocalPlayer player = minecraft.player;
        if (player == null) return;

        if (LiberatorKeybinds.TOGGLE_LIBERATOR.consumeClick()) {
            if (hasLiberatorShieldInOffhand(player)) {
                boolean swordToShield = player.getMainHandItem().is(ModWeaponItems.LIBERATOR_SWORD);
                LiberatorClientNetworking.sendSwapRequest(swordToShield);
                cooldown = COOLDOWN_TICKS;
            }
        }
    }

    private static boolean hasLiberatorShieldInOffhand(LocalPlayer player) {
        return player.getOffhandItem().is(ModWeaponItems.LIBERATOR_SHIELD);
    }
}