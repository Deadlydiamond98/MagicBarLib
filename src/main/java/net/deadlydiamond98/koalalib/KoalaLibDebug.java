package net.deadlydiamond98.koalalib;

import net.deadlydiamond98.koalalib.init.KoalaLibBlocks;
import net.deadlydiamond98.koalalib.init.KoalaLibEffects;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.File;

/**
 * This class is used for testing, nothing here should be used!!
 */
public class KoalaLibDebug {
    
    public static void initDebugging() {

        KoalaLibBlocks.register();
        KoalaLibEffects.register();

//        ModSharedItems.register("test_item",
//                new CustomBundleItem(new Item.Settings().maxCount(1), 128, true, stack -> true)
//        );

        KoalaLib.LOGGER.info("Testing mode is enabled for KoalaLib!!!");
    }
}
