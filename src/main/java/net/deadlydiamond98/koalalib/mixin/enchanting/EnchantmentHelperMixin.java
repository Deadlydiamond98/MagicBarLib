package net.deadlydiamond98.koalalib.mixin.enchanting;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.deadlydiamond98.koalalib.common.items.vanillamodified.IExtraEnchantments;
import net.deadlydiamond98.koalalib.common.misc.TableCompatEnchant;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @WrapOperation(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean koalalib$getPossibleEntries(EnchantmentTarget instance, Item item, Operation<Boolean> original, @Local(argsOnly = true) ItemStack stack, @Local() Enchantment enchantment) {
        if (enchantment instanceof TableCompatEnchant enchant) {
            return enchant.canAppearInEnchantingTable(stack);
        } else if (item instanceof IExtraEnchantments extras && extras.getEnchantments().contains(enchantment)) {
            return true;
        }
        return original.call(instance, item);
    }
}
