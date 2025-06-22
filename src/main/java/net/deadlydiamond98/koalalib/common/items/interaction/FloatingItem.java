package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.item.Item;

/**
 * This item won't have gravity applied to it due to using {@link IFloating}
 */
public class FloatingItem extends Item implements IFloating {
    public FloatingItem(Settings settings) {
        super(settings);
    }
}