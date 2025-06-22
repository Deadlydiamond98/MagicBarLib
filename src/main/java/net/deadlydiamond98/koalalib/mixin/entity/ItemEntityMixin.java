package net.deadlydiamond98.koalalib.mixin.entity;

import net.deadlydiamond98.koalalib.common.items.interaction.IFloating;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntity.class)
public class ItemEntityMixin extends EntityMixin {
    @Override
    protected boolean koalalib$hasNoGravity(boolean original) {
        ItemEntity item = (ItemEntity) (Object) this;
        return item.getStack().getItem() instanceof IFloating || super.koalalib$hasNoGravity(original);
    }
}
