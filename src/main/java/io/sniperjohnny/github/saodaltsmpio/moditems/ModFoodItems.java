package io.sniperjohnny.github.saodaltsmpio.moditems;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import io.sniperjohnny.github.saodaltsmpio.potioneffects.ModMobEffects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ModFoodItems {


    public static final FoodProperties RAGOUT_RABBIT_FOODCOMPONENT = new FoodProperties.Builder()
            .effect(new MobEffectInstance(ModMobEffects.RAGOUTRABBITEFFECT, 180 * 20), 2.0f)
            .usingConvertsTo(Items.BOWL)
            .build();

    public static Item Rabbit_ragout = register("rabbit_ragout", new Item
            (new Item.Properties().food(RAGOUT_RABBIT_FOODCOMPONENT)));





    public static Item register(String id, Item item) {

        ResourceLocation itemID = ResourceLocation.fromNamespaceAndPath(Saodaltsmpio.MOD_ID, id);

        // Register the item.
        Item registeredItem = Registry.register(BuiltInRegistries.ITEM, itemID, item);

        // Return the registered item!
        return registeredItem;
    }



    public static void initialize() {

    }

}
