package net.deadlydiamond98.koalalib.util.mixinterfaces.player;

public interface IPlayerOtherMixinData {

    // Advancement Check
    boolean koalalib$hasAdvancement(String advancementID);
    void koalalib$updateAdvancementClient(boolean hasAdvancement);

    // Is Attacking
    boolean koalalib$isAttacking();
    void koalalib$setAttacking(boolean attacking);

    // Pseudo Random Numbers
    boolean koalalib$getPseudoRandom(String id, float chance, int max);
}
