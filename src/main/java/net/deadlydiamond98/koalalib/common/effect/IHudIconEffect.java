package net.deadlydiamond98.koalalib.common.effect;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public interface IHudIconEffect {

    IconType getIconType();

    default Identifier getIconsTexture(PlayerEntity player) {
        return Identifier.of(KoalaLib.MOD_ID, "textures/gui/debug_hud_icons.png");
    }

    default boolean canHeartBlink() {
        return true;
    }

    default boolean customHeartOutline() {
        return false;
    }

    default boolean customShankOutline() {
        return false;
    }
}
