package net.deadlydiamond98.koalalib.mixin.player;

import net.deadlydiamond98.koalalib.networking.s2c.CustomHudRendererS2CPacket;
import net.deadlydiamond98.koalalib.util.mixinterfaces.player.ICustomHudBarTextureMixinData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class PlayerEntityCustomHudMixin implements ICustomHudBarTextureMixinData {

    @Unique @Nullable private Identifier koalalib$heartTexture, koalalib$shankTexture;
    @Unique private boolean koalalib$canBlink, koalalib$hasHeartOutline, koalalib$hasShankOutline;

    // Hearts //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void koalalib$applyCustomHeartTexture(Identifier texture, boolean hasOutline, boolean canBlink) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        this.koalalib$heartTexture = texture;
        this.koalalib$hasHeartOutline = hasOutline;
        this.koalalib$canBlink = canBlink;

        if (player instanceof ServerPlayerEntity sender) {
            sendUpdatePacket(sender, texture, hasOutline, canBlink, true);
        }
    }

    @Override @Nullable
    public Identifier koalalib$getCustomHeartTexture() {
        return this.koalalib$heartTexture;
    }

    @Override
    public boolean koalalib$hasCustomHearts() {
        return koalalib$heartTexture != null;
    }

    @Override
    public boolean koalalib$hasHeartOutline() {
        return this.koalalib$hasHeartOutline;
    }

    @Override
    public boolean koalalib$canHeartBlink() {
        return this.koalalib$canBlink;
    }

    // Shanks //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void koalalib$applyCustomShankTexture(Identifier texture, boolean hasOutline) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        this.koalalib$shankTexture = texture;
        this.koalalib$hasShankOutline = hasOutline;

        if (player instanceof ServerPlayerEntity sender) {
            sendUpdatePacket(sender, texture, hasOutline, false, false);
        }
    }

    @Override @Nullable
    public Identifier koalalib$getCustomShankTexture() {
        return this.koalalib$shankTexture;
    }

    @Override
    public boolean koalalib$hasCustomShanks() {
        return koalalib$shankTexture != null;
    }

    @Override
    public boolean koalalib$hasShankOutline() {
        return this.koalalib$hasShankOutline;
    }

    @Unique
    private void sendUpdatePacket(ServerPlayerEntity player, @Nullable Identifier texture, boolean hasOutline, boolean canBlink, boolean isHeart) {
        CustomHudRendererS2CPacket.send(player, texture, hasOutline, canBlink, isHeart);
    }
}
