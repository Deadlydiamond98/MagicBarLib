package net.deadlydiamond98.koalalib.util.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ItemModelDatagenUtil {

    public static final Model SPAWN_EGG = new Model(Optional.of(new Identifier("item/template_spawn_egg")), Optional.empty());

    public static void registerGenerated(ItemModelGenerator itemModelGenerator, Item... items) {
        bulkItemModelRegister(itemModelGenerator, Models.GENERATED, items);
    }

    public static void registerHandheld(ItemModelGenerator itemModelGenerator, Item... items) {
        bulkItemModelRegister(itemModelGenerator, Models.HANDHELD, items);
    }

    public static void registerSpawnEggs(ItemModelGenerator itemModelGenerator, Item... items) {
        bulkItemModelRegister(itemModelGenerator, SPAWN_EGG, items);
    }

    public static void bulkItemModelRegister(ItemModelGenerator itemModelGenerator, Model model, Item... items) {
        for (Item item : items) {
            itemModelGenerator.register(item, model);
        }
    }
}