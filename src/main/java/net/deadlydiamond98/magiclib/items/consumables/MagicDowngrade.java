package net.deadlydiamond98.magiclib.items.consumables;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MagicDowngrade extends MagicReplenisher {
    private final int useUntilManaIs;
    private final int removedMana;
    private final boolean consumed;
    private final int cooldown;


    /**
     * This Item is used to decrease a player's Maximum Mana level if you want to add mana upgrades to the game
     * @see MagicConsumable If you want to increase the Max Mana with an item
     *
     * @param useUntilManaIs, The Minimum Mana Level a player needs to have to no longer be able to use the item
     * @param removedMana, The Amount of Mana that is removed from the Mana Bar
     * @param consumed, Whether the item is consumed on use
     * @param cooldown, Item use Cooldown, if any
     */
    public MagicDowngrade(Settings settings, int useUntilManaIs, int removedMana, boolean consumed, int cooldown) {
        super(settings);
        this.useUntilManaIs = useUntilManaIs;
        this.removedMana = removedMana;
        this.consumed = consumed;
        this.cooldown = cooldown;
    }

    /**
     * This Method is called if more mana can be removed from the player,
     * use this method if you want to do extra things once more mana is removed from the player
     */
    public void removeMana(PlayerEntity user, Hand hand) {
        user.decreaseMaxMana(this.removedMana);
        if (consumed) {
            user.getStackInHand(hand).decrement(1);
        }
        if (this.cooldown > 0) {
            user.getItemCooldownManager().set(this, this.cooldown);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (user.getMaxMana() > this.useUntilManaIs) {
                removeMana(user, hand);
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}
