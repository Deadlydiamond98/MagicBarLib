package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

@Deprecated(forRemoval = true)
public interface IFloating extends IAdvancedItemProperties {
    @Override
    default boolean hasNoGravity(ItemEntity entity, ItemStack stack) {
        return true;
    }
}
