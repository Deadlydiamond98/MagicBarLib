package net.deadlydiamond98.koalalib.mixin.client.rendering.block;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs.ICustomSign;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {
    @Unique protected SignBlockEntity koalalib$signBlockEntity;

    @WrapMethod(method = "render(Lnet/minecraft/block/entity/SignBlockEntity;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/block/BlockState;Lnet/minecraft/block/AbstractSignBlock;Lnet/minecraft/block/WoodType;Lnet/minecraft/client/model/Model;)V")
    private void koalalib$render(SignBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockState state, AbstractSignBlock block, WoodType woodType, Model model, Operation<Void> original) {
        this.koalalib$signBlockEntity = entity;
        original.call(entity, matrices, vertexConsumers, light, overlay, state, block, woodType, model);
        this.koalalib$signBlockEntity = null;
    }

    @WrapMethod(method = "getTextureId")
    private SpriteIdentifier koalalib$getTextureId(WoodType signType, Operation<SpriteIdentifier> original) {
        if (this.koalalib$signBlockEntity != null && this.koalalib$signBlockEntity.getCachedState().getBlock() instanceof ICustomSign sign) {
            return new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, sign.getTexture());
        }
        return original.call(signType);
    }
}
