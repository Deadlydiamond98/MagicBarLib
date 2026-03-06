package net.deadlydiamond98.koalalib.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class PhysicsProjectile extends ProjectileEntity {

    private static final TrackedData<Float> DRAG = DataTracker.registerData(PhysicsProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> BOUNCINESS = DataTracker.registerData(PhysicsProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> GRAVITY = DataTracker.registerData(PhysicsProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> BUOYANCY = DataTracker.registerData(PhysicsProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> WATER_DRAG = DataTracker.registerData(PhysicsProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private double serverX, serverY, serverZ, serverYaw, serverPitch;
    private int steps;

    protected boolean canEnterPortals = true;
    protected int maxLife = 100;
    private int despawnTimer;

    public PhysicsProjectile(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isRemoved()) {
            tickSyncedMovement();
        }
        checkCollision();
        tickMovement();
        tickDespawn();
    }

    protected void tickSyncedMovement() {
        if (this.isLogicalSideForUpdatingMovement()) {
            this.steps = 0;
            this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
        }
        if (this.steps > 0) {
            double d = 1.0 / (double)steps;
            double e = MathHelper.lerp(d, this.getX(), this.serverX);
            double f = MathHelper.lerp(d, this.getY(), this.serverY);
            double g = MathHelper.lerp(d, this.getZ(), this.serverZ);
            float h = MathHelper.lerpAngleDegrees((float) d, this.getYaw(), (float) this.serverYaw);
            float i = (float)MathHelper.lerp(d, this.getPitch(), this.serverPitch);
            this.setPosition(e, f, g);
            this.setRotation(h, i);
            --this.steps;
        }

        Vec3d vec3d = this.getVelocity();
        double d = vec3d.x;
        double e = vec3d.y;
        double f = vec3d.z;
        if (Math.abs(vec3d.x) < 0.003) {
            d = 0.0;
        }

        if (Math.abs(vec3d.y) < 0.003) {
            e = 0.0;
        }

        if (Math.abs(vec3d.z) < 0.003) {
            f = 0.0;
        }

        this.setVelocity(d, e, f);
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.serverX = x;
        this.serverY = y;
        this.serverZ = z;
        this.serverYaw = yaw;
        this.serverPitch = pitch;
        this.steps = interpolationSteps;
    }

    protected void tickDespawn() {
        if (!this.getWorld().isClient) {
            if (this.getVelocity().lengthSquared() < 0.005) {
                if (this.despawnTimer++ >= getMaxDespawnTime()) {
                    this.despawnProjectile();
                }
            } else {
                this.despawnTimer = 0;
            }
        }
    }

    protected void despawnProjectile() {
        this.discard();
    }

    protected void tickMovement() {
        this.velocityDirty = true;

        this.moveWithBounce(MovementType.SELF, this.getVelocity());

        if (this.isTouchingWater()) {
            this.setVelocity(this.getVelocity().add(0.0, getBuoyancy(), 0.0));
            this.setVelocity(this.getVelocity().multiply(getFluidDrag()));
        } else {
            this.setVelocity(this.getVelocity().multiply(getDrag()));
        }

        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -getGravity(), 0.0));
        }
    }

    protected void hitWall(boolean hitXAxis, boolean hitZAxis) {
        if (hitXAxis) {
            this.setVelocity(this.getVelocity().multiply(-getBounciness(), 1, 1));
        }
        if (hitZAxis) {
            this.setVelocity(this.getVelocity().multiply(1, 1, -getBounciness()));
        }
    }

    protected void hitFloor(Block block) {
        float slipperiness = block.getSlipperiness();
        this.setVelocity(this.getVelocity().multiply(slipperiness, -getBounciness(), slipperiness));
    }

    protected void checkCollision() {
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        boolean collidedWithPortal = false;

        if (hitResult.getType() == HitResult.Type.BLOCK && this.canEnterPortals) {
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState blockState = this.getWorld().getBlockState(blockPos);
            if (blockState.isOf(Blocks.NETHER_PORTAL)) {
                this.setInNetherPortal(blockPos);
                collidedWithPortal = true;
            } else if (blockState.isOf(Blocks.END_GATEWAY)) {
                BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
                if (blockEntity instanceof EndGatewayBlockEntity && EndGatewayBlockEntity.canTeleport(this)) {
                    EndGatewayBlockEntity.tryTeleportingEntity(this.getWorld(), blockPos, blockState, this, (EndGatewayBlockEntity) blockEntity);
                }
                collidedWithPortal = true;
            }
        }

        if (hitResult.getType() != HitResult.Type.MISS && !collidedWithPortal) {
            this.onCollision(hitResult);
        }
    }

    protected int getMaxDespawnTime() {
        return this.maxLife;
    }

    protected void setMaxDespawnTime(int maxLife) {
        this.maxLife = maxLife;
    }

    public float getDrag() {
        return this.dataTracker.get(DRAG);
    }

    public void setDrag(float drag) {
        this.dataTracker.set(DRAG, drag);
    }

    public float getGravity() {
        return this.dataTracker.get(GRAVITY);
    }

    public void setGravity(float gravity) {
        this.dataTracker.set(GRAVITY, gravity);
    }

    public float getBounciness() {
        return this.dataTracker.get(BOUNCINESS);
    }

    public void setBounciness(float bounce) {
        this.dataTracker.set(BOUNCINESS, bounce);
    }

    public float getBuoyancy() {
        return this.dataTracker.get(BUOYANCY);
    }

    public void setBuoyancy(float buoyancy) {
        this.dataTracker.set(BUOYANCY, buoyancy);
    }

    public float getFluidDrag() {
        return this.dataTracker.get(WATER_DRAG);
    }

    public void setWaterDrag(float drag) {
        this.dataTracker.set(WATER_DRAG, drag);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(DRAG, 0.98f);
        this.dataTracker.startTracking(WATER_DRAG, 0.75f);
        this.dataTracker.startTracking(GRAVITY, 0.03f);
        this.dataTracker.startTracking(BOUNCINESS, 0.75f);
        this.dataTracker.startTracking(BUOYANCY, 0.07f);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("PhysicsDrag", getDrag());
        nbt.putFloat("PhysicsWaterDrag", getFluidDrag());
        nbt.putFloat("PhysicsGravity", getGravity());
        nbt.putFloat("PhysicsBounce", getBounciness());
        nbt.putFloat("PhysicsBuoyancy", getBuoyancy());
        nbt.putInt("PhysicsDespawnTimer", this.despawnTimer);
        nbt.putInt("PhysicsMaxDespawnTimer", this.maxLife);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setDrag(nbt.getFloat("PhysicsDrag"));
        setWaterDrag(nbt.getFloat("PhysicsWaterDrag"));
        setGravity(nbt.getFloat("PhysicsGravity"));
        setBounciness(nbt.getFloat("PhysicsBounce"));
        setBuoyancy(nbt.getFloat("PhysicsBuoyancy"));
        this.despawnTimer = nbt.getInt("PhysicsDespawnTimer");
        this.maxLife = nbt.getInt("PhysicsMaxDespawnTimer");
    }

    public void moveWithBounce(MovementType movementType, Vec3d movement) {
        if (this.noClip) {
            this.setPosition(this.getX() + movement.x, this.getY() + movement.y, this.getZ() + movement.z);
        } else {
            this.wasOnFire = this.isOnFire();
            if (movementType == MovementType.PISTON) {
                movement = this.adjustMovementForPiston(movement);
                if (movement.equals(Vec3d.ZERO)) {
                    return;
                }
            }

            this.getWorld().getProfiler().push("move");
            if (this.movementMultiplier.lengthSquared() > 1.0E-7) {
                movement = movement.multiply(this.movementMultiplier);
                this.movementMultiplier = Vec3d.ZERO;
                this.setVelocity(Vec3d.ZERO);
            }

            movement = this.adjustMovementForSneaking(movement, movementType);
            Vec3d vec3d = this.adjustMovementForCollisionsCopied(movement);
            double d = vec3d.lengthSquared();
            if (d > 1.0E-7) {
                if (this.fallDistance != 0.0F && d >= 1.0) {
                    BlockHitResult blockHitResult = this.getWorld().raycast(new RaycastContext(this.getPos(), this.getPos().add(vec3d), RaycastContext.ShapeType.FALLDAMAGE_RESETTING, RaycastContext.FluidHandling.WATER, this));
                    if (blockHitResult.getType() != HitResult.Type.MISS) {
                        this.onLanding();
                    }
                }

                this.setPosition(this.getX() + vec3d.x, this.getY() + vec3d.y, this.getZ() + vec3d.z);
            }

            this.getWorld().getProfiler().pop();
            this.getWorld().getProfiler().push("rest");
            boolean bl = !MathHelper.approximatelyEquals(movement.x, vec3d.x);
            boolean bl2 = !MathHelper.approximatelyEquals(movement.z, vec3d.z);
            this.horizontalCollision = bl || bl2;
            this.verticalCollision = movement.y != vec3d.y;
            this.groundCollision = this.verticalCollision && movement.y < 0.0;
            if (this.horizontalCollision) {
                this.collidedSoftly = this.hasCollidedSoftly(vec3d);
            } else {
                this.collidedSoftly = false;
            }

            this.setOnGround(this.groundCollision, vec3d);
            BlockPos blockPos = this.getLandingPos();
            BlockState blockState = this.getWorld().getBlockState(blockPos);
            this.fall(vec3d.y, this.isOnGround(), blockState, blockPos);
            if (this.isRemoved()) {
                this.getWorld().getProfiler().pop();
            } else {
                if (this.horizontalCollision) {
                    hitWall(bl, bl2);
                }

                Block block = blockState.getBlock();
                if (movement.y != vec3d.y) {
                    hitFloor(block);
                }

                if (this.isOnGround()) {
                    block.onSteppedOn(this.getWorld(), blockPos, blockState, this);
                }

                MoveEffect moveEffect = this.getMoveEffect();
                if (moveEffect.hasAny() && !this.hasVehicle()) {
                    double e = vec3d.x;
                    double f = vec3d.y;
                    double g = vec3d.z;
                    this.speed += (float)(vec3d.length() * 0.6);

                    this.horizontalSpeed += (float)vec3d.horizontalLength() * 0.6F;
                    this.distanceTraveled += (float)Math.sqrt(e * e + f * f + g * g) * 0.6F;
                }

                this.tryCheckBlockCollision();
                float h = this.getVelocityMultiplier();
                this.setVelocity(this.getVelocity().multiply(h, 1.0, h));
                if (this.getWorld().getStatesInBoxIfLoaded(this.getBoundingBox().contract(1.0E-6)).noneMatch((state) -> {
                    return state.isIn(BlockTags.FIRE) || state.isOf(Blocks.LAVA);
                })) {
                    if (this.getFireTicks() <= 0) {
                        this.setFireTicks(-this.getBurningDuration());
                    }

                    if (this.wasOnFire && (this.inPowderSnow || this.isWet())) {
                        this.playExtinguishSound();
                    }
                }

                if (this.isOnFire() && (this.inPowderSnow || this.isWet())) {
                    this.setFireTicks(-this.getBurningDuration());
                }

                this.getWorld().getProfiler().pop();
            }
        }
    }

    protected Vec3d adjustMovementForCollisionsCopied(Vec3d movement) {
        Box box = this.getBoundingBox();
        List<VoxelShape> list = this.getWorld().getEntityCollisions(this, box.stretch(movement));
        Vec3d vec3d = movement.lengthSquared() == 0.0 ? movement : adjustMovementForCollisions(this, movement, box, this.getWorld(), list);
        boolean bl = movement.x != vec3d.x;
        boolean bl2 = movement.y != vec3d.y;
        boolean bl3 = movement.z != vec3d.z;
        boolean bl4 = this.isOnGround() || bl2 && movement.y < 0.0;
        if (this.getStepHeight() > 0.0F && bl4 && (bl || bl3)) {
            Vec3d vec3d2 = adjustMovementForCollisions(this, new Vec3d(movement.x, (double)this.getStepHeight(), movement.z), box, this.getWorld(), list);
            Vec3d vec3d3 = adjustMovementForCollisions(this, new Vec3d(0.0, (double)this.getStepHeight(), 0.0), box.stretch(movement.x, 0.0, movement.z), this.getWorld(), list);
            if (vec3d3.y < (double)this.getStepHeight()) {
                Vec3d vec3d4 = adjustMovementForCollisions(this, new Vec3d(movement.x, 0.0, movement.z), box.offset(vec3d3), this.getWorld(), list).add(vec3d3);
                if (vec3d4.horizontalLengthSquared() > vec3d2.horizontalLengthSquared()) {
                    vec3d2 = vec3d4;
                }
            }

            if (vec3d2.horizontalLengthSquared() > vec3d.horizontalLengthSquared()) {
                return vec3d2.add(adjustMovementForCollisions(this, new Vec3d(0.0, -vec3d2.y + movement.y, 0.0), box.offset(vec3d2), this.getWorld(), list));
            }
        }

        return vec3d;
    }
}