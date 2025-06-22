package net.deadlydiamond98.koalalib;


import eu.midnightdust.lib.config.MidnightConfig;
import net.deadlydiamond98.koalalib.common.commands.MagicBarCommands;
import net.deadlydiamond98.koalalib.common.misc.ModSharedSounds;
import net.deadlydiamond98.koalalib.events.KoalaAfterDeathEvent;
import net.deadlydiamond98.koalalib.events.KoalaAfterRespawnEvent;
import net.deadlydiamond98.koalalib.common.items.ModSharedItems;
import net.deadlydiamond98.koalalib.util.MagicConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KoalaLib implements ModInitializer {
	public static final String MOD_ID = "koalalib";

	//Custom Zelda Font
	public static final Identifier ZELDA_FONT = new Identifier(MOD_ID, "zeldafont");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, MagicConfig.class);

		ModSharedItems.register();
		ModSharedSounds.register();


		KoalaAfterRespawnEvent.register();
		KoalaAfterDeathEvent.register();
		MagicBarCommands.register();

		LOGGER.info("KoalaLib finished Loading");
	}



	public static boolean isModLoaded(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}
}