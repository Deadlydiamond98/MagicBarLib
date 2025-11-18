package net.deadlydiamond98.koalalib.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class LerpedMovmentEntity extends Entity {
    private double serverX, serverY, serverZ, serverYaw, serverPitch;
    private int steps;

    public LerpedMovmentEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isRemoved()) {
            tickSyncedMovement();
        }
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
}
