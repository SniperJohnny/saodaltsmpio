package io.sniperjohnny.github.saodaltsmpio.mixin;

import io.sniperjohnny.github.saodaltsmpio.moditems.ModWeaponItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class LiberatorMixin {
    @Inject(method = "drop", at = @At("HEAD"))
    private void onDrop(ItemStack stack, boolean randomize, boolean retainOwnership, CallbackInfo ci) {
        if (stack.is(ModWeaponItems.LIBERATOR_SHIELD)) {
            ServerPlayer player = (ServerPlayer)(Object)this;
            deleteLiberatorSword(player);
        }
    }

    @Inject(method = "moveItemStackTo", at = @At("HEAD"))
    private void onMoveToContainer(ItemStack stack, int start, int end, boolean reverse, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ModWeaponItems.LIBERATOR_SHIELD)) {
            ServerPlayer player = (ServerPlayer)(Object)this;
            deleteLiberatorSword(player);
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void onDeath(net.minecraft.world.damagesource.DamageSource source, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        deleteLiberatorSword(player);
    }

    private void deleteLiberatorSword(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ModWeaponItems.LIBERATOR_SWORD)) {
                stack.setCount(0);
                player.getInventory().setChanged();
                break;
            }
        }
    }
}