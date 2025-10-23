package net.deadlydiamond98.koalalib;

import net.deadlydiamond98.koalalib.common.blocks.ModSharedBlocks;
import net.deadlydiamond98.koalalib.common.effect.ModSharedEffects;

/**
 * This class is used for testing, nothing here should be used!!
 */
public class KoalaLibDebug {
    
    public static void initDebugging() {

        ModSharedBlocks.register();
        ModSharedEffects.register();

        KoalaLib.LOGGER.info("Testing mode is enabled for KoalaLib!!!");
    }
}
