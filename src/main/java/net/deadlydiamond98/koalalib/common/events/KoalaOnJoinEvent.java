package net.deadlydiamond98.koalalib.common.events;

import net.deadlydiamond98.koalalib.config.configs.MainConfigs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class KoalaOnJoinEvent {
    //TODO: Actually make this check and have information based on mod version!
    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (MainConfigs.checkForUpdates) {
                client.player.sendMessage(Text.translatable("koalalib.updatechecker").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));

                client.player.sendMessage(Text.literal("https://modrinth.com/mod/koala_lib/versions").setStyle(Style.EMPTY.withColor(Formatting.BLUE)
                        .withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/koala_lib/versions")))
                );
            }
        });
    }
}
