package io.sniperjohnny.github.saodaltsmpio.moditems;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;

public class LiberatorShieldItem extends ShieldItem {
    public static final int MAX_DURABILITY = 5000;
    public static final ResourceLocation LIBERATOR_KEY = Saodaltsmpio.id("liberator_shield");

    public LiberatorShieldItem() {
        super(new Item.Properties()
            .durability(MAX_DURABILITY)
            .fireResistant());
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    public static boolean isLiberatorShield(ItemStack stack) {
        return stack.is(LiberatorShieldItem.getItem());
    }

    public static LiberatorShieldItem getItem() {
        return (LiberatorShieldItem) BuiltInRegistries.ITEM.get(Saodaltsmpio.id("liberator"));
    }
}