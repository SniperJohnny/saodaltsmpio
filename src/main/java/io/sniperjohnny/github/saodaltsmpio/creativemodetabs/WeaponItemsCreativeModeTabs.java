package io.sniperjohnny.github.saodaltsmpio.creativemodetabs;

import io.sniperjohnny.github.saodaltsmpio.Saodaltsmpio;
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

public class WeaponItemsCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> SAO_WEAPON_ITEM_GROUP_KEY =
            ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ResourceLocation.fromNamespaceAndPath(Saodaltsmpio.MOD_ID, "weapon_item_group"));

    public static final CreativeModeTab SAO_WEAPON_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModWeaponItems.Elucidator))
            .title(Component.translatable("itemgroup.saodaltsmpio_weapon_item_group"))

            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SAO_WEAPON_ITEM_GROUP_KEY
                , SAO_WEAPON_ITEM_GROUP).canScroll();
        ItemGroupEvents.modifyEntriesEvent(SAO_WEAPON_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.accept(ModWeaponItems.Elucidator);
            itemGroup.accept(ModWeaponItems.Dark_Repulsor);
            itemGroup.accept(ModWeaponItems.Liberator_Shield);
            itemGroup.accept(ModWeaponItems.Lambent_Light);

            // ...
        });
    }
}
