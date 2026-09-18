package io.sniperjohnny.github.saodaltsmpio.moditems;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import io.sniperjohnny.github.saodaltsmpio.toolmaterials.CrystalliteToolMaterial;
import io.sniperjohnny.github.saodaltsmpio.toolmaterials.MetallToolMaterial;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModWeaponItems {

    public static Item Elucidator = register("elucidator", new SwordItem(MetallToolMaterial.INSTANCE,
            new Item.Properties().fireResistant().attributes(SwordItem.createAttributes(MetallToolMaterial.INSTANCE, 10, -2.4F))));

    public static Item DARK_REPULSOR = register("dark_repulsor", new SwordItem(CrystalliteToolMaterial.INSTANCE,
            new Item.Properties().fireResistant().attributes(SwordItem.createAttributes(CrystalliteToolMaterial.INSTANCE, 6, -2.4F))));

    public static Item LIBERATOR_SHIELD = register("liberator", new LiberatorShieldItem());

    public static Item LIBERATOR_SWORD = register("liberator_sword", new LiberatorSwordItem());

    public static Item LAMBENT_LIGHT = register("lambent_light", new LambentLightItem());

    public static ItemStack LIBERATOR_SWORD_TEMPLATE;





    public static Item register(String id, Item item) {

        ResourceLocation itemID = ResourceLocation.fromNamespaceAndPath(Saodaltsmpio.MOD_ID, id);

            // Register the item.
        Item registeredItem = Registry.register(BuiltInRegistries.ITEM, itemID, item);

            // Return the registered item!
        return registeredItem;
        }



    public static void initialize() {
        LIBERATOR_SWORD_TEMPLATE = new ItemStack(LIBERATOR_SWORD);
    }

}
