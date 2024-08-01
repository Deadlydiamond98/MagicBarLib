package net.deadlydiamond98.magiclib.events;

import net.deadlydiamond98.magiclib.util.ManaEntityData;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class AfterRespawnEvent {

    public static void register() {
        // Keeps the Player's Max Mana level
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            newPlayer.setMaxMana(oldPlayer.getMaxMana());
        });
    }

}
