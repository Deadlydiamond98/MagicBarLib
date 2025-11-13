package net.deadlydiamond98.koalalib.mixin.entity.living;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.deadlydiamond98.koalalib.common.items.vanillamodified.CustomShieldItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow protected ItemStack activeItemStack;

    @WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
    private void koalalib$damage(LivingEntity instance, float amount, Operation<Void> original, @Local(argsOnly = true) DamageSource source) {
        if (this.activeItemStack.getItem() instanceof CustomShieldItem && instance instanceof PlayerEntity player) {
            CustomShieldItem.attemptDamageSheild(player, player.getActiveItem(), amount, source);
        } else {
            original.call(instance, amount);
        }
    }
}
