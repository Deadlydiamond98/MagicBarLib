package net.deadlydiamond98.koalalib.common.items.other;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * This can be applied to any block or item to add an icon
 */
public interface ISpriteIconItem {

    /**
     * Enum for determining start position of Icons, Item and Block are separate due to transformations on their models
     */
    enum GUICorner {
        TOP_LEFT(0.64, 1.36, -1, 1.56),
        TOP_RIGHT(1.49, 1.36, -0.12, 1.56),
        BOTTOM_LEFT(0.64, 0.5, -1, 0.66),
        BOTTOM_RIGHT(1.49, 0.5, -0.12, 0.66);

        final double itemX, itemY, blockX, blockY;

        GUICorner(double itemX, double itemY, double blockX, double blockY) {
            this.itemX = itemX;
            this.itemY = itemY;
            this.blockX = blockX;
            this.blockY = blockY;
        }
    }

    /**
     * If false, the icon won't be displayed on screen!
     */
    default boolean showIcon(PlayerEntity player, ItemStack stack) {
        return true;
    }

    /**
     * This returns the GUICorner, which is used to determine the default position of the Icon
     */
    default GUICorner getGUICorner() {
        return GUICorner.BOTTOM_LEFT;
    }

    /**
     * This determines the amount of pixels the icon is offset by on the X-axis
     * (This might not work properly if getTranslate is changed)
     * @return number of pixels
     */
    default int pixelOffsetX() {
        return 0;
    }
    /**
     * This determines the amount of pixels the icon is offset by on the Y-axis <br>
     * (This might not work properly if getTranslate is changed)
     * @return number of pixels
     */
    default int pixelOffsetY() {
        return 0;
    }

    /**
     * This gets the texture for the icon, has additional parameters for modifying the texture based on player and stack too
     * @param player - The Player (Client-side)
     * @param stack - The Itemstack
     * @return the Texture to use
     */
    default Identifier getTexture(PlayerEntity player, ItemStack stack) {
        return Identifier.of(KoalaLib.MOD_ID, "textures/item/icon/debug_icon.png");
    }

    /**
     * Checks if this is extending a block, and if so applies different offsets
     */
    default boolean isBlock() {
        return this instanceof Block || this instanceof BlockItem;
    }

    // While Most Item models work fine with the default translations, some don't so these can be modified in case the
    // default values don't work for an item. Various methods also take in the player and stack for animation potential

    default double getTranslateX(PlayerEntity player, ItemStack stack) {
        return isBlock() ? getGUICorner().blockX : getGUICorner().itemX;
    }
    default double getTranslateY(PlayerEntity player, ItemStack stack) {
        return isBlock() ? getGUICorner().blockY: getGUICorner().itemY;
    }
    default double getTranslateZ() {
        return isBlock() ? -0.5 : 1;
    }
    default float getScaleX(PlayerEntity player, ItemStack stack) {
        return isBlock() ? -0.8f : 0.5f;
    }
    default float getScaleY(PlayerEntity player, ItemStack stack) {
        return isBlock() ? 0.92f : 0.5f;
    }
    default float getRotation() {
        return isBlock() ? -45 : 0;
    }
}
