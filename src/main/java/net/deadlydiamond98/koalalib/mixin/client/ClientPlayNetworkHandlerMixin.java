package net.deadlydiamond98.koalalib.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.deadlydiamond98.koalalib.common.entity.IMimicItemPickup;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @ModifyArgs(method = "onItemPickupAnimation", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V", ordinal = 1))
    private void koalalib$onItemPickupAnimation(Args args, @Local Entity entity) {
        if (entity instanceof IMimicItemPickup itemPickup && itemPickup.getPickUpSound() != null) {
            args.set(3, itemPickup.getPickUpSound());
            args.set(5, itemPickup.getPickUpVolume());
            args.set(6, itemPickup.getPickUpPitch());
        }
    }
}
