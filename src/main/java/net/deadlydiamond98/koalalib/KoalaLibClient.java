package net.deadlydiamond98.koalalib;

import net.deadlydiamond98.koalalib.client.events.KoalaClientTickEvents;
import net.deadlydiamond98.koalalib.client.events.KoalaItemTooltipEvents;
import net.deadlydiamond98.koalalib.common.events.KoalaOnJoinEvent;
import net.deadlydiamond98.koalalib.common.items.magic.IMagicItem;
import net.deadlydiamond98.koalalib.networking.KoalaPackets;
import net.deadlydiamond98.koalalib.client.renderer.MagicBarHud;
import net.deadlydiamond98.koalalib.updater.KoalaUpdateChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class KoalaLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		KoalaPackets.registerS2CPackets();

		HudRenderCallback.EVENT.register(new MagicBarHud());
		KoalaItemTooltipEvents.register();
		KoalaClientTickEvents.register();
		KoalaOnJoinEvent.register();
	}
}