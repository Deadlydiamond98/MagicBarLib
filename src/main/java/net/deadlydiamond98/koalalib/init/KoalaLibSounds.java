package net.deadlydiamond98.koalalib.init;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class KoalaLibSounds {
    // APRIL FOOLS /////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final SoundEvent BAGEL = register("aprilfools.bagel");

    // MAGIC ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final SoundEvent MAGIC_FAIL = register("magic.negative");

    // LEGACY CONSOLE SOUNDS ///////////////////////////////////////////////////////////////////////////////////////////
    public static final SoundEvent CONSOLE_BACK = register("legacy.gui.back");
    public static final SoundEvent CONSOLE_CRAFT = register("legacy.gui.craft");
    public static final SoundEvent CONSOLE_CRAFT_FAIL = register("legacy.gui.craftfail");
    public static final SoundEvent CONSOLE_FOCUS = register("legacy.gui.focus");
    public static final SoundEvent CONSOLE_PRESS = register("legacy.gui.press");
    public static final SoundEvent CONSOLE_SCROLL = register("legacy.gui.scroll");

    // GENERIC /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // sounds here are copies of Minecraft Sounds, but with a more generic subtitle translation
    public static final SoundEvent ITEM_THROW = register("generic.thrown");
    public static final SoundEvent ITEM_IGNITE = register("generic.item_ignite");
    public static final SoundEvent TOOL_IGNITE = register("generic.tool_ignite");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(KoalaLib.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {}
}
