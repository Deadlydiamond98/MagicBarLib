package net.deadlydiamond98.koalalib.mixin.player;

import net.deadlydiamond98.koalalib.common.items.vanillamodified.CustomShieldItem;
import net.deadlydiamond98.koalalib.events.PlayerAttackingCallback;
import net.deadlydiamond98.koalalib.networking.s2c.HasAdvancementS2CPacket;
import net.deadlydiamond98.koalalib.util.PseudoRandom;
import net.deadlydiamond98.koalalib.util.mixinterfaces.player.IPlayerOtherMixinData;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerOtherMixinData {

    /*
        Handled Here:

        - Pseudorandom Numbers
        - Advancement Check for breaking some blocks
        - Player Attacking Action
        - Custom Shield Disabling

     */

    @Unique private final HashMap<String, PseudoRandom> koalalib$pseudoRandoms = new HashMap<>();
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
    private void koalalib$disableSheild(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        CustomShieldItem.disableShield(player, this.getItemCooldownManager());
    }

    // READ & WRITE NBT ////////////////////////////////////////////////////////////////////////////////////////////////

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void zeldacraft$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        // Pseudo Randoms
        NbtCompound pseudoRandoms = new NbtCompound();
        for (Map.Entry<String, PseudoRandom> entry : this.koalalib$pseudoRandoms.entrySet()) {
            pseudoRandoms.put(entry.getKey(), entry.getValue().toNbt());
        }
        nbt.put("PseudoRandoms", pseudoRandoms);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void zeldacraft$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        // Pseudo Randoms
        if (nbt.contains("PseudoRandoms")) {
            NbtCompound pseudoRandoms = nbt.getCompound("PseudoRandoms");
            for (String key : pseudoRandoms.getKeys()) {
                this.koalalib$pseudoRandoms.put(key, PseudoRandom.fromNbt(
                        ((PlayerEntity) (Object) this).getRandom(), (NbtCompound) pseudoRandoms.get(key))
                );
            }
        }
    }

    // Advancement Checker Methods /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean koalalib$hasAdvancement(String advancementID) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.getWorld().isClient()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                MinecraftServer server = player.getServer();

                if (server != null) {
                    AdvancementEntry advancement = server.getAdvancementLoader().get(Identifier.of(advancementID));

                    if (advancement != null) {
                        boolean bl = serverPlayer.getAdvancementTracker().getProgress(advancement).isDone();
                        HasAdvancementS2CPacket.Sender.send(serverPlayer, bl);
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

    // Get PseudoRandom ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean koalalib$getPseudoRandom(String id, float chance, int max) {
        this.koalalib$pseudoRandoms.putIfAbsent(id, new PseudoRandom(((PlayerEntity) (Object) this).getRandom(), chance, max));
        PseudoRandom pseudoRandom = this.koalalib$pseudoRandoms.get(id);
        pseudoRandom.updateArgs(chance, max);
        return pseudoRandom.isSuccessful();
    }
}
