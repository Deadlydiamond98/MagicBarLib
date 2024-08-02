package net.deadlydiamond98.magiclib;


import eu.midnightdust.lib.config.MidnightConfig;
import net.deadlydiamond98.magiclib.events.AfterRespawnEvent;
import net.deadlydiamond98.magiclib.items.TestingItems;
import net.deadlydiamond98.magiclib.networking.MagicServerPackets;
import net.deadlydiamond98.magiclib.util.MagicConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagicLib implements ModInitializer {
	public static final String MOD_ID = "magiclib";

	//Custom Zelda Font
	public static final Identifier ZELDA_FONT = Identifier.of(MOD_ID, "zeldafont");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, MagicConfig.class);
		MagicServerPackets.registerServerPackets();
		TestingItems.registerItems();
		AfterRespawnEvent.register();

		LOGGER.info("MagicLib finished Loading");
	}

	public static boolean isModLoaded(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}
}