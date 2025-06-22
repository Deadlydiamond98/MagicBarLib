package net.deadlydiamond98.koalalib.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyReturnValue(method = "hasNoGravity", at = @At("RETURN"))
    protected boolean koalalib$hasNoGravity(boolean original) {
        return original;
    }
}
