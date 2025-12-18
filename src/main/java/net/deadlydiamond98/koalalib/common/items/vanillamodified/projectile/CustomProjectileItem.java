package net.deadlydiamond98.koalalib.common.items.vanillamodified.projectile;

import net.deadlydiamond98.koalalib.init.KoalaLibSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CustomProjectileItem extends Item implements ICustomProjectile {
    private final EntityType<?> type;

    public CustomProjectileItem(Settings settings, EntityType<?> type) {
        super(settings);
        this.type = type;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world instanceof ServerWorld server) {
            if (createProjectile(server, user.getBlockPos(), stack, user, hand)) {
                onProjectileThrown(world, user, stack);
            }
        }
        return TypedActionResult.success(stack);
    }

    protected void onProjectileThrown(World world, PlayerEntity user, ItemStack stack) {
        playThrowSound(user);
        if (stack.getItem() instanceof CustomProjectileItem) {
            stack.decrement(1);
        }
    }


    @Override
    public void initProjectile(Entity entity, ItemStack stack, LivingEntity owner, @Nullable Hand hand) {
        ICustomProjectile.super.initProjectile(entity, stack, owner, hand);
        entity.setYaw(owner.getYaw());
        entity.setVelocity(owner.getRotationVector());
    }

    @Override
    public EntityType<?> getEntityType() {
        return this.type;
    }

    // SFX /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected SoundEvent getThrowSound() {
        return KoalaLibSounds.ITEM_THROW;
    }

    public final void playThrowSound(PlayerEntity player) {
        playSound(player, getThrowSound(), 0.5f, 0.4f / (player.getWorld().getRandom().nextFloat() * 0.4f + 0.8f));
    }

    protected final void playSound(PlayerEntity player, SoundEvent sound, float volume, float pitch) {
        player.getWorld().playSound(null, player.getBlockPos(), sound, SoundCategory.PLAYERS, volume, pitch);
    }
}
