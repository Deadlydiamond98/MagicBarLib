package net.deadlydiamond98.koalalib.common.items.magic;

import net.minecraft.item.ItemStack;

public interface IMagicItem extends IShowsMagicBar {
    /**
     * When this is implemented into an item, the mana cost will be displayed as a tooltip on the item, this is the old
     * version, Use {@link #getManaCost(ItemStack)}. Only still included as the old version of the Legend of Steve still
     * relies on this.
     */
    @Deprecated
    default int getManaCost() {
        return 0;
    }

    /**
     * When this is implemented into an item, the mana cost will be displayed as a tooltip on the item
     *
     * @param stack the itemStack for the item
     */
    default int getManaCost(ItemStack stack) {
        return getManaCost();
    }
}
