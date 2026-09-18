package io.sniperjohnny.github.saodaltsmpio.moditems;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import io.sniperjohnny.github.saodaltsmpio.toolmaterials.CrystalliteToolMaterial;
import io.sniperjohnny.github.saodaltsmpio.toolmaterials.MetallToolMaterial;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public class ModToolsItems {

   public static Item Crystallite_pickaxe = register("crystallite_pickaxe", new PickaxeItem(CrystalliteToolMaterial.INSTANCE,
            new Item.Properties().fireResistant().attributes(PickaxeItem.createAttributes(CrystalliteToolMaterial.INSTANCE, -1.0f, -2.8F))));
   public static Item Crystallite_axe = register("crystallite_axe", new AxeItem(CrystalliteToolMaterial.INSTANCE,
            new Item.Properties().fireResistant().attributes(AxeItem.createAttributes(CrystalliteToolMaterial.INSTANCE, 3.0f, -3.0F))));
    public static Item Crystallite_shovel = register("crystallite_shovel", new ShovelItem(CrystalliteToolMaterial.INSTANCE,
            new Item.Properties().fireResistant().attributes(ShovelItem.createAttributes(CrystalliteToolMaterial.INSTANCE, -2.0f, -2.1F))));





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
