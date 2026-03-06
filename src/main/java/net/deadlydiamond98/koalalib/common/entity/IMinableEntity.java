package net.deadlydiamond98.koalalib.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Deprecated(forRemoval = true)
public interface IMinableEntity extends IHitEntityAction {

    // Breaking Progress

    float getBreakProgress();
    void setBreakProgress(float progress);

    // Prev Breaking Progress

    float getPrevBreakProgress();
    void setPrevBreakProgress(float progress);

    // Breaking Sound Variables

    float getBreakSoundCooldown();
    void setBreakSoundCooldown(float progress);

    /**
     * The block that determines how the entity is mined (Hardness, Particles, etc.)
     */
    BlockState getBlock();

    @Override
    default void attack(Entity entity, World world, PlayerEntity player) {
        if (canMineMob(entity, world, player)) {
            player.swingHand(Hand.MAIN_HAND);
            setBreakProgress(getBreakProgress() + getBlock().calcBlockBreakingDelta(player, world, entity.getBlockPos()));

            if (world instanceof ServerWorld server) {
                Vec3d offset = entity.getPos().offset(getDirection(player, entity.getPos()), 0.51f);
                server.spawnParticles(
                        new BlockStateParticleEffect(ParticleTypes.BLOCK, getBlock()),
                        offset.x, offset.y + 0.5, offset.z, 1,
                        0.25, 0.25, 0.25, 0.1
                );

                if (player.isCreative()) {
                    breakBlock(entity, world, player);
                }

                if (getBreakSoundCooldown() % 4 == 0) {
                    BlockSoundGroup soundGroup = getBlock().getSoundGroup();
                    world.playSound(null, entity.getBlockPos(), soundGroup.getHitSound(), SoundCategory.BLOCKS,
                            (soundGroup.getVolume() + 1.0f) / 8.0f, soundGroup.getPitch() * 0.5f
                    );
                }
                setBreakSoundCooldown(getBreakSoundCooldown() + 1);

                if (getBreakProgress() >= 1) {
                    breakBlock(entity, world, player);
                }
            }
        }
    }

    /**
     * Allows you to mine the entity if true
     */
    default boolean canMineMob(Entity entity, World world, PlayerEntity player) {
        return true;
    }

    /**
     * Breaks the Entity as if it were a block
     */
    default void breakBlock(Entity entity, World world, PlayerEntity player) {
        if (player.canHarvest(getBlock()) && !entity.isRemoved() && !player.isCreative()) {
            dropBlockLoot(entity, world);
        }
        world.playSound(null, entity.getBlockPos(), getBlock().getSoundGroup().getBreakSound(),
                SoundCategory.BLOCKS, 1, 1
        );
        entity.discard();
    }

    /**
     * Drops loot upon the entity being broken
     */
    default void dropBlockLoot(Entity entity, World world) {
        Block.dropStacks(getBlock(), world, entity.getBlockPos());
    }


    /**
     * This goes in the tick method to reset breaking if the player stops breaking the block
     */
    default void updateBreakingTick() {
        if (getBreakProgress() == getPrevBreakProgress()) {
            setBreakProgress(0);
        }

        setPrevBreakProgress(getBreakProgress());
    }

    /**
     * Returns the Break Stage, which can be used in the entity's renderer for rendering the breaking overlay
     */
    default int getBreakStage() {
        return getBreakProgress() > 0.0f ? Math.min(9, (int)(getBreakProgress() * 10.0f)) : -1;
    }

    /**
     * Gets the direction towards the player from the entity's position
     */
    default Direction getDirection(PlayerEntity player, Vec3d center) {
        double dx = player.getX() - (center.getX());
        double dz = player.getZ() - (center.getZ());

        double angleRadians = Math.atan2(dz, dx);
        double angleDegrees = Math.toDegrees(angleRadians);

        angleDegrees = (angleDegrees + 360) % 360;

        Direction direction;

        if (angleDegrees >= 45 && angleDegrees < 135) {
            direction = Direction.SOUTH;
        } else if (angleDegrees >= 135 && angleDegrees < 225) {
            direction = Direction.WEST;
        } else if (angleDegrees >= 225 && angleDegrees < 315) {
            direction = Direction.NORTH;
        } else {
            direction = Direction.EAST;
        }
        return direction;
    }

    @Override
    default boolean allowAttackHolding() {
        return true;
    }
}