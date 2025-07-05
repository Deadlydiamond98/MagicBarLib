package net.deadlydiamond98.koalalib;

/**
 * Contains various toggleable features, such as the Magic Meter, so they can be used if wanted, but don't get added if
 * unused.
 */
public class ToggleableContent {

    private static boolean magicBar = false;
    private static boolean enderSoul = false;

    public static void enableMagicBar(boolean enabled) {
        magicBar = enabled;
    }

    public static void enableEnderSouls(boolean enabled) {
        enderSoul = enabled;
    }

    public static boolean isMagicBarEnabled() {
        return magicBar;
    }

    public static boolean areEnderSoulsEnabled() {
        return enderSoul;
    }
}