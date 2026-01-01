package net.deadlydiamond98.koalalib.client.renderer;

import net.deadlydiamond98.koalalib.common.items.other.IAnimatedSpriteIconItem;
import net.deadlydiamond98.koalalib.common.items.other.ISpriteIconItem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.HashMap;

public class IconItemRenderer {
    public static final HashMap<String, Pair<Integer, Long>> ICON_ANIMATIONS = new HashMap<>();

    /**
     * Helper Method that's used to render icons on items
     */
    public static void renderIcon(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ISpriteIconItem icon, PlayerEntity player, ItemStack stack) {
        if (icon.showIcon(player, stack)) {
            matrices.translate(0.5, 0.5, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(icon.getRotation()));
            matrices.translate(-0.5, -0.5, -0.5);

            matrices.scale(
                    icon.getScaleX(player, stack),
                    icon.getScaleY(player, stack),
                    1
            );
            matrices.translate(
                    icon.getTranslateX(player, stack) + (icon.pixelOffsetX() * 0.125),
                    icon.getTranslateY(player, stack) + (icon.pixelOffsetY() * 0.125),
                    icon.getTranslateZ()
            );

            MatrixStack.Entry entry = matrices.peek();
            Matrix4f modelMatrix = entry.getPositionMatrix();

            VertexConsumer vertexConsumer;
            vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(icon.getTexture(player, stack)));

            float minV = 0;
            float maxV = 1;

            if (icon instanceof IAnimatedSpriteIconItem animation && animation.getFrames(player, stack) > 1) {
                String key = stack.getItem().getTranslationKey();
                Pair<Integer, Long> pair = ICON_ANIMATIONS.getOrDefault(key, new Pair<>(0, 0L));
                long tick = System.currentTimeMillis() / 50;
                int frame = pair.getLeft();
                long lastTime = pair.getRight();

                if (tick != lastTime) {
                    if (tick % animation.getFrameTime(player, stack) == 0) {
                        frame = (frame + 1) % animation.getFrames(player, stack);
                    }
                    lastTime = tick;
                }
                ICON_ANIMATIONS.put(key, new Pair<>(frame, lastTime));

                float textureHeight = 1 / (float) animation.getFrames(player, stack);
                minV = textureHeight * frame;
                maxV = minV + textureHeight;
            }

            int light = LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE;
            vertexConsumer.vertex(modelMatrix, 1,  1, 0).color(255, 255, 255, 255).texture(1, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(modelMatrix,  -1,  1, 0).color(255, 255, 255, 255).texture(0, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(modelMatrix,  -1, -1, 0).color(255, 255, 255, 255).texture(0, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0, 1, 0);
            vertexConsumer.vertex(modelMatrix, 1, -1, 0).color(255, 255, 255, 255).texture(1, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0, 1, 0);
        }
    }
}
