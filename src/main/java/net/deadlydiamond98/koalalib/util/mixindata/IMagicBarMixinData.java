package net.deadlydiamond98.koalalib.util.mixindata;

import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;

/**
 * This is an interface used on Living Entities that allows them to store and use Magic, you can change the values by
 * using {@link MagicBarHelper}
 */
public interface IMagicBarMixinData {
    void koalalib$setMana(int value);
    int koalalib$getMana();

    void koalalib$setMaxMana(int value);
    int koalalib$getMaxMana();

    void koalalib$setManaRegenAbility(boolean value);
    boolean koalalib$isManaRegenEnabled();

    void koalalib$setMagicBarRenderTime(int value);
    int koalalib$getMagicBarRenderTime();

    void koalalib$applyRegenDelay(boolean value);
}
