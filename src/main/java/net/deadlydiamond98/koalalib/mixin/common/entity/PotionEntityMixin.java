package net.deadlydiamond98.koalalib.mixin.common.entity;

import net.deadlydiamond98.koalalib.common.blocks.IExtinguish;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionEntity.class)
public class PotionEntityMixin {

    // Extinguishes flames of extinguishable blocks with water bottles

    @Inject(method = "extinguishFire", at = @At("TAIL"))
    private void extingishFlameBlocks(BlockPos pos, CallbackInfo ci) {

        PotionEntity potionEntity = ((PotionEntity) (Object) this);
        BlockState state = potionEntity.getWorld().getBlockState(pos);

        if (state.getBlock() instanceof IExtinguish extinguish && extinguish.isLit(state)) {
            extinguish.extinguish(potionEntity.getOwner(), potionEntity.getWorld(), pos, state);
        }
    }
}
