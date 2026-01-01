package net.deadlydiamond98.koalalib.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class LootTableHelper {

    /**
     * Allows you to add more loot to a mob, without overwriting their current Loot Table!
     * @param entity - the Entity that the Loot Table it applied to
     * @param damageSource - the Damage Source
     * @param lootTableID - the ID for the Loot Table
     */
    public static void addLootToMob(LivingEntity entity, DamageSource damageSource, Identifier lootTableID) {
        addLootToMob(entity, damageSource, lootTableID, entity::dropStack);
    }

    /**
     * Allows you to add more loot to a mob, without overwriting their current Loot Table!
     * @param entity - the Entity that the Loot Table it applied to
     * @param damageSource - the Damage Source
     * @param lootTableID - the ID for the Loot Table
     * @param customDropLootEvent - Consumer for the dropStack method, Used to pass in a custom event to do additional things when dropping items
     */
    public static void addLootToMob(LivingEntity entity, DamageSource damageSource, Identifier lootTableID, Consumer<ItemStack> customDropLootEvent) {
        if (!entity.getWorld().isClient) {
            LootTable lootTable = entity.getWorld().getServer().getReloadableRegistries().getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, lootTableID));
            LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)entity.getWorld()))
                    .add(LootContextParameters.THIS_ENTITY, entity).add(LootContextParameters.ORIGIN, entity.getPos())
                    .add(LootContextParameters.DAMAGE_SOURCE, damageSource)
                    .addOptional(LootContextParameters.THIS_ENTITY, damageSource.getAttacker())
                    .addOptional(LootContextParameters.DIRECT_ATTACKING_ENTITY, damageSource.getSource());

            LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
            lootTable.generateLoot(lootContextParameterSet, entity.getLootTableSeed(), customDropLootEvent);
        }
    }
}
