package net.deadlydiamond98.koalalib.mixin.player;

import net.deadlydiamond98.koalalib.common.items.vanillamodified.CustomShieldItem;
import net.deadlydiamond98.koalalib.events.PlayerAttackingCallback;
import net.deadlydiamond98.koalalib.networking.s2c.HasAdvancementS2CPacket;
import net.deadlydiamond98.koalalib.util.mixindata.player.IPlayerOtherMixinData;
import net.minecraft.advancement.Advancement;
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

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerOtherMixinData {

    @Unique private boolean koalalib$hasAdvancement;
    @Unique private boolean koalalib$isAttacking;

    @Shadow public abstract ItemCooldownManager getItemCooldownManager();

    // Shield Related Things

    @Inject(method = "tick", at = @At("HEAD"))
    private void koalalib$tick(CallbackInfo ci) {
        if (this.koalalib$isAttacking) {
            PlayerAttackingCallback.EVENT.invoker().interact((PlayerEntity) (Object) this);
            this.koalalib$isAttacking = false;
        }
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

    // Attack Checking Methods /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean koalalib$isAttacking() {
        return this.koalalib$isAttacking;
    }

    @Override
    public void koalalib$setAttacking(boolean attacking) {
        this.koalalib$isAttacking = attacking;
    }
}
