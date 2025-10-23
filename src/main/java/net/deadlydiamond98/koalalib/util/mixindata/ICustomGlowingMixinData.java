package net.deadlydiamond98.koalalib.util.mixindata;

import java.util.Optional;

public interface ICustomGlowingMixinData {
    Optional<Integer> koalalib$getGlowColor();
    void koalalib$setGlowColor(Optional<Integer> hex);
}
