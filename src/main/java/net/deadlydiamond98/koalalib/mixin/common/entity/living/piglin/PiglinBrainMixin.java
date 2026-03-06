package net.deadlydiamond98.koalalib.mixin.common.entity.living.piglin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.deadlydiamond98.koalalib.init.KoalaLibTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @ModifyReturnValue(method = "wearsGoldArmor", at = @At("RETURN"))
    private static boolean koalalib$wearsGoldArmor(boolean original, @Local(argsOnly = true) LivingEntity entity) {
        for (ItemStack itemStack : entity.getArmorItems()) {
            if (itemStack.isIn(KoalaLibTags.PIGLIN_GOLD_ARMOR)) {
                return true;
            }
        }
        return original;
    }
}
