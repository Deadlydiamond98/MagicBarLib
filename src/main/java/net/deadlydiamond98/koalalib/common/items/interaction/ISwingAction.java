package net.deadlydiamond98.koalalib.common.items.interaction;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Any item that implements this interface will run the attack method whenever you punch using the item!
 */
public interface ISwingAction {
    void attack(World world, PlayerEntity player);
}
