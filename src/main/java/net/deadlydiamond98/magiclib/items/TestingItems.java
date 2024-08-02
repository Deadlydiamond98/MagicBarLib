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

    public static final Item Crystal_Apple = registerItem("crystal_apple",
            new MagicUpgrade(new Item.Settings(), 1000, 100, false, false, 0));
    public static final Item Crystal_Applae = registerItem("crystal_apaple",
            new MagicDowngrade(new Item.Settings(), 100, 100, false, 0));
    public static final Item Crystal_Appdlae = registerItem("crystal_apaplde",
            new MagicConsumable(new Item.Settings(), 100, true, 0));

    //Registration stuff
    private static Item registerItem(String itemName, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MagicLib.MOD_ID, itemName), item);
    }

    public static void registerItems() {
    }
}
