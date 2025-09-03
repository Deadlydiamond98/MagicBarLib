package net.deadlydiamond98.koalalib;


import net.deadlydiamond98.koalalib.common.blocks.ModSharedBlocks;
import net.deadlydiamond98.koalalib.common.commands.MagicBarCommands;
import net.deadlydiamond98.koalalib.common.misc.ModSharedSounds;
import net.deadlydiamond98.koalalib.common.events.KoalaAfterDeathEvents;
import net.deadlydiamond98.koalalib.common.events.KoalaAfterRespawnEvents;
import net.deadlydiamond98.koalalib.common.items.ModSharedItems;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.deadlydiamond98.koalalib.config.configs.MagicBarConfigs;
import net.deadlydiamond98.koalalib.config.configs.MainConfigs;
import net.deadlydiamond98.koalalib.networking.KoalaPackets;
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
		KoalaConfigCreator.addModConfig(MOD_ID, MainConfigs.class);
		KoalaConfigCreator.addModConfigCategory(MOD_ID, "magic_bar", MagicBarConfigs.class);

		ToggleableContent.enableMagicBar(true);

		ModSharedItems.register();
		ModSharedBlocks.register();
		ModSharedSounds.register();

		KoalaAfterRespawnEvents.register();
		KoalaAfterDeathEvents.register();
		MagicBarCommands.register();
		KoalaPackets.registerC2SPackets();

		LOGGER.info("KoalaLib finished Loading");
	}



	public static boolean isModLoaded(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}
}