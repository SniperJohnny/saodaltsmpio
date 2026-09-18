package io.sniperjohnny.github.saodaltsmpio.potioneffects;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class ModMobEffects implements ModInitializer {

    public static final Holder<MobEffect> RAGOUTRABBITEFFECT;
    static {
        RAGOUTRABBITEFFECT = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                ResourceLocation.fromNamespaceAndPath(Saodaltsmpio.MOD_ID, "rabbit_ragout"),
                new RagoutRabbitEffects());
    }

    @Override
    public void onInitialize() {

    }
}
