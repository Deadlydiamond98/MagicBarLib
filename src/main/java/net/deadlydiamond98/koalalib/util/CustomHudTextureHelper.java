package net.deadlydiamond98.koalalib.util;

import net.deadlydiamond98.koalalib.util.mixinterfaces.player.ICustomHudBarTextureMixinData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

/**
 * Helper Class for applying custom textures to the Health and Hunger Bar
 */
public class CustomHudTextureHelper {

    /**
     * Applys a custom Heart texture to a player's health bar
     * @param target Target Player
     * @param texture Heart Texture
     * @param hasOutline Whether the hearts should use the outline from the texture or the default one
     * @param canBlink Whether the hearts should flash a lighter color when hurt
     */
    public static void applyCustomHeartTexture(PlayerEntity target, Identifier texture, boolean hasOutline, boolean canBlink) {
        if (target instanceof ICustomHudBarTextureMixinData hudInfo) {
            hudInfo.koalalib$applyCustomHeartTexture(texture, hasOutline, canBlink);
        }
    }

    /**
     * Applys a custom Shank texture to a player's hunger bar
     * @param target Target Player
     * @param texture Shank Texture
     * @param hasOutline Whether the shanks should use the outline from the texture or the default one
     */
    public static void applyCustomShankTexture(PlayerEntity target, Identifier texture, boolean hasOutline) {
        if (target instanceof ICustomHudBarTextureMixinData hudInfo) {
            hudInfo.koalalib$applyCustomShankTexture(texture, hasOutline);
        }
    }

    /**
     * Removes the Heart Texture
     * @param target Target Player
     */
    public static void resetHeartTexture(PlayerEntity target) {
        if (target instanceof ICustomHudBarTextureMixinData hudInfo) {
            hudInfo.koalalib$applyCustomHeartTexture(null, false, false);
        }
    }

    /**
     * Removes the Shank Texture
     * @param target Target Player
     */
    public static void resetShankTexture(PlayerEntity target) {
        if (target instanceof ICustomHudBarTextureMixinData hudInfo) {
            hudInfo.koalalib$applyCustomShankTexture(null, false);
        }
    }

    /**
     * Removes the Heart and Shank Texture
     * @param target Target Player
     */
    public static void resetHudTextures(PlayerEntity target) {
        resetHeartTexture(target);
        resetShankTexture(target);
    }

    /**
     * Whether the player has a custom Heart Texture applied
     * @param target Target Player
     */
    public static boolean hasHeartTexture(PlayerEntity target) {
        if (target instanceof ICustomHudBarTextureMixinData hudInfo) {
            return hudInfo.koalalib$hasCustomHearts();
        }
        return false;
    }

    /**
     * Whether the player has a custom Shank Texture applied
     * @param target Target Player
     */
    public static boolean hasShankTexture(PlayerEntity target) {
        if (target instanceof ICustomHudBarTextureMixinData hudInfo) {
            return hudInfo.koalalib$hasCustomShanks();
        }
        return false;
    }
}
