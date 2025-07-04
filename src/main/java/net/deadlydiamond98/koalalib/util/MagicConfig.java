package net.deadlydiamond98.koalalib.util;

public class MagicConfig {

    //TODO: REMOVE CLASS AND SWAP WITH NEW CFG CLASS

//    @Comment(category = "text") public static Comment spacer1;
    public static manaBarEnum renderManaBar = manaBarEnum.Always;
    public enum manaBarEnum {
        Always, When_Needed, Never
    }
//    @Comment(category = "text") public static Comment spacer2;
    public static int manaBarPositionX = 420;
//    @Comment(category = "text") public static Comment spacer3;
    public static int manaBarPositionY = 3;

//    @Comment(category = "text") public static Comment spacer4;
    public static int manaBarTextOffsetX = 16;
//    @Comment(category = "text") public static Comment spacer5;
    public static int manaBarTextOffsetY = 35;
}
