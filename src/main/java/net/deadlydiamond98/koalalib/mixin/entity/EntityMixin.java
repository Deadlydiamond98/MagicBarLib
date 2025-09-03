package net.deadlydiamond98.koalalib.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.deadlydiamond98.koalalib.common.items.interaction.IFloating;
import net.deadlydiamond98.koalalib.util.mixindata.IFloatingItemMixinData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract void setVelocity(Vec3d velocity);

    @Shadow public abstract Vec3d getVelocity();

    @ModifyReturnValue(method = "hasNoGravity", at = @At("RETURN"))
    protected boolean koalalib$hasNoGravity(boolean original) {
        return original;
    }
}
