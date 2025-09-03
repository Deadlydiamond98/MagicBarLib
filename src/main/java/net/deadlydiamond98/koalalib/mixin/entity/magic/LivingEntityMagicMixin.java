package net.deadlydiamond98.koalalib.mixin.entity.magic;

import net.deadlydiamond98.koalalib.common.items.magic.IShowsMagicBar;
import net.deadlydiamond98.koalalib.networking.packets.s2c.EntityMagicStatsS2CPacket;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.deadlydiamond98.koalalib.util.mixindata.IMagicBarMixinData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMagicMixin implements IMagicBarMixinData {
    // Mana Level
    @Unique
    private int koalalib$manaLevel;
    @Unique
    private int koalalib$maxManaLevel = 100;

    // Mana Regeneration
    @Unique
    private int koalalib$manaRegenDelay;
    @Unique
    private boolean koalalib$hasManaRegen = true;

    // Visuals
    @Unique
    private int koalalib$manaBarRenderTime;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.getWorld().isClient) {
            koalalib$updateMagicBarClient(entity);
            koalalib$regenManaBar(entity);
            this.koalalib$setMagicBarRenderTime(Math.max(0, --this.koalalib$manaBarRenderTime));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void onSave(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("ManaLevelKoalaLib", this.koalalib$manaLevel);
        nbt.putInt("MaxManaLevelKoalaLib", this.koalalib$maxManaLevel);
        nbt.putBoolean("ManaRegenerationEnabledKoalaLib", this.koalalib$hasManaRegen);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void onLoad(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("ManaLevelKoalaLib")) {
            this.koalalib$manaLevel = nbt.getInt("ManaLevelKoalaLib");
        }
        if (nbt.contains("MaxManaLevelKoalaLib")) {
            this.koalalib$maxManaLevel = nbt.getInt("MaxManaLevelKoalaLib");
        }
        if (nbt.contains("ManaRegenerationEnabledKoalaLib")) {
            this.koalalib$hasManaRegen = nbt.getBoolean("ManaRegenerationEnabledKoalaLib");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    //                                                 Magic Methods                                                  //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unique
    private void koalalib$updateMagicBarClient(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            Item currentHandItem = player.getActiveItem().getItem();

            if (currentHandItem instanceof IShowsMagicBar) {
                koalalib$updateMagicBarVisibility();
            }

            EntityMagicStatsS2CPacket.send(
                    (ServerPlayerEntity) player, this.koalalib$manaLevel,
                    this.koalalib$maxManaLevel,
                    this.koalalib$manaBarRenderTime
            );
        }
    }

    @Unique
    private void koalalib$regenManaBar(LivingEntity entity) {
        if (this.koalalib$manaRegenDelay++ < 0) {
            return;
        }

        if (entity.age % 12 == 0 && koalalib$isManaRegenEnabled()) {
            if (entity instanceof PlayerEntity player && player.getHungerManager().isNotFull()) {
                return;
            }

            int regenRate = (int) Math.max(Math.floor(Math.min(Math.floor(Math.pow((koalalib$getMana() / (double) koalalib$getMaxMana()) *
                    (koalalib$getMaxMana() / 100.0) * 0.8, -1)), 5)), 1);
            MagicBarHelper.addMana(entity, regenRate);
        }
    }

    @Unique
    private void koalalib$updateMagicBarVisibility() {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity) {
            this.koalalib$setMagicBarRenderTime(100);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    //                                               Interface Methods                                                //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void koalalib$setMana(int value) {
        koalalib$updateMagicBarVisibility();
        this.koalalib$manaLevel = value;
    }

    @Override
    public int koalalib$getMana() {
        return this.koalalib$manaLevel;
    }

    @Override
    public void koalalib$setMaxMana(int value) {
        koalalib$updateMagicBarVisibility();
        this.koalalib$maxManaLevel = value;
    }

    @Override
    public int koalalib$getMaxMana() {
        return this.koalalib$maxManaLevel;
    }

    @Override
    public void koalalib$setManaRegenAbility(boolean value) {
        this.koalalib$hasManaRegen = value;
    }

    @Override
    public boolean koalalib$isManaRegenEnabled() {
        return this.koalalib$hasManaRegen;
    }

    @Override
    public void koalalib$setMagicBarRenderTime(int value) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity) {
            this.koalalib$manaBarRenderTime = value;
        }
    }

    @Override
    public int koalalib$getMagicBarRenderTime() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity instanceof PlayerEntity ? this.koalalib$manaBarRenderTime : 0;
    }

    @Override
    public void koalalib$applyRegenDelay(boolean value) {
        // Totally didn't borrow the regen formula from Terraria before modifying it, I would never
        if (value) {
            this.koalalib$manaRegenDelay = (int) Math.floor(
                    Math.min(0.7 * ((1 - (koalalib$getMana() / (double) koalalib$getMaxMana()) * 500 + 45)), -60)
            );
        }
    }
}