package net.deadlydiamond98.koalalib.config;

import net.deadlydiamond98.koalalib.util.magic.MagicMeterRenderType;

/**
 * This file is just the configs for Koala Lib content that can be present in-game, ignore this in regular Dev!
 */
public class KoalaLibConfigs {
    public static class Main {
        public static boolean checkForUpdates = true;
        public static boolean fancyTransitions = true;
        public static boolean enderDragonDrops = true;
    }
    public static class MagicBar {
        public static MagicMeterRenderType manaBarRenderType = MagicMeterRenderType.Always;
        public static int manaBarPositionX = 420;
        public static int manaBarPositionY = 3;
        public static int manaBarTextOffsetX = 16;
        public static int manaBarTextOffsetY = 35;
    }
}
