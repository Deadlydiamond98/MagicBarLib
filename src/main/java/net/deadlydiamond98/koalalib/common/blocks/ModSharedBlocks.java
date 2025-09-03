package net.deadlydiamond98.koalalib.common.blocks;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Currently there are no shared blocks, this purely exists for debugging
 */
public class ModSharedBlocks {

//    public static final Block DEBUG_BLOCK = registerBlock("debug_block", new TestBlock(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK)));

    private static Block registerBlock(String blockName, Block block) {
        registerBlockItem(blockName, block);
        return Registry.register(Registries.BLOCK, new Identifier(KoalaLib.MOD_ID, blockName), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(KoalaLib.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void register() {
    }
}
