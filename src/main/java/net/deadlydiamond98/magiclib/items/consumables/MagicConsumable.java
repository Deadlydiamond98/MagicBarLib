package net.deadlydiamond98.magiclib.items.consumables;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
     * @see net.deadlydiamond98.magiclib.items.consumables.MagicFood
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
            user.addMana(this.amountToGive);
            if (consumed) {
                user.getStackInHand(hand).decrement(1);
            }
            if (this.cooldown > 0) {
                user.getItemCooldownManager().set(this, this.cooldown);
            }
        }
        return super.use(world, user, hand);
    }
}
