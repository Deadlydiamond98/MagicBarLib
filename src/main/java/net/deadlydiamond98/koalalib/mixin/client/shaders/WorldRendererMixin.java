package net.deadlydiamond98.koalalib.mixin.client.shaders;

import net.deadlydiamond98.koalalib.client.PostProcessingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * This file is part of The Legend of Steve, but it uses
 * code from the Citadel project by Alexthe668.
 * The original project is licensed under the GNU Lesser General Public License.
 * For more details, visit: https://github.com/AlexModGuy/Citadel/tree/master
 */

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "loadEntityOutlinePostProcessor", at = @At("TAIL"))
    private void koalalib$loadEntityOutlinePostProcessor(CallbackInfo ci) {
        PostProcessingRegistry.onInitializeOutline();
    }

    @Inject(method = "onResized", at = @At("TAIL"))
    private void koalalib$onResized(int width, int height, CallbackInfo ci) {
        PostProcessingRegistry.resize(width, height);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;", shift = At.Shift.BEFORE))
    private void koalalib$renderGetEntityVertexConsumers(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        PostProcessingRegistry.copyDepth(this.client.getFramebuffer());
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;draw()V", shift = At.Shift.BEFORE))
    private void koalalib$renderDraw(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        PostProcessingRegistry.processEffects(this.client.getFramebuffer(), tickCounter.getLastFrameDuration());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void koalalib$renderTail(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        PostProcessingRegistry.blitEffects();
    }
}
