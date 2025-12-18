package net.deadlydiamond98.koalalib.mixin.common.entity.item;

import net.deadlydiamond98.koalalib.common.items.interaction.IAdvancedItemProperties;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void koalalib$tick(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (entity.getStack().getItem() instanceof IAdvancedItemProperties properties) {
            properties.onItemEntityTick(entity, entity.getStack());
        }
    }

    @Override
    protected boolean koalalib$hasNoGravity(boolean original) {
        ItemEntity entity = (ItemEntity) (Object) this;
        boolean bl = false;
        if (entity.getStack().getItem() instanceof IAdvancedItemProperties properties) {
            bl = properties.hasNoGravity(entity, entity.getStack());
        }
        return bl || super.koalalib$hasNoGravity(original);
    }

    @Override
    protected boolean koalalib$isGlowing(boolean original) {
        ItemEntity entity = (ItemEntity) (Object) this;
        boolean bl = false;
        if (entity.getStack().getItem() instanceof IAdvancedItemProperties properties) {
            bl = properties.isGlowing(entity, entity.getStack());
        }
        return bl || super.koalalib$isGlowing(original);
    }

    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void koalalib$onItemPickup(PlayerEntity player, CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (entity.getStack().getItem() instanceof IAdvancedItemProperties properties) {
            properties.onPlayerCollision(entity, entity.getStack(), player);
        }
    }


    @Inject(
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;triggerItemPickedUpByEntityCriteria(Lnet/minecraft/entity/ItemEntity;)V",
            shift = At.Shift.AFTER)
    )
    private void koalalib$onPlayerCollisionServer(PlayerEntity player, CallbackInfo info) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (entity.getStack().getItem() instanceof IAdvancedItemProperties properties) {
            properties.onPlayerPickingUp(entity.getStack(), player);
        }
    }
}
