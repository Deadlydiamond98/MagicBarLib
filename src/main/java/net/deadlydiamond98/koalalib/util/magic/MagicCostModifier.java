package net.deadlydiamond98.koalalib.util.magic;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MagicCostModifier {
    public enum Operation {
        ADDITION,
        MULTIPLY_BASE,
        MULTIPLY_TOTAL
    }

    private final List<Double> additions = new ArrayList<>();
    private final List<Double> multiplysBase = new ArrayList<>();
    private final List<Double> multiplysTotal = new ArrayList<>();

    public void applyModifier(Operation operation, double value) {
        switch (operation) {
            case ADDITION -> this.additions.add(value);
            case MULTIPLY_BASE -> this.multiplysBase.add(value);
            case MULTIPLY_TOTAL -> this.multiplysTotal.add(value);
        }
    }

//    public void applyEnchantments(ItemStack stack) {
//        EnchantmentHelper.get(stack).forEach((enchant, lvl) -> {
//            if (enchant instanceof MagicModifierEnchantment magicEnchantment) {
//                applyModifier(
//                        magicEnchantment.getOperationModifer(enchant, lvl),
//                        magicEnchantment.getCostModifier(enchant, lvl)
//                );
//            }
//        });
//    }

    public int calculate(int base) {
        double d = base;
        for (double modifier : this.additions) {
            d += modifier;
        }
        double e = d;
        for (double modifier : this.multiplysBase) {
            e += d * modifier;
        }
        for (double modifier : this.multiplysTotal) {
            e *= 1.0 + modifier;
        }
        return (int) Math.round(e);
    }
}
