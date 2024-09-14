package net.deadlydiamond98.magiclib.items;

import net.deadlydiamond98.magiclib.MagicLib;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TestingItems {
    //Only for testing things, doesn't do anything for the mod

    //Registration stuff
    private static Item registerItem(String itemName, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MagicLib.MOD_ID, itemName), item);
    }

    public static void registerItems() {
    }
}
