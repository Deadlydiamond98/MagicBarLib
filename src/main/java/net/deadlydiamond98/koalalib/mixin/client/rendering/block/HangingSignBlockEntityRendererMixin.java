package net.deadlydiamond98.koalalib.mixin.client.rendering.block;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs.ICustomSign;
import net.minecraft.block.WoodType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HangingSignBlockEntityRenderer.class)
public class HangingSignBlockEntityRendererMixin extends SignBlockEntityRendererMixin {

    @WrapMethod(method = "getTextureId")
    private SpriteIdentifier koalalib$getTextureId(WoodType signType, Operation<SpriteIdentifier> original) {
        if (this.koalalib$signBlockEntity != null && this.koalalib$signBlockEntity.getCachedState().getBlock() instanceof ICustomSign sign) {
            return new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, sign.getTexture());
        }
        return original.call(signType);
    }
}
