package net.deadlydiamond98.koalalib.common.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Extending this Enchant Class will allow the enchantment to show up in the Enchanting Table if canAppearInEnchantingTable() is true
 */
public class TableCompatEnchant extends Enchantment {
    public TableCompatEnchant(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    public boolean canAppearInEnchantingTable(ItemStack stack) {
        return true;
    }
}
