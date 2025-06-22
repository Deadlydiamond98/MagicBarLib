package net.deadlydiamond98.koalalib.common.misc;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSharedSounds {
    // APRIL FOOLS
    public static final SoundEvent BAGEL = registerSoundEvent("aprilfools.bagel");

    // LEGACY CONSOLE SOUNDS
    public static final SoundEvent CONSOLE_BACK = registerSoundEvent("legacy.gui.back");
    public static final SoundEvent CONSOLE_CRAFT = registerSoundEvent("legacy.gui.craft");
    public static final SoundEvent CONSOLE_CRAFT_FAIL = registerSoundEvent("legacy.gui.craftfail");
    public static final SoundEvent CONSOLE_FOCUS = registerSoundEvent("legacy.gui.focus");
    public static final SoundEvent CONSOLE_PRESS = registerSoundEvent("legacy.gui.press");
    public static final SoundEvent CONSOLE_SCROLL = registerSoundEvent("legacy.gui.scroll");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(KoalaLib.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {}
}
