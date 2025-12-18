package net.deadlydiamond98.koalalib.common.enchant;

import net.deadlydiamond98.koalalib.common.items.magic.IMagicItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TableCompatEnchant extends Enchantment implements ITableCompatable {
    public TableCompatEnchant(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public boolean canAppearInEnchantingTable(ItemStack stack) {
        return isAcceptableItem(stack);
    }
}
