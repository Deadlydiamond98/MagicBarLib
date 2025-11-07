package net.deadlydiamond98.koalalib.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Entities that implement this interface will be able to perform actions when attacked, even if not "hittable"
 */
public interface IHitEntityAction {
    /**
     * This Method is triggered when the block is hit
     * @param entity The Entity
     * @param world The World
     * @param player The Player
     */
    void attack(Entity entity, World world, PlayerEntity player);

    /**
     * This is used to return whether the attack() method should trigger if the attack button is held, or only if it's pressed
     */
    boolean allowAttackHolding();

    /**
     * This Method shouldn't be overridden! This is used to determine whether attacks should or shouldn't continue to register if the key is held
     */
    default void attemptAttack(boolean wasAttacking, Entity entity, World world, PlayerEntity player) {
        if (!allowAttackHolding() && !wasAttacking) {
            attack(entity, world, player);
        } else if (allowAttackHolding()) {
            attack(entity, world, player);
        }
    }
}
