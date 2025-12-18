package net.deadlydiamond98.koalalib.mixin.common.entity.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract boolean saveNbt(NbtCompound nbt);

    @ModifyReturnValue(method = "hasNoGravity", at = @At("RETURN"))
    protected boolean koalalib$hasNoGravity(boolean original) {
        return original;
    }

    @ModifyReturnValue(method = "isGlowing", at = @At("RETURN"))
    protected boolean koalalib$isGlowing(boolean original) {
        return original;
    }
}
