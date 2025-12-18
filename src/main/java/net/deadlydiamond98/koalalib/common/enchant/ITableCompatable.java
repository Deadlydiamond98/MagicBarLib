package net.deadlydiamond98.koalalib.common.enchant;

import net.minecraft.item.ItemStack;

/**
 * Enchantments that implement this will be able to appear inside the Enchanting table
 */
public interface ITableCompatable {
    /**
     *
     * @param stack the item with the enchantment
     * @return whether the enchantment can appear in the enchanting table
     */
    boolean canAppearInEnchantingTable(ItemStack stack);
}
