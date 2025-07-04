package net.deadlydiamond98.koalalib.mixin.entity;

import net.deadlydiamond98.koalalib.common.items.vanillamodified.CustomShieldItem;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Unique
    private DamageSource koalalib$shieldSource;

    @Shadow public abstract ItemCooldownManager getItemCooldownManager();

    @Inject(method = "damage", at = @At("HEAD"))
    private void koalalib$getSheildDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.koalalib$shieldSource = source;
    }

    @Inject(method = "damageShield", at = @At("HEAD"))
    private void koalalib$shieldDurability(float amount, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        CustomShieldItem.attemptDamageSheild(player, player.getActiveItem(), amount, this.koalalib$shieldSource);
    }

    @Inject(method = "disableShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;clearActiveItem()V"))
    private void koalalib$disableSheild(boolean sprinting, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        CustomShieldItem.disableShield(player, this.getItemCooldownManager());
    }
}
