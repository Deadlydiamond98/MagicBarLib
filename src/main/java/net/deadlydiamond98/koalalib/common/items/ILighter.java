package net.deadlydiamond98.koalalib.common.items;

import net.deadlydiamond98.koalalib.init.KoalaLibSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Used for allowing an item to ignite vanilla blocks, and other blocks that use IgnitionHelper.canUseIgniter()
 */
public interface ILighter {
    default boolean onIgnite(BlockState state, World world, BlockPos pos, ItemStack stack, LivingEntity user, Hand hand) {
        world.playSound(user, user.getBlockPos(), getIgniteSound(), SoundCategory.BLOCKS, getVolume(), getPitch(world));
        return true;
    }

    default SoundEvent getIgniteSound() {
        return KoalaLibSounds.TOOL_IGNITE;
    }

    default float getVolume() {
        return 1;
    }

    default float getPitch(World world) {
        return world.getRandom().nextFloat() * 0.4f + 0.8f;
    }
}
