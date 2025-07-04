package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.sound.SoundEvent;

/**
 * Any Item that implements this will play a sound when they're picked up
 */
public interface IPickupSound {
    SoundEvent getSound();
    default float getVolume() {
        return 1;
    }
    default float getPitch() {
        return 1;
    }
}
