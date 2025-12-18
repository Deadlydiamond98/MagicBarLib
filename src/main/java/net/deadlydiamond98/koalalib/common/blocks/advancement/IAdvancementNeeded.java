package net.deadlydiamond98.koalalib.common.blocks.advancement;

import net.deadlydiamond98.koalalib.util.mixinterfaces.player.IPlayerOtherMixinData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;

/**
 * Interface with some helper methods for blocks that require an advancement to break
 */
public interface IAdvancementNeeded {

    default boolean isPlayerPlaced(ItemPlacementContext ctx) {
        if (ctx.getPlayer() != null) {
            return !ctx.getPlayer().isCreative();
        }
        return false;
    }

    default boolean hasAdvancment(PlayerEntity player, String id) {
        return ((IPlayerOtherMixinData)player).koalalib$hasAdvancement(id);
    }
}
