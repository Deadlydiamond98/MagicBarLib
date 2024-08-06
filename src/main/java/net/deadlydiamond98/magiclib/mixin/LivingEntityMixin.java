package net.deadlydiamond98.magiclib.mixin;

import net.deadlydiamond98.magiclib.items.MagicItemData;
import net.deadlydiamond98.magiclib.items.consumables.MagicReplenisher;
import net.deadlydiamond98.magiclib.networking.ZeldaServerPackets;
import net.deadlydiamond98.magiclib.util.ManaEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ManaEntityData {
    @Unique
    private int manaLevelZelda;
    @Unique
    private int manaMaxLevelZelda;
    @Unique
    private boolean regen;
    @Unique
    private int tickPause;
    @Unique
    private int amountToRegen;
    @Unique
    private int whenNeededRenderTime;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.manaMaxLevelZelda = 100;
        this.manaLevelZelda = 0;
        this.regen = false;
        this.tickPause = 40;
        this.amountToRegen = 1;
        this.whenNeededRenderTime = 0;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (!getThisEntity().getWorld().isClient()) {
            if (getThisEntity() instanceof PlayerEntity player) {
                Item currentHandItem = player.getStackInHand(player.getActiveHand()).getItem();
                if (currentHandItem instanceof MagicItemData || currentHandItem instanceof MagicReplenisher) {
                    this.setWhenNeededRenderTime(100);
                }
                ZeldaServerPackets.sendPlayerStatsPacket((ServerPlayerEntity) player, this.manaLevelZelda, this.manaMaxLevelZelda, this.whenNeededRenderTime);
            }

            if (this.hasManaRegen() && getThisEntity().age % this.tickPause == 0) {
                this.addMana(this.amountToRegen);
            }

            if (this.whenNeededRenderTime > 0) {
                this.whenNeededRenderTime--;
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void onSave(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("manaLevelZelda", manaLevelZelda);
        nbt.putInt("manaMaxLevelZelda", manaMaxLevelZelda);
        nbt.putBoolean("regenZelda", regen);
        nbt.putInt("tickPause", tickPause);
        nbt.putInt("amountToRegen", amountToRegen);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void onLoad(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("manaLevelZelda")) {
            this.manaLevelZelda = nbt.getInt("manaLevelZelda");
        }
        if (nbt.contains("manaMaxLevelZelda")) {
            this.manaMaxLevelZelda = nbt.getInt("manaMaxLevelZelda");
        }
        if (nbt.contains("regenZelda")) {
            this.regen = nbt.getBoolean("regenZelda");
        }
        if (nbt.contains("tickPause")) {
            this.tickPause = nbt.getInt("tickPause");
        }
        if (nbt.contains("amountToRegen")) {
            this.amountToRegen = nbt.getInt("amountToRegen");
        }
    }

    @Unique
    private LivingEntity getThisEntity() {
        return ((LivingEntity)(Object)this);
    }

    @Override
    public void setMana(int value) {
        if (getThisEntity() instanceof PlayerEntity) {
            this.setWhenNeededRenderTime(100);
        }
        this.manaLevelZelda = value;
    }
    @Override
    public int getMana() {
        return this.manaLevelZelda;
    }
    @Override
    public void setMaxMana(int value) {
        if (getThisEntity() instanceof PlayerEntity) {
            this.setWhenNeededRenderTime(100);
        }
        this.manaMaxLevelZelda = value;
    }
    @Override
    public int getMaxMana() {
        return this.manaMaxLevelZelda;
    }
    @Override
    public void addMana(int amount) {
        if (amount < 0) {
            removeMana(amount * -1);
        }
        else {
            if (canAddMana(amount)) {
                if (amount + this.getMana() <= this.getMaxMana()) {
                    this.setMana(this.getMana() + amount);
                }
                else if (this.getMana() < this.getMaxMana() && amount + this.getMana() > this.getMaxMana()) {
                    this.setMana(this.getMaxMana());
                }
            }
        }
    }
    @Override
    public void removeMana(int amount) {
        if (amount < 0) {
            addMana(amount * -1);
        }
        if (canRemoveMana(amount)) {
            if (this.getMana() - amount >= 0) {
                this.setMana(this.getMana() - amount);
            }
            else if (this.getMana() > 0 && this.getMana() - amount < 0) {
                this.setMana(0);
            }
        }
    }
    @Override
    public boolean canAddMana(int amountToGive) {
        if (amountToGive + this.getMana() <= this.getMaxMana()) {
            return true;
        }
        else return this.getMana() < this.getMaxMana() && amountToGive + this.getMana() > this.getMaxMana();
    }

    @Override
    public boolean canRemoveMana(int amountToRemove) {
        if (this.getMana() - amountToRemove >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void increaseMaxMana(int amount, boolean replenish) {
        if (amount < 0) {
            decreaseMaxMana(amount * -1);
        }
        else {
            this.setMaxMana(this.getMaxMana() + amount);
            if (replenish) {
                this.addMana(amount);
            }
        }
    }

    @Override
    public void decreaseMaxMana(int amount) {
        if (amount < 0) {
            increaseMaxMana(amount * -1, false);
        } else {
            if (this.canDecreaseMaxMana(amount)) {
                this.setMaxMana(this.getMaxMana() - amount);
                if (this.getMaxMana() < this.getMana()) {
                    this.setMana(this.getMaxMana());
                }
            }
        }
    }
    @Override
    public boolean canDecreaseMaxMana(int amount) {
        return this.getMaxMana() - amount > 0;
    }

    @Override
    public boolean hasManaRegen() {
        return this.regen;
    }

    @Override
    public void enableManaRegen(boolean regen, int tickPause, int amount) {
        this.tickPause = tickPause;
        this.amountToRegen = amount;
        this.regen = regen;
    }

    @Override
    public int getWhenNeededRenderTime() {
        if (getThisEntity() instanceof PlayerEntity) {
            return this.whenNeededRenderTime;
        }
        return 0;
    }

    @Override
    public void setWhenNeededRenderTime(int whenNeededRenderTime) {
        if (getThisEntity() instanceof PlayerEntity) {
            this.whenNeededRenderTime = whenNeededRenderTime;
        }
    }
}