package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

/**
 * Any Item that implements this will play a sound when they're picked up
 */
public interface IPickupSound extends IAdvancedItemProperties {

    @Override
    default void onPlayerPickingUp(ItemStack stack, PlayerEntity player) {
        long currentTime = System.currentTimeMillis();
        long lastPickupTime = PickupSoundItem.LAST_PICKUP_TIME.getOrDefault(player, 0L);
        if (currentTime - lastPickupTime > 500) {
            player.playSound(getSound(stack, player), getVolume(stack, player), getPitch(stack, player));
            PickupSoundItem.LAST_PICKUP_TIME.put(player, currentTime);
        }
    }

    SoundEvent getSound(ItemStack stack, PlayerEntity player);

    default float getVolume(ItemStack stack, PlayerEntity player) {
        return 1;
    }
    default float getPitch(ItemStack stack, PlayerEntity player) {
        return 1;
    }
}
