package net.deadlydiamond98.koalalib.compat;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.compat.trinkets.service.ITrinketCompat;

import java.util.Iterator;
import java.util.ServiceLoader;

public class KoalaCompatServices {
    public static final ITrinketCompat TRINKETS_COMPAT = loadService("trinkets", ITrinketCompat.class);

    private static <T> T loadService(String modID, Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        if (KoalaLib.isModLoaded(modID)) {
            return loader.findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        }
        Iterator<T> iterator = loader.iterator();

        if (iterator.hasNext()) {
            iterator.next();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        throw new NullPointerException("Failed to load fallback service for " + clazz.getName());
    }
}
