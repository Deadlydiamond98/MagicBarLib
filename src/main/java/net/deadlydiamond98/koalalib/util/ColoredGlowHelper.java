package net.deadlydiamond98.koalalib.util;

import net.deadlydiamond98.koalalib.util.mixindata.ICustomGlowingMixinData;
import net.minecraft.entity.Entity;

import java.util.Optional;

/**
 * Used to apply a Colored Glow Effect to an Entity
 */
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
