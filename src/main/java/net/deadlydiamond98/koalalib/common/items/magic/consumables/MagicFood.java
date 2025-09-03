package net.deadlydiamond98.koalalib.common.items.magic.consumables;

import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class MagicFood extends MagicReplenisher {
    private int amountToGive;
    /**
     * @param amountToGive, amount of mana to replenish to the player
     * <p>
     * Use this Item if you want to make the player eat the item like food, otherwise
     * @see MagicFood
     */
    public MagicFood(Settings settings, int amountToGive) {
        super(settings);
        this.amountToGive = amountToGive;
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        MagicBarHelper.addMana(user, this.amountToGive);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (this.isFood()) {

            if (user.canConsume(this.getFoodComponent().isAlwaysEdible()) || MagicBarHelper.canAddMana(user, this.amountToGive)) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            } else {
                return TypedActionResult.fail(itemStack);
            }

        } else {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }
}
