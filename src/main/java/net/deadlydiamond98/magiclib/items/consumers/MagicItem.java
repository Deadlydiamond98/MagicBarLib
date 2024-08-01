package net.deadlydiamond98.magiclib.items.consumers;

import net.deadlydiamond98.magiclib.items.MagicItemData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MagicItem extends Item implements MagicItemData {
    private final int manaCost;

    /**
     * @param manaCost, The cost of mana from the item's usage
     */
    public MagicItem(Settings settings, int manaCost) {
        super(settings);
        this.manaCost = manaCost;
    }

    /**
     * Run when item is uses mana
     */
    protected void doManaAction(PlayerEntity user) {
        user.removeMana(this.manaCost);
    }

    /**
     * displays the Mana cost of the item in a tooltip (think sword damage for example)
     */
    @Override
    public int getManaCost() {
        return this.manaCost;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.canRemoveMana(this.manaCost)) {
            doManaAction(user);
        }
        return super.use(world, user, hand);
    }
}
