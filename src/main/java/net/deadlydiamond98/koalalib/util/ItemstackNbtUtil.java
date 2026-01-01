package net.deadlydiamond98.koalalib.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemstackNbtUtil {

    public static NbtCompound getOrCreateNbt(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);

        if (customData == null) {
            return new NbtCompound();
        }

        return customData.getNbt();
    }

    public static NbtCompound getNbt(ItemStack stack) {
        return stack.get(DataComponentTypes.CUSTOM_DATA).getNbt();
    }

    public static void setNbt(ItemStack stack, NbtCompound nbt) {
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }
}
