package net.deadlydiamond98.koalalib.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;

public interface IMimicItemPickup {
    default void pickUp(Entity entity, PlayerEntity player, int count) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().sendToNearbyPlayers(entity, new ItemPickupAnimationS2CPacket(entity.getId(), player.getId(), count));
        }
    }

    @Nullable
    default SoundEvent getPickUpSound() {
        return null;
    }

    default float getPickUpVolume() {
        return 1;
    }

    default float getPickUpPitch() {
        return 1;
    }
}
