package net.deadlydiamond98.koalalib.init;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

/**
 * Currently there are no shared blocks, this purely exists for debugging
 */
public class KoalaLibBlocks {

//    public static final Block DEBUG_BLOCK = register("debug_block", new TestBlock(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK)));

    private static Block register(String name, Block block) {
        return RegistryHelper.Blocks.register(new Identifier(KoalaLib.MOD_ID, name), block, true);
    }

    public static void register() {}
}
