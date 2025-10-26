package net.deadlydiamond98.koalalib.compat.trinkets.service;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TrinketHelper implements ITrinketCompat {

    @Nullable
    private static TrinketComponent getTrinketComponent(LivingEntity entity) {
        Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(entity);
        return optional.orElse(null);
    }

    @Override
    public List<ItemStack> getEquippedTrinkets(LivingEntity entity, Predicate<ItemStack> predicate) {
        TrinketComponent component = getTrinketComponent(entity);
        List<ItemStack> stacks = new ArrayList<>();
        if (component != null) {
            for (Pair<SlotReference, ItemStack> pair : component.getEquipped(predicate)) {
                stacks.add(pair.getRight());
            }
        }
        return stacks;
    }
}
