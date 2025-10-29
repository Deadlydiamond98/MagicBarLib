package net.deadlydiamond98.koalalib.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Event that fires whenever the player presses the attack key
 * Runs on both the server and client!
 */
public interface PlayerAttackingCallback {
    Event<PlayerAttackingCallback> EVENT = EventFactory.createArrayBacked(PlayerAttackingCallback.class,
            (listeners) -> (player) -> {
                for (PlayerAttackingCallback listener : listeners) {
                    listener.interact(player);
                }
    });

    void interact(PlayerEntity player);
}
