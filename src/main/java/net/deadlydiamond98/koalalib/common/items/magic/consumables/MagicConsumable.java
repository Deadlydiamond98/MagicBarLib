package net.deadlydiamond98.koalalib.common.items.magic.consumables;

import net.deadlydiamond98.koalalib.common.items.magic.IShowsMagicBar;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class MagicConsumable extends Item implements IShowsMagicBar {
    private final int magic;
    private final int cooldown;

    public MagicConsumable(Settings settings, int magic) {
        this(settings, magic, 0);
    }

    public MagicConsumable(Settings settings, int magic, int cooldown) {
        super(settings);
        this.magic = magic;
        this.cooldown = cooldown;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack consumable = user.getStackInHand(hand);

        FoodComponent foodComponent = consumable.get(DataComponentTypes.FOOD);

        if (foodComponent != null) {
            return eatItem(consumable, world, user, hand, foodComponent);
        } else {
            return consumeItem(consumable, world, user, hand);
        }
    }

    /**
     * Handles consuming the item if a food component isn't attached
     */
    protected TypedActionResult<ItemStack> consumeItem(ItemStack consumable, World world, PlayerEntity user, Hand hand) {
        if (canUse(consumable, world, user, hand)) {
            if (this.cooldown > 0) {
                user.getItemCooldownManager().set(this, this.cooldown);
            }
            if (!user.isCreative()) {
                consumable.decrement(1);
            }
            finishUsing(consumable, world, user);
            return TypedActionResult.success(consumable);
        }
        return TypedActionResult.fail(consumable);
    }

    /**
     * Handles eating the item if a food component is attached to the item
     */
    protected TypedActionResult<ItemStack> eatItem(ItemStack consumable, World world, PlayerEntity user, Hand hand, FoodComponent foodComponent) {
        if (user.canConsume(foodComponent.canAlwaysEat()) || canUse(consumable, world, user, hand)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(consumable);
        } else {
            return TypedActionResult.fail(consumable);
        }
    }

    /**
     * If true, the player can consume the item
     */
    protected boolean canUse(ItemStack consumable, World world, PlayerEntity user, Hand hand) {
        return MagicBarHelper.canAddMana(user, this.magic);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        MagicBarHelper.addMana(user, this.magic);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }
}
