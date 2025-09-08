package net.deadlydiamond98.koalalib;

import net.deadlydiamond98.koalalib.common.items.ModSharedItems;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.deadlydiamond98.koalalib.config.configs.MagicBarConfigs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;

/**
 * Contains various toggleable features, such as the Magic Meter, so they can be used if wanted, but don't get added if
 * unused.
 */
public class ToggleableContent {

    private static boolean magicBar = false;
    private static boolean enderSoul = false;

    /**
     * Original way to enable Magic Bar, will be removed Next Update!<br>
     * Use {@link #enableMagicBar()} instead!
     */
    @Deprecated(forRemoval = true)
    public static void enableMagicBar(boolean enabled) {
        enableMagicBar();
    }

    /**
     * Original way to enable Ender Souls, will be removed Next Update!<br>
     * Use {@link #enableEnderSouls()} instead!
     */
    @Deprecated(forRemoval = true)
    public static void enableEnderSouls(boolean enabled) {
        enableEnderSouls();
    }

    // Call this if you want to enable the Magic Bar in your mod
    public static void enableMagicBar() {
        if (!magicBar) {
            KoalaConfigCreator.addModConfigCategory(KoalaLib.MOD_ID, "magic_bar", MagicBarConfigs.class);
            magicBar = true;
        }
    }

    // Call this if you want to enable Ender Souls in your mod
    public static void enableEnderSouls() {
        if (!enderSoul) {
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModSharedItems.ENDER_SOUL));
            enderSoul = true;
        }
    }

    public static boolean isMagicBarEnabled() {
        return magicBar;
    }

    public static boolean areEnderSoulsEnabled() {
        return enderSoul;
    }
}