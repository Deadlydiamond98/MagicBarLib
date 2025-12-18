package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * These Items will constantly float, used mainly for Ender Souls
 */
public class FloatingItem extends Item implements IAdvancedItemProperties {
    public FloatingItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasNoGravity(ItemEntity entity, ItemStack stack) {
        return true;
    }
}