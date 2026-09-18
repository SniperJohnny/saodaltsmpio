package io.sniperjohnny.github.saodaltsmpio.potioneffects;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RagoutRabbitEffects extends MobEffect {

    protected RagoutRabbitEffects() {
        super(MobEffectCategory.BENEFICIAL, 0xe9b8b3);
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // In our case, we just make it return true so that it applies the effect every tick
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof final Player p) {

            p.getFoodData().setFoodLevel(20);         // max hunger
            p.getFoodData().setSaturation(100.0f);         // max hunger

        }

        return super.applyEffectTick(entity, amplifier);
    }
}
