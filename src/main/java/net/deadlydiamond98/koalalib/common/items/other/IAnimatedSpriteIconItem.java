package net.deadlydiamond98.koalalib.common.items.other;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * This can be applied to any block or item to add an icon, this version allows for animations
 */
public interface IAnimatedSpriteIconItem extends ISpriteIconItem {

    /**
     * Returns the amount of frames in the current texture
     */
    int getFrames(PlayerEntity player, ItemStack stack);

    /**
     * Returns the amount of time in ticks (0.05 seconds) spent per frame
     */
    int getFrameTime(PlayerEntity player, ItemStack stack);

    @Override
    default Identifier getTexture(PlayerEntity player, ItemStack stack) {
        return Identifier.of(KoalaLib.MOD_ID, "textures/item/icon/debug_icon_animated.png");
    }
}
