package io.sniperjohnny.github.saodaltsmpio.creativemodetabs;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModFoodItems;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModWeaponItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class FoodItemsCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> SAO_FOOD_ITEM_GROUP_KEY =
            ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ResourceLocation.fromNamespaceAndPath(Saodaltsmpio.MOD_ID, "food_item_group"));

    public static final CreativeModeTab SAO_FOOD_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModFoodItems.Rabbit_ragout))
            .title(Component.translatable("itemgroup.saodaltsmpio_food_item_group"))
            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SAO_FOOD_ITEM_GROUP_KEY
                , SAO_FOOD_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(SAO_FOOD_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.accept(ModFoodItems.Rabbit_ragout);

            // ...
        });
    }
}
