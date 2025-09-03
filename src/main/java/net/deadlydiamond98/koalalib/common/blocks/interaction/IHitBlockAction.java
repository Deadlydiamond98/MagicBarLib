package net.deadlydiamond98.koalalib.common.blocks.interaction;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Blocks that implement this interface will be able to perform actions when attacked
 */
public interface IHitBlockAction {

    /**
     * This Method is triggered when the block is hit
     * @param state The Blockstate
     * @param pos The BlockPos
     * @param world The World
     * @param player The Player
     */
    void attack(BlockState state, BlockPos pos, World world, PlayerEntity player);

    /**
     * This is used to return whether the attack() method should trigger if the attack button is held, or only if it's pressed
     */
    boolean allowAttackHolding();

    /**
     * This Method shouldn't be overridden! This is used to determine whether attacks should or shouldn't continue to register if the key is held
     */
    default void attemptAttack(boolean wasAttacking, BlockState state, BlockPos pos, World world, PlayerEntity player) {
        if (!allowAttackHolding() && !wasAttacking) {
            attack(state, pos, world, player);
        } else if (allowAttackHolding()) {
            attack(state, pos, world, player);
        }
    }
}
