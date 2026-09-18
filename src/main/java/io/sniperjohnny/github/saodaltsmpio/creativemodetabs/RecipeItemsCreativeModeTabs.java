package io.sniperjohnny.github.saodaltsmpio.creativemodetabs;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModRecipeItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RecipeItemsCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> CUSTOM_RECIPE_ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.fromNamespaceAndPath(Saodaltsmpio.MOD_ID, "recipe_item_group"));

    public static final CreativeModeTab CUSTOM_RECIPE_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModRecipeItems.Crystallite_ore))
            .title(Component.translatable("itemgroup.saodaltsmpio_recipe_item_group"))
            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_RECIPE_ITEM_GROUP_KEY, CUSTOM_RECIPE_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_RECIPE_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.accept(ModRecipeItems.Crystallite_ore);
            itemGroup.accept(ModRecipeItems.Metall);
            itemGroup.accept(ModRecipeItems.Metall_scrap);
            itemGroup.accept(ModRecipeItems.Reinforced_leather_stick);

            // ...
        });
    }
}
