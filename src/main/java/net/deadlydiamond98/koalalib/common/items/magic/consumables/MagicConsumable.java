package net.deadlydiamond98.koalalib.common.items.magic.consumables;

import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MagicConsumable extends MagicReplenisher {
    private final int amountToGive;
    private final boolean consumed;
    private final int cooldown;

    /**
     * @param amountToGive, amount of mana to replenish to the player
     * @param consumed, Whether the item is consumed on use
     * @param cooldown, Item use Cooldown, if any
     *
     * Use this Item if you don't want to make the player eat the item like food, otherwise
     *
     * @see MagicFood
     */
    public MagicConsumable(Settings settings, int amountToGive, boolean consumed, int cooldown) {
        super(settings);
        this.amountToGive = amountToGive;
        this.consumed = consumed;
        this.cooldown = cooldown;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            MagicBarHelper.addMana(user, this.amountToGive);
            if (this.consumed) {
                user.getStackInHand(hand).decrement(1);
            }
            if (this.cooldown > 0) {
                user.getItemCooldownManager().set(this, this.cooldown);
            }
            afterUse(user);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }

    protected void afterUse(PlayerEntity user) {

    }
}
