package net.deadlydiamond98.koalalib.common.effect;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CustomHudIconEffect extends StatusEffect {

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

    public static final Identifier DEBUG_HUD_ICONS = new Identifier(KoalaLib.MOD_ID, "textures/gui/debug_hud_icons.png");
    public static final List<CustomHudIconEffect> CUSTOM_HUD_RENDER_EFFECTS = new ArrayList<>();
    private final IconType iconType;

    public CustomHudIconEffect(StatusEffectCategory category, int color, IconType iconType, int priority) {
        super(category, color);
        this.iconType = iconType;
        int index = Math.max(0, Math.min(priority, CUSTOM_HUD_RENDER_EFFECTS.size()));
        CUSTOM_HUD_RENDER_EFFECTS.add(index, this);
    }

    public Identifier getTexture(PlayerEntity player) {
        return DEBUG_HUD_ICONS;
    }

    public boolean canHeartBlink() {
        return true;
    }

    public boolean customHeartOutline() {
        return true;
    }

    public boolean customShankOutline() {
        return false;
    }

    public IconType getIconType() {
        return this.iconType;
    }
}
