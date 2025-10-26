package net.deadlydiamond98.koalalib;

import net.deadlydiamond98.koalalib.common.blocks.ModSharedBlocks;
import net.deadlydiamond98.koalalib.common.effect.ModSharedEffects;
import net.deadlydiamond98.koalalib.common.items.ModSharedItems;
import net.deadlydiamond98.koalalib.common.items.vanillamodified.CustomBundleItem;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;

/**
 * This class is used for testing, nothing here should be used!!
 */
public class KoalaLibDebug {
    
    public static void initDebugging() {

        ModSharedBlocks.register();
        ModSharedEffects.register();

//        ModSharedItems.register("test_item",
//                new CustomBundleItem(new Item.Settings().maxCount(1), 128, true, stack -> true)
//        );

        KoalaLib.LOGGER.info("Testing mode is enabled for KoalaLib!!!");
    }
}
