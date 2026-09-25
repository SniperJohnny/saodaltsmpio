package io.sniperjohnny.github.saodaltsmpio.client;

import io.sniperjohnny.github.saodaltsmpio.moditems.ModWeaponItems;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class SaodaltsmpioClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LiberatorKeybinds.register();
		LiberatorClientHandler.register();
		ItemProperties.register(ModWeaponItems.Liberator_Shield,
			ResourceLocation.withDefaultNamespace("blocking"),
			(stack, level, entity, seed) -> {
				return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0f : 0.0f;
			}
		);
	}
}