package net.deadlydiamond98.koalalib.mixin.entity.player;

import net.deadlydiamond98.koalalib.common.items.vanillamodified.CustomShieldItem;
import net.deadlydiamond98.koalalib.networking.packets.s2c.HasAdvancementS2CPacket;
import net.deadlydiamond98.koalalib.util.mixindata.player.IPlayerOtherMixinData;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerOtherMixinData {

    // Variables

    @Unique private boolean koalalib$hasAdvancement;
    @Unique private DamageSource koalalib$shieldSource;

    // Shadowed Methods

    @Shadow public abstract ItemCooldownManager getItemCooldownManager();

    // Shield Related Things

    @Inject(method = "damage", at = @At("HEAD"))
    private void koalalib$getSheildDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.koalalib$shieldSource = source;
    }

    @Inject(method = "damageShield", at = @At("HEAD"))
    private void koalalib$shieldDurability(float amount, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        CustomShieldItem.attemptDamageSheild(player, player.getActiveItem(), amount, this.koalalib$shieldSource);
    }

    @Inject(method = "disableShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;clearActiveItem()V"))
    private void koalalib$disableSheild(boolean sprinting, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        CustomShieldItem.disableShield(player, this.getItemCooldownManager());
    }

    // Advancement Checker Methods /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean koalalib$hasAdvancement(String advancementID) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.getWorld().isClient()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                MinecraftServer server = player.getServer();

                if (server != null) {
                    Advancement advancement = server.getAdvancementLoader().get(new Identifier(advancementID));

                    if (advancement != null) {
                        boolean bl = serverPlayer.getAdvancementTracker().getProgress(advancement).isDone();
                        HasAdvancementS2CPacket.send(serverPlayer, bl);
                        return bl;
                    }
                }
            }
        }
        return this.koalalib$hasAdvancement;
    }

    @Override
    public void koalalib$updateAdvancementClient(boolean hasAdvancement) {
        this.koalalib$hasAdvancement = hasAdvancement;
    }
}
