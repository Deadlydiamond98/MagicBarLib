package net.deadlydiamond98.koalalib.common.enchant;

import net.deadlydiamond98.koalalib.common.items.magic.IMagicItem;
import net.deadlydiamond98.koalalib.util.magic.MagicCostModifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * This Enchantment can modify the Magic Cost of an effected item!
 */
public abstract class MagicModifierEnchantment extends Enchantment {

    public MagicModifierEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    /**
     * The Type of Operation That's used to modify the Magic Cost of an Item
     * @param enchantment the Enchantment
     * @param level the Enchantment level
     * @return the Type of Operation
     */
    public abstract MagicCostModifier.Operation getOperationModifer(Enchantment enchantment, int level);

    /**
     * The Number that is used to Modify the Magic Cost of an Item when paired with an Operation
     * @param enchantment the Enchantment
     * @param level the Enchantment level
     * @return the cost modifier
     */
    public abstract double getCostModifier(Enchantment enchantment, int level);

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof IMagicItem;
    }
}
