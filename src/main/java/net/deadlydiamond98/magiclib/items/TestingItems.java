package net.deadlydiamond98.magiclib.items;

import net.deadlydiamond98.magiclib.MagicLib;
import net.deadlydiamond98.magiclib.items.consumables.MagicConsumable;
import net.deadlydiamond98.magiclib.items.consumables.MagicDowngrade;
import net.deadlydiamond98.magiclib.items.consumables.MagicFood;
import net.deadlydiamond98.magiclib.items.consumables.MagicUpgrade;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TestingItems {

    //Registration stuff
    private static Item registerItem(String itemName, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MagicLib.MOD_ID, itemName), item);
    }

    public static void registerItems() {
    }
}
