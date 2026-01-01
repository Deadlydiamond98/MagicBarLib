package net.deadlydiamond98.koalalib.mixin.common.block.ignitable;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.deadlydiamond98.koalalib.util.IgnitionHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CandleBlock.class)
public class CandleBlockMixin {

    @WrapMethod(method = "onUseWithItem")
    private ItemActionResult koalalib$onUse(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, Operation<ItemActionResult> original) {
        if (IgnitionHelper.canUseIgniterNonVanilla(state, world, pos, player, hand) && CampfireBlock.canBeLit(state)) {
            world.setBlockState(pos, state.with(Properties.LIT, true));
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return ItemActionResult.success(world.isClient);
        }
        return original.call(stack, state, world, pos, player, hand, hit);
    }
}
