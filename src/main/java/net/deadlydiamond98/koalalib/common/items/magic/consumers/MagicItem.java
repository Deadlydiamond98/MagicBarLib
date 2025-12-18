package net.deadlydiamond98.koalalib.common.items.magic.consumers;

import net.deadlydiamond98.koalalib.common.items.magic.IMagicItem;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class MagicItem extends Item implements IMagicItem {
    private final int magicCost;

    public MagicItem(Settings settings, int magicCost) {
        super(settings);
        this.magicCost = magicCost;
    }

    public void onMagicActionSucceed(World world, PlayerEntity user, Hand hand) {}

    @Deprecated(forRemoval = true)
    protected void doManaAction(PlayerEntity user, World world) {}

    @Deprecated(forRemoval = true)
    protected void doNoManaEvent(PlayerEntity user, World world) {}

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hasEnoughMagic(user, user.getStackInHand(hand))) {
            onMagicActionSucceed(world, user, hand);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }

    @Override
    public int getBaseMagicCost(PlayerEntity player, ItemStack stack) {
        return this.magicCost;
    }
}
