package net.deadlydiamond98.koalalib.mixin.common.block.ignitable;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.deadlydiamond98.koalalib.util.IgnitionHelper;
import net.minecraft.block.*;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TntBlock.class)
public class TntBlockMixin {

    @WrapMethod(method = "onUseWithItem")
    private ItemActionResult koalalib$onUse(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, Operation<ItemActionResult> original) {
        if (IgnitionHelper.canUseIgniterNonVanilla(state, world, pos, player, hand) && CampfireBlock.canBeLit(state)) {
            koalalib$primeTnt(world, pos, player);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            return ItemActionResult.success(world.isClient);
        }
        return original.call(stack, state, world, pos, player, hand, hit);
    }

    @Unique
    private void koalalib$primeTnt(World world, BlockPos pos, PlayerEntity igniter) {
        if (world.isClient) {
            return;
        }
        TntEntity tntEntity = new TntEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, igniter);
        world.spawnEntity(tntEntity);
        world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
    }
}
