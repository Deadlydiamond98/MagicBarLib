package net.deadlydiamond98.koalalib.init;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Currently there are no shared effects, this purely exists for debugging
 */
public class KoalaLibEffects {

//    public static final StatusEffect DEBUG_EFFECT = register("debug_effecta",
//            new TestEffect(StatusEffectCategory.HARMFUL, 0xFFFFFF, 0x00ffaa, true)
//    );

    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(KoalaLib.MOD_ID, name), effect);
    }

    public static void register() {}
}
