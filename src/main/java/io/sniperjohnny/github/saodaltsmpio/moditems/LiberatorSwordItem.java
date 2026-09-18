package io.sniperjohnny.github.saodaltsmpio.moditems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public class LiberatorSwordItem extends SwordItem {
    public LiberatorSwordItem() {
        super(Tiers.NETHERITE,
            new Item.Properties()
                .fireResistant()
                .rarity(Rarity.EPIC)
                .attributes(SwordItem.createAttributes(Tiers.NETHERITE, 10, -2.4F)));
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public void onDroppedByPlayer(ItemStack stack, Player player) {
        stack.setCount(0);
    }

    public boolean canBePickedUp(Entity entity) {
        return false;
    }
}