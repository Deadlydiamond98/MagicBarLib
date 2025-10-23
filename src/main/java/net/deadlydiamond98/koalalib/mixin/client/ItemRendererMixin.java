package net.deadlydiamond98.koalalib.mixin.client;

import net.deadlydiamond98.koalalib.common.items.other.ISpriteIconItem;
import net.deadlydiamond98.koalalib.util.IconItemHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void koalalib$onRender(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        Item item = stack.getItem();

        matrices.push();
        if (renderMode == ModelTransformationMode.GUI) {
            if (item instanceof ISpriteIconItem spriteIconItem) {
                IconItemHelper.renderIcon(matrices, vertexConsumers, spriteIconItem, player, stack);
            } else if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ISpriteIconItem spriteIconItem) {
                IconItemHelper.renderIcon(matrices, vertexConsumers, spriteIconItem, player, stack);
            }
        }
        matrices.pop();
    }
}
