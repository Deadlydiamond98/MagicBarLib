package net.deadlydiamond98.koalalib.common.items.vanillamodified.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class CustomArrowItem extends ArrowItem implements ICustomProjectile {
    private final EntityType<?> type;

    public CustomArrowItem(Settings settings, EntityType<?> type) {
        super(settings);
        this.type = type;
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        Entity entity = getProjectile((ServerWorld) world, shooter.getBlockPos(), stack, shooter, false);
        if (entity instanceof PersistentProjectileEntity projectile) {
            return projectile;
        }
        throw new RuntimeException("An Entity was created, but the Entity was not a PersistentProjectileEntity!!!");
    }

    @Override
    public void initProjectile(Entity entity, ItemStack stack, LivingEntity owner) {
        ICustomProjectile.super.initProjectile(entity, stack, owner);
        entity.setPosition(entity.getPos().subtract(0, 0.10000000149011612, 0));
    }

    @Override
    public EntityType<?> getEntityType() {
        return this.type;
    }
}
