package net.deadlydiamond98.koalalib;

import net.deadlydiamond98.koalalib.client.events.KoalaClientTickEvents;
import net.deadlydiamond98.koalalib.client.events.KoalaItemTooltipEvents;
import net.deadlydiamond98.koalalib.client.events.KoalaOnJoinClientEvent;
import net.deadlydiamond98.koalalib.networking.KoalaPackets;
import net.deadlydiamond98.koalalib.client.renderer.MagicBarHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.*;

public class KoalaLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		KoalaPackets.registerS2CPackets();

		HudRenderCallback.EVENT.register(new MagicBarHud());
		KoalaItemTooltipEvents.register();
		KoalaClientTickEvents.register();
		KoalaOnJoinClientEvent.register();
	}
}