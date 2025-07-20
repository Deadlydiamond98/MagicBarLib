package net.deadlydiamond98.koalalib.util.mixindata;

/**
 * This interface is used to slow the vertical speed of dropped items with no gravity so that they don't fly into the sky
 */
public interface IFloatingItemMixinData {
    void koalalib$setDroppedItem(boolean bl);
}
