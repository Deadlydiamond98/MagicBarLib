package net.deadlydiamond98.magiclib.util;

import eu.midnightdust.lib.config.MidnightConfig;

public class MagicConfig extends MidnightConfig {
    @Comment(category = "text") public static Comment spacer1;
    @Entry(category = "text") public static manaBarEnum renderManaBar = manaBarEnum.Always;
    public enum manaBarEnum {
        Always, When_Needed, Never
    }
    @Comment(category = "text") public static Comment spacer2;
    @Entry(category = "text", isSlider = true, min = 0, max = 624) public static int manaBarPositionX = 420;
    @Comment(category = "text") public static Comment spacer3;
    @Entry(category = "text", isSlider = true, min = 0, max = 318) public static int manaBarPositionY = 3;

    @Comment(category = "text") public static Comment spacer4;
    @Entry(category = "text", isSlider = true, min = -50, max = 50) public static int manaBarTextOffsetX = 16;
    @Comment(category = "text") public static Comment spacer5;
    @Entry(category = "text", isSlider = true, min = -50, max = 50) public static int manaBarTextOffsetY = 35;
}
