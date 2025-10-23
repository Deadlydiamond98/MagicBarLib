package net.deadlydiamond98.koalalib.util.mixindata.player;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface ICustomHudBarTextureMixinData {
    void koalalib$applyCustomHeartTexture(Identifier texture, boolean hasOutline, boolean canBlink);
    @Nullable Identifier koalalib$getCustomHeartTexture();
    boolean koalalib$hasCustomHearts();
    boolean koalalib$hasHeartOutline();
    boolean koalalib$canHeartBlink();

    void koalalib$applyCustomShankTexture(Identifier texture, boolean hasOutline);
    @Nullable Identifier koalalib$getCustomShankTexture();
    boolean koalalib$hasCustomShanks();
    boolean koalalib$hasShankOutline();
}
