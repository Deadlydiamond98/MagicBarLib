package net.deadlydiamond98.koalalib.mixin.common.entity.living;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.deadlydiamond98.koalalib.ToggleableContent;
import net.deadlydiamond98.koalalib.common.items.magic.IShowsMagicBar;
import net.deadlydiamond98.koalalib.init.KoalaLibEntityAttributes;
import net.deadlydiamond98.koalalib.networking.s2c.EntityMagicUpdateS2CPacket;
import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.deadlydiamond98.koalalib.util.mixinterfaces.IMagicBarData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
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
public abstract class LivingEntityMagicMixin implements IMagicBarData {
    @Unique private int koalalib$manaLevel;

    @Unique private int koalalib$manaRegenDelay;
    @Unique private int koalalib$manaRegenCap = 100;
    @Unique private boolean koalalib$hasManaRegen = true;
    @Unique private boolean koalalib$requiresHunger = true;

    @Unique private int koalalib$manaBarRenderTime;

    @Inject(method = "tick", at = @At("HEAD"))
    public void koalalib$tick(CallbackInfo ci) {
        if (ToggleableContent.isMagicBarEnabled()) {
            LivingEntity entity = (LivingEntity) (Object) this;
            if (!entity.getWorld().isClient) {
                koalalib$regenManaBar(entity);
            } else {
                koalalib$updateManaBarClient(entity);
            }
        }
    }

    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder koalalib$createLivingAttributes(DefaultAttributeContainer.Builder original) {
        return original.add(KoalaLibEntityAttributes.GENERIC_MAX_MAGIC);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void koalalib$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("ManaLevelKoalaLib", this.koalalib$manaLevel);
        nbt.putInt("ManaRegenCapKoalaLib", this.koalalib$manaRegenCap);
        nbt.putBoolean("ManaRegenerationEnabledKoalaLib", this.koalalib$hasManaRegen);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void koalalib$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("ManaLevelKoalaLib")) {
            this.koalalib$manaLevel = nbt.getInt("ManaLevelKoalaLib");
        }
        if (nbt.contains("ManaRegenCapKoalaLib")) {
            this.koalalib$manaRegenCap = nbt.getInt("ManaRegenCapKoalaLib");
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
    private void koalalib$updateManaBarClient(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            Item currentHandItem = player.getMainHandStack().getItem();
            Item offHandItem = player.getOffHandStack().getItem();
            if (currentHandItem instanceof IShowsMagicBar || offHandItem instanceof IShowsMagicBar) {
                this.koalalib$setMagicBarRenderTime(100);
            }
            this.koalalib$setMagicBarRenderTime(Math.max(0, --this.koalalib$manaBarRenderTime));
        }
    }

    @Unique
    private void koalalib$regenManaBar(LivingEntity entity) {
        if (this.koalalib$manaRegenDelay++ < 0) {
            return;
        }

        if (koalalib$getMagicRegenCap() >= MagicBarHelper.getMaxMana(entity)) {
            return;
        }

        if (entity.age % 12 == 0 && koalalib$isManaRegenEnabled()) {
            if (this.koalalib$requiresHunger && entity instanceof PlayerEntity player && player.getHungerManager().isNotFull()) {
                return;
            }

            int maxMana = MagicBarHelper.getMaxMana(entity);

            int regenRate = (int) Math.max(Math.floor(Math.min(Math.floor(Math.pow((koalalib$getMana() / (double) maxMana) *
                    (maxMana / 100.0) * 0.8, -1)), 5)), 1);
            MagicBarHelper.addMana(entity, regenRate);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    //                                               Interface Methods                                                //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void koalalib$setMana(int value) {
        this.koalalib$manaLevel = value;
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.getWorld().isClient() && entity instanceof PlayerEntity player) {
            EntityMagicUpdateS2CPacket.Sender.send((ServerPlayerEntity) player, koalalib$manaLevel);
        }
    }

    @Override
    public int koalalib$getMana() {
        return this.koalalib$manaLevel;
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
    public int koalalib$getMagicRegenCap() {
        return this.koalalib$manaRegenCap;
    }

    @Override
    public void koalalib$setMagicRegenCap(int value) {
        this.koalalib$manaRegenCap = value;
    }

    @Override
    public void koalalib$requireFullHungerForMagicRegen(boolean bl) {
        this.koalalib$requiresHunger = bl;
    }

    @Override
    public void koalalib$applyRegenDelay(boolean value) {
        if (value) {
            this.koalalib$manaRegenDelay = (int) Math.floor(
                    Math.min(0.7 * ((1 - (koalalib$getMana() / (double) MagicBarHelper.getMaxMana((PlayerEntity) (Object) this)) * 500 + 45)), -60)
            );
        }
    }


    // RENDERING ///////////////////////////////////////////////////////////////////////////////////////////////////////

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
}