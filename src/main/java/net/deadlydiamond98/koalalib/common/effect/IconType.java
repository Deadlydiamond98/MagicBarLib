package net.deadlydiamond98.koalalib.common.effect;

public enum IconType {
    HEALTH,
    HUNGER,
    BOTH;

    public boolean isHeart(boolean bl) {
        return bl && (this == HEALTH  || this == BOTH);
    }

    public boolean isHunger(boolean bl) {
        return !bl && (this == HUNGER || this == BOTH);
    }

    public boolean canRender(boolean bl) {
        return isHeart(bl) || isHunger(bl);
    }
}