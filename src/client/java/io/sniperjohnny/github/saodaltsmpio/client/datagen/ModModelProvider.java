package io.sniperjohnny.github.saodaltsmpio.client.datagen;

import io.sniperjohnny.github.saodaltsmpio.moditems.ModFoodItems;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModRecipeItems;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModToolsItems;
import io.sniperjohnny.github.saodaltsmpio.moditems.ModWeaponItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModRecipeItems.Crystallite_ore, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModRecipeItems.Metall, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModRecipeItems.Metall_scrap, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModRecipeItems.Reinforced_leather_stick, ModelTemplates.FLAT_ITEM);


        itemModelGenerator.generateFlatItem(ModWeaponItems.Elucidator, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModWeaponItems.DARK_REPULSOR, ModelTemplates.FLAT_HANDHELD_ITEM);


        itemModelGenerator.generateFlatItem(ModToolsItems.Crystallite_pickaxe, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModToolsItems.Crystallite_axe, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModToolsItems.Crystallite_shovel, ModelTemplates.FLAT_HANDHELD_ITEM);


        itemModelGenerator.generateFlatItem(ModFoodItems.Rabbit_ragout, ModelTemplates.FLAT_ITEM);
    }
    @Override
    public String getName() {
        return "ModModelProvider";
    }


}
