package io.sniperjohnny.github.saodaltsmpio.toolmaterials;

import io.sniperjohnny.github.saodaltsmpio.moditems.ModRecipeItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class CrystalliteToolMaterial implements Tier {
    public static final CrystalliteToolMaterial INSTANCE = new CrystalliteToolMaterial();
    @Override
    public int getUses() {
        return 3094;
    }

    @Override
    public float getSpeed() {
        return 16f;
    }

    @Override
    public float getAttackDamageBonus() {
        return 6;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return 25;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModRecipeItems.Crystallite_ore);
    }
}
