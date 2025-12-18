package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IAdvancedItemProperties {
    /**
     * Runs every tick for an Item Entity
     * @param entity The Item Entity
     * @param stack The Item Stack
     */
    default void onItemEntityTick(ItemEntity entity, ItemStack stack) {
        if (hasNoGravity(entity, stack)) {
            // Prevents Item from flying somewhere unreachable with no Gravity
            entity.setVelocity(entity.getVelocity().multiply(0.9, 0.9, 0.9));
        }
    }

    /**
     * If true, the Item Entity will have no Gravity
     * @param entity The Item Entity
     * @param stack The Item Stack
     * @return returns whether the item will or will not have gravity
     */
    default boolean hasNoGravity(ItemEntity entity, ItemStack stack) {
        return false;
    }

    /**
     * If true the item will glow
     * @param entity The Item Entity
     * @param stack The Item Stack
     * @return returns true if the item glows
     */
    default boolean isGlowing(ItemEntity entity, ItemStack stack) {
        return false;
    }

    /**
     * Triggers Whenever the Item Entity collides with the Player, regardless on whether the item is picked up or not
     * @param entity The Item Entity
     * @param stack The Item Stack
     * @param player The Player the Item Entity is colliding with
     */
    default void onPlayerCollision(ItemEntity entity, ItemStack stack, PlayerEntity player) {}

    /**
     * Triggers when an item is collided with and picked up. Only Runs on Server-Side
     * @param stack The Item Stack
     * @param player The Player the Item Entity is colliding with
     */
    default void onPlayerPickingUp(ItemStack stack, PlayerEntity player) {

    }

    /**
     * Triggers When the item attempts to insert into the player's inventory.
     *
     * @param player
     * @param stack  The Item Stack
     * @param slot   The Slot the Item attempts to insert into
     * @return If true, the item won't enter the inventory on insertion
     */
    default boolean onInventoryInsertion(PlayerEntity player, ItemStack stack, int slot) {
        return false;
    }
}
