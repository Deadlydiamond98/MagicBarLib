package net.deadlydiamond98.koalalib.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public abstract class PhysicsItemProjectile extends PhysicsProjectile implements FlyingItemEntity {

    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(PhysicsItemProjectile.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public PhysicsItemProjectile(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setItem(ItemStack item) {
        if (!item.isOf(this.getDefaultItem()) || item.hasNbt()) {
            this.getDataTracker().set(ITEM, item.copyWithCount(1));
        }
    }

    protected abstract Item getDefaultItem();

    protected ItemStack getItem() {
        return this.getDataTracker().get(ITEM);
    }

    public ItemStack getStack() {
        ItemStack itemStack = this.getItem();
        return itemStack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemStack;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ITEM, ItemStack.EMPTY);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        ItemStack itemStack = this.getItem();
        if (!itemStack.isEmpty()) {
            nbt.put("Item", itemStack.writeNbt(new NbtCompound()));
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("Item"));
        this.setItem(itemStack);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = new ItemStackParticleEffect(ParticleTypes.ITEM, this.getDefaultItem().getDefaultStack());

            for(int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(),
                        0.0 + random.nextBetween(-5, 5) * 0.02,
                        0.1,
                        0.0 + random.nextBetween(-5, 5) * 0.02);
            }
        }
    }

    @Override
    protected void despawnProjectile() {
        this.getWorld().sendEntityStatus(this, (byte)3);
        super.despawnProjectile();
    }

}
