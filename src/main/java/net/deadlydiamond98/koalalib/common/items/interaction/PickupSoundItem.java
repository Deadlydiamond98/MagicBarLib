package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class PickupSoundItem extends Item implements IPickupSound {
    public static final Map<PlayerEntity, Long> LAST_PICKUP_TIME = new WeakHashMap<>();

    private final SoundEvent pickupSound;

    public PickupSoundItem(Settings settings, SoundEvent pickupSound) {
        super(settings);
        this.pickupSound = pickupSound;
    }

    @Override
    public SoundEvent getSound(ItemStack stack, PlayerEntity player) {
        return this.pickupSound;
    }
}
