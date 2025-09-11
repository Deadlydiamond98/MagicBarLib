package net.deadlydiamond98.koalalib.client.events;

import net.deadlydiamond98.koalalib.config.KoalaLibConfigs;
import net.deadlydiamond98.koalalib.updater.KoalaUpdateChecker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class KoalaOnJoinClientEvent {
    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> promptModUpdates(client));
    }

    private static void promptModUpdates(MinecraftClient client) {
        if (KoalaLibConfigs.Main.checkForUpdates && client.player != null) {

            KoalaClientTickEvents.updateTipTimer = 0;
            KoalaClientTickEvents.updateTipSent = false;

            KoalaUpdateChecker.MOD_UPDATE_LIST.forEach(mod -> {
                client.player.sendMessage(Text.translatable("chat.koalalib.update",
                        Text.literal(mod.name()).formatted(Formatting.YELLOW)
                ).formatted(Formatting.GREEN));

                client.player.sendMessage(Text.translatable("chat.koalalib.update.prompt",
                        Text.translatable("chat.koalalib.update.hyperlink").setStyle(attachURL(mod.url()))
                ).formatted(Formatting.AQUA));
            });

        }
    }

    /**
     * Creates a Style that will open a URL when clicked
     * @param url The URL to direct to
     * @return Returns the Style
     */
    private static Style attachURL(String url) {
        return Style.EMPTY.withColor(Formatting.BLUE).withUnderline(true)
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
    }
}
