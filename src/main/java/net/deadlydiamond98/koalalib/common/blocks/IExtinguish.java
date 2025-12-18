package net.deadlydiamond98.koalalib.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public interface IExtinguish {
    default boolean isLit(BlockState state) {
        return state.contains(Properties.LIT) && state.get(Properties.LIT);
    }

    default void extinguish(@Nullable Entity entity, WorldAccess world, BlockPos pos, BlockState state) {
        world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, pos, 0);
        world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
        world.setBlockState(pos, state.with(Properties.LIT, false), Block.NOTIFY_ALL);
    }
}
