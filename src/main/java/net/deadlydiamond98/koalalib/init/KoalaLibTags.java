package net.deadlydiamond98.koalalib.init;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class KoalaLibTags {

    // Item
    public static final TagKey<Item> IGNITER = create("igniter/igniter", RegistryKeys.ITEM);
    public static final TagKey<Item> IGNITER_TOOL = create("igniter/igniter_durability", RegistryKeys.ITEM);
    public static final TagKey<Item> IGNITER_USED = create("igniter/igniter_consumable", RegistryKeys.ITEM);

    public static final TagKey<Item> PIGLIN_GOLD_ARMOR = create("piglin_gold_armor", RegistryKeys.ITEM);

    // Block
    public static final TagKey<Block> WOODEN_MATERIAL = create("wooden", RegistryKeys.BLOCK);
    public static final TagKey<Block> CRACKED_BRICKS = create("cracked_bricks", RegistryKeys.BLOCK);

    // Entity
    public static final TagKey<EntityType<?>> ENDER_MOB = create("endersoul_dropping_mob", RegistryKeys.ENTITY_TYPE);
    public static final TagKey<EntityType<?>> ENDER_MINI_BOSS = create("endersoul_dropping_miniboss", RegistryKeys.ENTITY_TYPE);

    private static <T> TagKey<T> create(String name, RegistryKey<Registry<T>> key) {
        return TagKey.of(key, new Identifier(KoalaLib.MOD_ID, name));
    }
}
