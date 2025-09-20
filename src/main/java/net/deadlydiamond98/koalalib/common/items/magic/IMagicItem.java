package net.deadlydiamond98.koalalib.common.items.magic;

import net.minecraft.item.ItemStack;

public interface IMagicItem extends IShowsMagicBar {

    /**
     * When this is implemented into an item, the mana cost will be displayed as a tooltip on the item
     *
     * @param stack the itemStack for the item
     */
    default int getManaCost(ItemStack stack) {
        return 0;
    }

    /**
     * If this is true, an item will show a magic cost in a similar way to that of a sword's attack speed
     *
     * @param stack the itemStack for the item
     */
    default boolean showTooltip(ItemStack stack) {
        return getManaCost(stack) > 0;
    }
}
