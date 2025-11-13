package net.deadlydiamond98.koalalib;

import net.deadlydiamond98.koalalib.init.KoalaLibItems;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.deadlydiamond98.koalalib.config.KoalaLibConfigs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;

/**
 * Contains various toggleable features, such as the Magic Meter, so they can be used if wanted, but don't get added if
 * unused.
 */
public class ToggleableContent {

    private static boolean magicBar = false;
    private static boolean enderSoul = false;

    // Call this if you want to enable the Magic Bar in your mod
    public static void enableMagicBar() {
        if (!magicBar) {
            KoalaConfigCreator.addModConfigCategory(KoalaLib.MOD_ID, "magic_bar", KoalaLibConfigs.MagicBar.class);
            magicBar = true;
        }
    }

    // Call this if you want to enable Ender Souls as a material in your mod
    public static void enableEnderSouls() {
        if (!enderSoul) {
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(KoalaLibItems.ENDER_SOUL));
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