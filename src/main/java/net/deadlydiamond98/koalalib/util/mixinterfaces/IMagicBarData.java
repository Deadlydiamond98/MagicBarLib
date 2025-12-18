package net.deadlydiamond98.koalalib.util.mixinterfaces;

public interface IMagicBarData {
    void koalalib$setMana(int value);
    int koalalib$getMana();

    void koalalib$setManaRegenAbility(boolean value);
    boolean koalalib$isManaRegenEnabled();

    void koalalib$setMagicRegenCap(int value);
    int koalalib$getMagicRegenCap();

    void koalalib$requireFullHungerForMagicRegen(boolean bl);

    void koalalib$setMagicBarRenderTime(int value);
    int koalalib$getMagicBarRenderTime();

    void koalalib$applyRegenDelay(boolean value);
}
