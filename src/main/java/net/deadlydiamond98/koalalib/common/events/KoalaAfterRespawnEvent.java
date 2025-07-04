package net.deadlydiamond98.koalalib.common.events;

import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class KoalaAfterRespawnEvent {

    public static void register() {
        // Keeps the Player's Max Mana level after death
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            MagicBarHelper.setMaxMana(newPlayer, MagicBarHelper.getMaxMana(oldPlayer));
        });
    }
}
