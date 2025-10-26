package net.deadlydiamond98.koalalib.util.mixindata.player;

public interface IPlayerOtherMixinData {

    // Advancement Check
    boolean koalalib$hasAdvancement(String advancementID);
    void koalalib$updateAdvancementClient(boolean hasAdvancement);

    // Is Attacking
    boolean koalalib$isAttacking();
    void koalalib$setAttacking(boolean attacking);
}
