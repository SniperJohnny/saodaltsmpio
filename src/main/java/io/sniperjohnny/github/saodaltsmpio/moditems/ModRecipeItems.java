package io.sniperjohnny.github.saodaltsmpio.moditems;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModRecipeItems {


    public static Item Crystallite_ore = register("crystallite_ore", new Item(new Item.Properties()));
    public static Item Metall = register("metall", new Item(new Item.Properties()));
    public static Item Metall_scrap = register("metall_scrap", new Item(new Item.Properties()));
    public static Item Reinforced_leather_stick = register("reinforced_leather_stick", new Item(new Item.Properties()));




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
