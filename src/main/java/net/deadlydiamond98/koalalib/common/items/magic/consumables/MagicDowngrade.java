package net.deadlydiamond98.koalalib.common.items.magic.consumables;

import net.deadlydiamond98.koalalib.common.items.magic.IShowsMagicBar;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MagicDowngrade extends MaxMagicModifierItem {
    public MagicDowngrade(Settings settings, int magicIncrement, boolean replenish, int useUntilManaIs, int cooldown) {
        super(settings, -magicIncrement, replenish, useUntilManaIs, cooldown);
    }

    @Override
    protected boolean canUse(World world, PlayerEntity user, Hand hand) {
        return MagicBarHelper.getMaxMana(user) > this.useUntilManaIs;
    }
}
