package net.deadlydiamond98.koalalib.common.items.vanillamodified;

import net.minecraft.enchantment.Enchantment;

import java.util.List;

/**
 * Implementing this interface will allow you to apply non-standard enchantments to items
 */
public interface IExtraEnchantments {
    /**
     * Returns a list of enchantments that will be made compatible with the item
     */
    List<Enchantment> getEnchantments();
}
