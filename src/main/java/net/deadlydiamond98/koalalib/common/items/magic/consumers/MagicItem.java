package net.deadlydiamond98.koalalib.common.items.magic.consumers;

import net.deadlydiamond98.koalalib.common.items.magic.IMagicItem;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MagicItem extends Item implements IMagicItem {
    private final int manaCost;

    /**
     * @param manaCost, The cost of mana from the item's usage
     */
    public MagicItem(Settings settings, int manaCost) {
        super(settings);
        this.manaCost = manaCost;
    }

    /**
     * Run when item is using mana
     */
    protected void doManaAction(PlayerEntity user, World world) {}

    /**
     * Run when item can't use mana
     */
    protected void doNoManaEvent(PlayerEntity user, World world) {}

    @Override
    public int getManaCost(ItemStack stack) {
        return this.manaCost;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (MagicBarHelper.removeMana(user, this.manaCost) || user.isCreative()) {
            doManaAction(user, world);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        else {
            doNoManaEvent(user, world);
        }
        return super.use(world, user, hand);
    }
}
