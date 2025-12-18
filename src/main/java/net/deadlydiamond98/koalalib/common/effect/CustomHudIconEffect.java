package net.deadlydiamond98.koalalib.common.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CustomHudIconEffect extends StatusEffect implements IHudIconEffect {

    private final IconType iconType;

    @Deprecated(forRemoval = true)
    public CustomHudIconEffect(StatusEffectCategory category, int color, IconType iconType, int priority) {
        this(category, color, iconType);
    }

    public CustomHudIconEffect(StatusEffectCategory category, int color, IconType iconType) {
        super(category, color);
        this.iconType = iconType;
    }

    public IconType getIconType() {
        return this.iconType;
    }
}
