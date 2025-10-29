package net.deadlydiamond98.koalalib.util;

import net.deadlydiamond98.koalalib.util.mixindata.ICustomGlowingMixinData;
import net.minecraft.entity.Entity;

import java.util.Optional;

/**
 * Marked for removal, not really all that needed and my implementation was kinda half-baked for this
 */
@Deprecated(forRemoval = true)
public class ColoredGlowHelper {
    public static void setEntityGlow(Entity entity, int hex) {
        if (entity instanceof ICustomGlowingMixinData glow) {
            glow.koalalib$setGlowColor(Optional.of(hex));
        }
    }

    public static void removeEntityGlow(Entity entity) {
        if (entity instanceof ICustomGlowingMixinData glow) {
            glow.koalalib$setGlowColor(Optional.empty());
        }
    }
}
