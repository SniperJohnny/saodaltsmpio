package io.sniperjohnny.github.saodaltsmpio.moditems;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import io.sniperjohnny.github.saodaltsmpio.toolmaterials.CrystalliteToolMaterial;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.resources.ResourceLocation;

public class LambentLightItem extends SwordItem {
    public static final String SPEED_BOOST_ID = "saodaltsmpio:lambent_light_speed";
    public static final double SPEED_BOOST_AMOUNT = 0.075;
    public static final double REACH_BONUS = 1.0;

    public LambentLightItem() {
        super(CrystalliteToolMaterial.INSTANCE,
            new Item.Properties()
                .fireResistant()
                .attributes(SwordItem.createAttributes(CrystalliteToolMaterial.INSTANCE, 4, -2.4F)
                    // Attack range attribute not available in this version
                    // .add(Attributes.ATTACK_RANGE, new AttributeModifier(
                    //     Saodaltsmpio.id(SPEED_BOOST_ID), REACH_BONUS, AttributeModifier.Operation.ADD_VALUE))
                ));
    }
}