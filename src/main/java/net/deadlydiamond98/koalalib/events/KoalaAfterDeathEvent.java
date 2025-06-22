package net.deadlydiamond98.koalalib.events;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.ToggleableContent;
import net.deadlydiamond98.koalalib.common.misc.KoalalibTags;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class KoalaAfterDeathEvent {

    public static final Identifier ENDERSOUL_LOOT_TABLE_ID = new Identifier(KoalaLib.MOD_ID, "entities/endersoul");

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            dropEnderSouls(entity, damageSource);
        });
    }

    private static void dropEnderSouls(LivingEntity entity, DamageSource damageSource) {
        // If Ender Souls are enabled, this will allow them to drop using a loot table
        if (ToggleableContent.areEnderSoulsEnabled() && entity.getType().isIn(KoalalibTags.Entities.ENDER_MOB)) {

            if (!entity.getWorld().isClient) {

                LootTable lootTable = entity.getWorld().getServer().getLootManager().getLootTable(ENDERSOUL_LOOT_TABLE_ID);
                LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)entity.getWorld()))
                        .add(LootContextParameters.THIS_ENTITY, entity).add(LootContextParameters.ORIGIN, entity.getPos())
                        .add(LootContextParameters.DAMAGE_SOURCE, damageSource)
                        .addOptional(LootContextParameters.KILLER_ENTITY, damageSource.getAttacker())
                        .addOptional(LootContextParameters.DIRECT_KILLER_ENTITY, damageSource.getSource());

                LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
                lootTable.generateLoot(lootContextParameterSet, entity.getLootTableSeed(), entity::dropStack);
            }
        }
    }
}
