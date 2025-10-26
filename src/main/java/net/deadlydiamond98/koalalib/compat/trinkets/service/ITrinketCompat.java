package net.deadlydiamond98.koalalib.compat.trinkets.service;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.function.Predicate;

public interface ITrinketCompat {

    default List<ItemStack> getEquippedTrinkets(LivingEntity entity, TagKey<Item> tag) {
        return this.getEquippedTrinkets(entity, stack -> stack.isIn(tag));
    }

    default List<ItemStack> getEquippedTrinkets(LivingEntity entity, Item item) {
        return this.getEquippedTrinkets(entity, stack -> stack.isOf(item));
    }

    default List<ItemStack> getEquippedTrinkets(LivingEntity entity) {
        return this.getEquippedTrinkets(entity, stack -> true);
    }

    List<ItemStack> getEquippedTrinkets(LivingEntity entity, Predicate<ItemStack> predicate);
}
