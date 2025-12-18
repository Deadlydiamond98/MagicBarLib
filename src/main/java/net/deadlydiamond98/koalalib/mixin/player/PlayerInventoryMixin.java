package net.deadlydiamond98.koalalib.mixin.player;

import net.deadlydiamond98.koalalib.common.items.interaction.IAdvancedItemProperties;
import net.deadlydiamond98.koalalib.common.items.vanillamodified.CustomBundleItem;
import net.deadlydiamond98.koalalib.compat.KoalaCompatServices;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow @Final public PlayerEntity player;

    @Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    public void koalalib$insertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        // Trinkets Compatibility
        for (ItemStack trinket : KoalaCompatServices.TRINKETS_COMPAT.getEquippedTrinkets(this.player)) {
            if (koalalib$addItemToBundle(stack, trinket)) {
                cir.setReturnValue(true);
            }
        }

        for (int i = 0; i < this.player.getInventory().size(); i++) {
            ItemStack bundleStack = this.player.getInventory().getStack(i);
            if (koalalib$addItemToBundle(stack, bundleStack)) {
                cir.setReturnValue(true);
            }
        }

        if (stack.getItem() instanceof IAdvancedItemProperties properties) {
            if (properties.onInventoryInsertion(this.player, stack, slot)) {
                cir.setReturnValue(true);
                stack.setCount(0);
            }
        }
    }

    @Unique
    private boolean koalalib$addItemToBundle(ItemStack stack, ItemStack bundleStack) {
        if (bundleStack.getItem() instanceof CustomBundleItem bundle && bundle.canInsertOnPickup()) {
            int max = bundle.getMaxInsertables(bundleStack);
            int count = stack.getCount();
            if (CustomBundleItem.addToBundle(bundleStack, stack)) {
                this.player.increaseStat(Stats.PICKED_UP.getOrCreateStat(stack.getItem()), Math.min(count, max));
                bundle.playInsertSound(this.player);
                return true;
            }
        }
        return false;
    }
}
