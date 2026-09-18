package io.sniperjohnny.github.saodaltsmpio;

import io.sniperjohnny.github.saodaltsmpio.creativemodetabs.FoodItemsCreativeModeTabs;
import io.sniperjohnny.github.saodaltsmpio.creativemodetabs.RecipeItemsCreativeModeTabs;
import io.sniperjohnny.github.saodaltsmpio.creativemodetabs.ToolItemsCreativeModeTabs;
import io.sniperjohnny.github.saodaltsmpio.creativemodetabs.WeaponItemsCreativeModeTabs;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModFoodItems;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModRecipeItems;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModToolsItems;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModWeaponItems;
import io.sniperjohnny.github.saodaltsmpio.networking.LiberatorNetworking;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Saodaltsmpio implements ModInitializer {
	public static final String MOD_ID = "saodaltsmpio";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModRecipeItems.initialize();
		ModFoodItems.initialize();
		ModWeaponItems.initialize();
		ModToolsItems.initialize();

		LiberatorNetworking.register();


		RecipeItemsCreativeModeTabs.initialize();
		WeaponItemsCreativeModeTabs.initialize();
		ToolItemsCreativeModeTabs.initialize();
		FoodItemsCreativeModeTabs.initialize();


		LOGGER.info("Sao dal tsmpio has initialized, and is ready to be played!");

	}




	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
