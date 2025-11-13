package net.deadlydiamond98.koalalib.init;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class KoalaLibTags {
    public static class Entities {
        public static final TagKey<EntityType<?>> ENDER_MOB = createTag("endersoul_dropping_mob");
        public static final TagKey<EntityType<?>> ENDER_MINI_BOSS = createTag("endersoul_dropping_miniboss");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(KoalaLib.MOD_ID, name));
        }
    }
}
