package io.sniperjohnny.github.saodaltsmpio.networking;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModWeaponItems;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class LiberatorNetworking {
    public static final ResourceLocation SWAP_PACKET_ID = Saodaltsmpio.id("liberator_swap");

    public record SwapPayload(boolean swordToShield) implements CustomPacketPayload {
        public static final Type<SwapPayload> TYPE = new Type<>(SWAP_PACKET_ID);
        public static final StreamCodec<FriendlyByteBuf, SwapPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> buf.writeBoolean(payload.swordToShield),
            buf -> new SwapPayload(buf.readBoolean())
        );

        @Override
        public Type<SwapPayload> type() {
            return TYPE;
        }
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(SwapPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> handleSwap(context.player(), payload.swordToShield()));
        });
    }

    private static void handleSwap(ServerPlayer player, boolean swordToShield) {
        if (swordToShield) {
            ItemStack mainhand = player.getMainHandItem();
            if (!mainhand.is(ModWeaponItems.Liberator_Sword)) return;

            ItemStack shield = new ItemStack(ModWeaponItems.Liberator_Shield);
            player.setItemInHand(InteractionHand.OFF_HAND, shield);
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        } else {
            ItemStack offhand = player.getOffhandItem();
            if (!offhand.is(ModWeaponItems.Liberator_Shield)) return;

            ItemStack sword = createPreEnchantedLiberatorSword();
            player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            player.setItemInHand(InteractionHand.MAIN_HAND, sword);
        }
    }

    private static ItemStack createPreEnchantedLiberatorSword() {
        return new ItemStack(ModWeaponItems.Liberator_Sword);
    }
}