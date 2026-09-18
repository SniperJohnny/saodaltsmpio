package io.sniperjohnny.github.saodaltsmpio.client;

import io.sniperjohnny.github.saodaltsmpio.networking.LiberatorNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LiberatorClientNetworking {
    public static void sendSwapRequest(boolean swordToShield) {
        ClientPlayNetworking.send(new LiberatorNetworking.SwapPayload(swordToShield));
    }
}