package net.deadlydiamond98.koalalib.compat.trinkets.service;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class NoTrinketHelper implements ITrinketCompat {
    @Override
    public List<ItemStack> getEquippedTrinkets(LivingEntity entity, Predicate<ItemStack> predicate) {
        return List.of();
    }
}
