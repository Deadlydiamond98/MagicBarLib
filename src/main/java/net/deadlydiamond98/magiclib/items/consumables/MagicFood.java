package net.deadlydiamond98.magiclib.items.consumables;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class MagicFood extends MagicReplenisher {
    private int amountToGive;
    /**
     * @param amountToGive, amount of mana to replenish to the player
     *
     * Use this Item if you want to make the player eat the item like food, otherwise
     * @see net.deadlydiamond98.magiclib.items.consumables.MagicFood
     */
    public MagicFood(Settings settings, int amountToGive) {
        super(settings);
        this.amountToGive = amountToGive;
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.addMana(this.amountToGive);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (user.canAddMana(this.amountToGive) || user.isCreative()) {
                return super.use(world, user, hand);
            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }
}
