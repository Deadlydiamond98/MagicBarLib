package net.deadlydiamond98.koalalib.mixin.entity;

import net.deadlydiamond98.koalalib.common.items.interaction.IFloating;
import net.deadlydiamond98.koalalib.common.items.interaction.IPickupSound;
import net.deadlydiamond98.koalalib.util.mixindata.IFloatingItemMixinData;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin implements IFloatingItemMixinData {

    @Unique
    private static final Map<PlayerEntity, Long> LAST_PICKUP_TIME = new ConcurrentHashMap<>();

    @Override
    protected boolean koalalib$hasNoGravity(boolean original) {
        ItemEntity item = (ItemEntity) (Object) this;
        return item.getStack().getItem() instanceof IFloating || super.koalalib$hasNoGravity(original);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void koalalib$tick(CallbackInfo ci) {

        ItemEntity item = (ItemEntity) (Object) this;

        if (item.getStack().getItem() instanceof IFloating) {
            this.setVelocity(this.getVelocity().multiply(0.9, 0.9, 0.9));
        }
    }

    @Inject(
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;triggerItemPickedUpByEntityCriteria(Lnet/minecraft/entity/ItemEntity;)V",
            shift = At.Shift.AFTER)
    )
    private void koalalib$onItemPickup(PlayerEntity player, CallbackInfo info) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;
        if (!player.getWorld().isClient) {
            if (itemEntity.getStack().getItem() instanceof IPickupSound item) {
                long currentTime = System.currentTimeMillis();
                long lastPickupTime = LAST_PICKUP_TIME.getOrDefault(player, 0L);
                if (currentTime - lastPickupTime > 500) {
                    player.playSound(item.getSound(), SoundCategory.PLAYERS, item.getVolume(), item.getPitch());
                    LAST_PICKUP_TIME.put(player, currentTime);
                }
            }
        }
    }
}
