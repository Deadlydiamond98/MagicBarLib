package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;

public class PickupSoundItem extends Item implements IPickupSound {

    private final SoundEvent pickupSound;

    public PickupSoundItem(Settings settings, SoundEvent pickupSound) {
        super(settings);
        this.pickupSound = pickupSound;
    }

    @Override
    public SoundEvent getSound() {
        return this.pickupSound;
    }
}
