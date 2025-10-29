package net.deadlydiamond98.koalalib.common.items.vanillamodified.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface ICustomProjectile {
    EntityType<?> getEntityType();

    default void initProjectile(Entity entity, ItemStack stack, LivingEntity owner, @Nullable Hand hand) {
        entity.setPosition(owner.getEyePos());
        if (entity instanceof ProjectileEntity projectile) {
            projectile.setOwner(owner);
        }
    }

    default boolean createProjectile(ServerWorld server, BlockPos blockPos, ItemStack stack, LivingEntity player, @Nullable Hand hand) {
        return getProjectile(server, blockPos, stack, player, hand, true) != null;
    }

    @Nullable
    default Entity getProjectile(ServerWorld server, BlockPos blockPos, ItemStack stack, LivingEntity owner, @Nullable Hand hand, boolean shouldSpawn) {
        Entity projectile = getEntity(server, blockPos);
        if (projectile != null) {
            initProjectile(projectile, stack, owner, hand);
            if (shouldSpawn) {
                server.spawnEntityAndPassengers(projectile);
            }
            return projectile;
        }
        return null;
    }

    @Nullable
    default Entity getEntity(ServerWorld server, BlockPos blockPos) {
        return getEntityType().create(server, null, null, blockPos, SpawnReason.DISPENSER, false, false);
    }
}
