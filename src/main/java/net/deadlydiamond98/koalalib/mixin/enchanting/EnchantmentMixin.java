package net.deadlydiamond98.koalalib.mixin.enchanting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.deadlydiamond98.koalalib.common.items.vanillamodified.IExtraEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @ModifyReturnValue(method = "isAcceptableItem", at = @At(value = "RETURN"))
    protected boolean koalalib$isAcceptableItem(boolean original, ItemStack itemStack) {
        if (itemStack.getItem() instanceof IExtraEnchantments extraEnchants) {
            return extraEnchants.getEnchantments().contains((Enchantment) (Object) this);
        }
        return original;
    }
}
