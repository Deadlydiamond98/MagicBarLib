package net.deadlydiamond98.koalalib.common.events;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.ToggleableContent;
import net.deadlydiamond98.koalalib.common.misc.KoalalibTags;
import net.deadlydiamond98.koalalib.config.configs.MainConfigs;
import net.deadlydiamond98.koalalib.util.LootTableHelper;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;

public class KoalaAfterDeathEvents {

    // Ender Soul Loot Table IDs
    public static final Identifier SOUL = new Identifier(KoalaLib.MOD_ID, "entities/endersoul_reg_mob");
    public static final Identifier SOUL_MINIBOSS = new Identifier(KoalaLib.MOD_ID, "entities/endersoul_reg_mob");
    private static final Identifier SOUL_DRAGON = new Identifier(KoalaLib.MOD_ID, "entities/endersoul_dragon");

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register(KoalaAfterDeathEvents::dropEnderSouls);
    }

    /**
     * Drops Ender Souls when an Ender-Type Mob dies!
     */
    private static void dropEnderSouls(LivingEntity entity, DamageSource damageSource) {
        if (ToggleableContent.areEnderSoulsEnabled()) {
            if (entity.getType().isIn(KoalalibTags.Entities.ENDER_MOB)) {
                LootTableHelper.addLootToMob(entity, damageSource, SOUL);
            }
            else if (entity.getType().isIn(KoalalibTags.Entities.ENDER_MINI_BOSS)) {
                LootTableHelper.addLootToMob(entity, damageSource, SOUL_MINIBOSS);
            }
            else if (entity instanceof EnderDragonEntity dragon && MainConfigs.enderDragonDrops) {
                LootTableHelper.addLootToMob(entity, damageSource, SOUL_DRAGON, stack -> dropEnderDragonSouls(dragon, stack));
            }
        }
    }

    /**
     * Creates the loot-table above the Dragon Egg with a Glowing Effect, so that they aren't easily missed!
     */
    private static void dropEnderDragonSouls(EnderDragonEntity dragon, ItemStack stack) {
        if (!stack.isEmpty()) {
            BlockPos pos = dragon.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING, EndPortalFeature.offsetOrigin(dragon.getFightOrigin()));
            ItemEntity itemEntity = new ItemEntity(dragon.getWorld(), pos.getX() + 0.5, pos.getY() + 1, pos.getZ()+ 0.5, stack, 0, 0, 0);
            itemEntity.setToDefaultPickupDelay();
            itemEntity.setGlowing(true);
            dragon.getWorld().spawnEntity(itemEntity);
        }
    }
}
