package net.deadlydiamond98.koalalib.common.items.magic.consumables;

import net.deadlydiamond98.koalalib.common.items.magic.IShowsMagicBar;
import net.minecraft.item.Item;

/**
 * Magic Consumables extend this class so that they make the Mana Bar appear when it's on When Needed mode
 */
public class MagicReplenisher extends Item implements IShowsMagicBar {
    public MagicReplenisher(Settings settings) {
        super(settings);
    }
}
