package net.deadlydiamond98.koalalib.common.items.magic.consumables;

import net.deadlydiamond98.koalalib.common.items.magic.IShowsMagicBar;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Deprecated(forRemoval = true)
public abstract class MaxMagicModifierItem extends Item implements IShowsMagicBar {
    protected final int magicIncrement;
    protected final boolean replenish;
    protected final int useUntilManaIs;
    protected final int cooldown;

    public MaxMagicModifierItem(Settings settings, int magicIncrement, boolean replenish, int useUntilManaIs, int cooldown) {
        super(settings);
        this.magicIncrement = magicIncrement;
        this.replenish = replenish;
        this.useUntilManaIs = useUntilManaIs;
        this.cooldown = cooldown;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (canUse(world, user, hand)) {
                updateMaxMagic(world, user, hand);
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }

    protected abstract boolean canUse(World world, PlayerEntity user, Hand hand);

    protected void updateMaxMagic(World world, PlayerEntity user, Hand hand) {
//        MagicBarHelper.increaseMaxMana(user, this.magicIncrement, this.replenish);
//
//        if (this.cooldown > 0) {
//            user.getItemCooldownManager().set(this, this.cooldown);
//        }
//
//        if (!user.isCreative()) {
//            user.getStackInHand(hand).decrement(1);
//        }
    }
}
