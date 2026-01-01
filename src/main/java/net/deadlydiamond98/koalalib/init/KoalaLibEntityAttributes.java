package net.deadlydiamond98.koalalib.init;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class KoalaLibEntityAttributes {
    public static final RegistryEntry<EntityAttribute> GENERIC_MAX_MAGIC = register("generic.max_magic", 100.0, 1.0, 999999.0, true);

    private static RegistryEntry<EntityAttribute> register(String id, double fallback, double min, double max, boolean tracked) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(KoalaLib.MOD_ID, id), new ClampedEntityAttribute("attribute.name." + id, fallback, min, max).setTracked(tracked));
    }
}
