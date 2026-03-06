package net.deadlydiamond98.koalalib.mixin.client.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs.ICustomSign;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodType;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SignEditScreen.class)
public class SignEditScreenMixin {
    @WrapOperation(method = "renderSignBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TexturedRenderLayers;getSignTextureId(Lnet/minecraft/block/WoodType;)Lnet/minecraft/client/util/SpriteIdentifier;"))
    private SpriteIdentifier koalalib$renderSignBackground(WoodType signType, Operation<SpriteIdentifier> original, @Local(argsOnly = true) BlockState state) {
        if (state.getBlock() instanceof ICustomSign sign) {
            return new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, sign.getTexture());
        }
        return original.call(signType);
    }
}
