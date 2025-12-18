package net.deadlydiamond98.koalalib.init;

import net.deadlydiamond98.koalalib.KoalaLib;
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

    // Entity
    public static final TagKey<EntityType<?>> ENDER_MOB = create("endersoul_dropping_mob", RegistryKeys.ENTITY_TYPE);
    public static final TagKey<EntityType<?>> ENDER_MINI_BOSS = create("endersoul_dropping_miniboss", RegistryKeys.ENTITY_TYPE);

    private static <T> TagKey<T> create(String name, RegistryKey<Registry<T>> key) {
        return TagKey.of(key, new Identifier(KoalaLib.MOD_ID, name));
    }
}
