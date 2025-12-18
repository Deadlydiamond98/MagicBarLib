package net.deadlydiamond98.koalalib.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.deadlydiamond98.koalalib.common.effect.IHudIconEffect;
import net.deadlydiamond98.koalalib.util.mixinterfaces.player.ICustomHudBarTextureMixinData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 3))
    private void koalalib$renderShankOutline(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (!koalalib$renderIcon(instance, x, y, false, false, false, false, true)) {
            original.call(instance, texture, x, y, u, v, width, height);
        }
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 4))
    private void koalalib$renderShank(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (!koalalib$renderIcon(instance, x, y, false, false, false, false, false)) {
            original.call(instance, texture, x, y, u, v, width, height);
        }
    }

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 5))
    private void koalalib$renderHalfShank(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (!koalalib$renderIcon(instance, x, y, false, true, false, false, false)) {
            original.call(instance, texture, x, y, u, v, width, height);
        }
    }

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void koalalib$drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        boolean hardcore = this.getCameraPlayer().getWorld().getLevelProperties().isHardcore();
        if (koalalib$renderIcon(context, x, y, true, halfHeart, blinking, hardcore, type == InGameHud.HeartType.CONTAINER)) {
            ci.cancel();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unique
    private boolean koalalib$renderIcon(DrawContext context, int x, int y, boolean isHeart, boolean half, boolean blink, boolean hardcore, boolean isOutline) {
        if (this.getCameraPlayer() instanceof ICustomHudBarTextureMixinData hudInfo) {
            if (hudInfo.koalalib$hasCustomHearts() && isHeart) {
                if (isOutline) {
                    if (hudInfo.koalalib$hasHeartOutline()) {
                        koalalib$renderHudOutline(context, hudInfo.koalalib$getCustomHeartTexture(), x, y, 0);
                        return true;
                    }
                    return false;
                } else {
                    koalalib$renderHudPart(context, hudInfo.koalalib$getCustomHeartTexture(), x, y, 0, half, blink && hudInfo.koalalib$canHeartBlink(), hardcore);
                    return true;
                }
            } else if (hudInfo.koalalib$hasCustomShanks() && !isHeart) {
                if (isOutline) {
                    if (hudInfo.koalalib$hasShankOutline()) {
                        koalalib$renderHudOutline(context, hudInfo.koalalib$getCustomShankTexture(), x, y, 2);
                        return true;
                    }
                    return false;
                } else {
                    koalalib$renderHudPart(context, hudInfo.koalalib$getCustomShankTexture(), x, y, 2, half, false, hardcore);
                    return true;
                }
            }
        }

        for (StatusEffectInstance instance : this.getCameraPlayer().getStatusEffects()) {
            if (instance.getEffectType() instanceof IHudIconEffect effect) {
                if (effect.getIconType().canRender(isHeart)) {
                    if (isOutline) {
                        if ((effect.customHeartOutline() && isHeart && !blink) || (effect.customShankOutline() && !isHeart)) {
                            koalalib$renderHudOutline(context, effect.getIconsTexture(this.getCameraPlayer()), x, y, (isHeart ? 0 : 2));
                            return true;
                        }
                        return false;
                    } else {
                        koalalib$renderHudPart(context, effect.getIconsTexture(this.getCameraPlayer()), x, y, (isHeart ? 0 : 2), half, blink && effect.canHeartBlink(), hardcore);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Unique
    private void koalalib$renderHudOutline(DrawContext context, Identifier texture, int x, int y, int v) {
        koalalib$renderHudPart(context, texture, x, y, 4, v);
    }

    @Unique
    private void koalalib$renderHudPart(DrawContext context, Identifier texture, int x, int y, int v, boolean half, boolean blinking, boolean hardcore) {
        koalalib$renderHudPart(context, texture, x, y, (half ? 1 : 0) + (blinking ? 2 : 0), v + (hardcore ? 1 : 0));
    }

    @Unique
    private void koalalib$renderHudPart(DrawContext context, Identifier texture, int x, int y, int u, int v) {
        context.drawTexture(texture, x, y, u * 9, v * 9, 9, 9, 64, 64);
    }
}
