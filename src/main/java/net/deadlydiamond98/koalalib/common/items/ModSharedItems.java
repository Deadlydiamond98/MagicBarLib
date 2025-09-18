package net.deadlydiamond98.koalalib.common.items;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.common.items.interaction.FloatingItem;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModSharedItems {

    public static final Item ENDER_SOUL = registerItem("ender_soul", new FloatingItem(new Item.Settings()));

//    public static final Item TEST = registerItem("test", new TestItem(new Item.Settings()));

    public static Item registerItem(String itemName, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(KoalaLib.MOD_ID, itemName), item);
    }

    public static void register() {}
}
