package net.deadlydiamond98.koalalib;


import net.deadlydiamond98.koalalib.common.blocks.ModSharedBlocks;
import net.deadlydiamond98.koalalib.common.commands.MagicBarCommands;
import net.deadlydiamond98.koalalib.common.events.KoalaOnJoinServerEvent;
import net.deadlydiamond98.koalalib.common.misc.ModSharedSounds;
import net.deadlydiamond98.koalalib.common.events.KoalaAfterDeathEvents;
import net.deadlydiamond98.koalalib.common.events.KoalaAfterRespawnEvents;
import net.deadlydiamond98.koalalib.common.items.ModSharedItems;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.deadlydiamond98.koalalib.config.KoalaLibConfigs;
import net.deadlydiamond98.koalalib.networking.KoalaPackets;
import net.deadlydiamond98.koalalib.updater.KoalaUpdateChecker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KoalaLib implements ModInitializer {
	public static final String MOD_ID = "koalalib";

	public static final Identifier ZELDA_FONT = new Identifier(MOD_ID, "zeldafont");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		KoalaConfigCreator.addModConfig(MOD_ID, KoalaLibConfigs.Main.class);
		KoalaUpdateChecker.addModUpdateChecker(MOD_ID);

		if (isDev()) {
			KoalaLibDebug.initDebugging();
		}

		ModSharedItems.register();
		ModSharedSounds.register();
		MagicBarCommands.register();
		KoalaPackets.registerC2SPackets();

		KoalaAfterRespawnEvents.register();
		KoalaAfterDeathEvents.register();
		KoalaOnJoinServerEvent.register();

		LOGGER.info("KoalaLib finished Loading");
	}

	/**
	 * Returns true if in Dev Environment and when there's exactly 59 mods loaded <br>
	 * (which would mean this mod is being used as a dependency)
	 */
	public static boolean isDev() {
		FabricLoader loader = FabricLoader.getInstance();
		return loader.isDevelopmentEnvironment() && loader.getAllMods().size() == 59;
	}

	public static boolean isModLoaded(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}
}