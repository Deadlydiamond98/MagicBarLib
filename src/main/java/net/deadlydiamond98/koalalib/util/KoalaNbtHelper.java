package net.deadlydiamond98.koalalib.util;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

/**
 * Various NBT Read and Write Methods
 */
public class KoalaNbtHelper {

    public static void writeVec3d(String name, Vec3d vec, NbtCompound nbt) {
        nbt.putDouble(name + "X", vec.x);
        nbt.putDouble(name + "Y", vec.y);
        nbt.putDouble(name + "Z", vec.z);
    }

    public static Vec3d readVec3d(String name, NbtCompound nbt) {
        double x = nbt.getDouble(name + "X");
        double y = nbt.getDouble(name + "Y");
        double z = nbt.getDouble(name + "Z");
        return new Vec3d(x, y, z);
    }

    /**
     * Custom method for writing ItemStack to NBT since ItemStack.toNbt() turns count into a byte
     * @param stack the ItemStack
     * @return returns the NBT Compound
     */
    public static NbtCompound largeItemStackToNBT(ItemStack stack) {
        NbtCompound nbt = new NbtCompound();
        Identifier identifier = Registries.ITEM.getId(stack.getItem());
        nbt.putString("id", identifier.toString());
        nbt.putInt("Count", stack.getCount());
        if (stack.getNbt() != null) {
            nbt.put("tag", stack.getNbt().copy());
        }
        return nbt;
    }

    /**
     * Custom method for reading ItemStack from NBT since ItemStack.fromNbt() reads count as a byte
     * @param nbt the NBT to read from
     * @return returns the ItemStack
     */
    public static ItemStack largeItemStackFromNBT(NbtCompound nbt) {
        try {
            Item item = Registries.ITEM.get(new Identifier(nbt.getString("id")));
            int count = nbt.getInt("Count");

            Optional<NbtCompound> itemNBT = Optional.empty();
            if (nbt.contains("tag", 10)) {
                itemNBT = Optional.of(nbt.getCompound("tag"));
                item.postProcessNbt(itemNBT.get());
            }

            ItemStack stack = new ItemStack(item, count);
            itemNBT.ifPresent(stack::setNbt);

            if (stack.getItem().isDamageable()) {
                stack.setDamage(stack.getDamage());
            }

            return stack;
        } catch (RuntimeException exception) {
            KoalaLib.LOGGER.debug("Tried to load invalid item: {}", nbt, exception);
            return ItemStack.EMPTY;
        }
    }
}
