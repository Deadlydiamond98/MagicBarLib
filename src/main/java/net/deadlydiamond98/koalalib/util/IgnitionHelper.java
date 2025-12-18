package net.deadlydiamond98.koalalib.util;

import net.deadlydiamond98.koalalib.common.items.ILighter;
import net.deadlydiamond98.koalalib.init.KoalaLibSounds;
import net.deadlydiamond98.koalalib.init.KoalaLibTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Helper Class for Using Custom Ignition Items on blocks
 */
public class IgnitionHelper {
    /**
     * Called on Vanilla Blocks (Campfire, Candle, TNT, Candle Cake) for lighting them with custom items
     * @param state the block state
     * @param world the world
     * @param pos the pos
     * @param user the user
     * @param hand the hand
     * @return will return true if the item isn't a flint and steel, and is in the Igniter Tag
     */
    public static boolean canUseIgniterNonVanilla(BlockState state, World world, BlockPos pos, LivingEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        return !stack.isOf(Items.FLINT_AND_STEEL) && !stack.isOf(Items.FIRE_CHARGE) && canUseIgniter(state, world, pos, user, hand);
    }

    public static boolean canUseIgniter(BlockState state, World world, BlockPos pos, LivingEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (stack.isIn(KoalaLibTags.IGNITER) && user instanceof PlayerEntity player) {
            boolean bl = true;

            if (stack.getItem() instanceof ILighter lighter) {
                bl = lighter.onIgnite(state, world, pos, stack, user, hand);
            } else {
                if (stack.isIn(KoalaLibTags.IGNITER_TOOL) && stack.isDamageable()) {
                    doFlintAndSteelAction(stack, player, hand);
                } else if (stack.isIn(KoalaLibTags.IGNITER_USED)) {
                    doFireChargeAction(stack, player);
                }
            }
            if (bl) {
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            }
            return bl;
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void doFlintAndSteelAction(ItemStack stack, PlayerEntity user, Hand hand) {
        playSound(user, KoalaLibSounds.TOOL_IGNITE, 0.4f, 0.8f);
        if (!user.isCreative()) {
            if (hand != null) {
                stack.damage(1, user, playerx -> playerx.sendToolBreakStatus(hand));
            } else {
                stack.setDamage(stack.getDamage() + 1);
            }
        }
    }

    private static void doFireChargeAction(ItemStack stack, PlayerEntity user) {
        playSound(user, KoalaLibSounds.ITEM_IGNITE, 0.2f, 1.0f);
        if (!user.isCreative()) {
            stack.decrement(1);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void playSound(LivingEntity user, SoundEvent sound, float pitchMulti, float pitchAdd) {
        World world = user.getWorld();
        float pitch = world.getRandom().nextFloat() * pitchMulti + pitchAdd;
        world.playSound(user, user.getBlockPos(), sound, SoundCategory.BLOCKS, 1, pitch);
    }
}
