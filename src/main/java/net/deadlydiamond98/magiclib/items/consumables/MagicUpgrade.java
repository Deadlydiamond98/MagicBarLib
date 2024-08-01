package net.deadlydiamond98.magiclib.items.consumables;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MagicUpgrade extends MagicReplenisher {
    private final int useUntilManaIs;
    private final int addedMana;
    private final boolean consumed;
    private final boolean replenishMana;
    private final int cooldown;

    /**
     * This Item is used to increase a player's Maximum Mana level if you want to add mana upgrades to the game
     * @see MagicDowngrade If you want to decrease the Max Mana with an item
     *
     * @param useUntilManaIs, The Max Mana Level a player needs to have to no longer be able to use the item
     * @param addedMana, The Amount of Mana that is added to the Mana Bar
     * @param consumed, Whether the item is consumed on use
     * @param replenishMana, if true, the amount of mana the mana upgrade will also give the player the added mana as filled mana
     * @param cooldown, Item use Cooldown, if any
     */
    public MagicUpgrade(Settings settings, int useUntilManaIs, int addedMana, boolean consumed, boolean replenishMana, int cooldown) {
        super(settings);
        this.useUntilManaIs = useUntilManaIs;
        this.addedMana = addedMana;
        this.consumed = consumed;
        this.replenishMana = replenishMana;
        this.cooldown = cooldown;
    }

    /**
     * This Method is called if more mana can be added to the player,
     * use this method if you want to do extra things once more mana is granted to the player
     */
    public void addMana(PlayerEntity user, Hand hand) {
        user.increaseMaxMana(this.addedMana, this.replenishMana);
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
            if (user.getMaxMana() < this.useUntilManaIs) {
                addMana(user, hand);
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}
