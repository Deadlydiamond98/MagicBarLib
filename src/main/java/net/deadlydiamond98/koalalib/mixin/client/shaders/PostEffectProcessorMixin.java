package net.deadlydiamond98.koalalib.mixin.client.shaders;

import net.deadlydiamond98.koalalib.client.PostShaderUniformRegistry;
import net.minecraft.client.gl.PostEffectProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PostEffectProcessor.class)
public class PostEffectProcessorMixin {
    @Shadow private float lastTickDelta;
    @Unique private float koalalib$betterTime;

    @Inject(method = "render", at = @At("HEAD"))
    private void koalalib$render(float tickDelta, CallbackInfo ci) {
        if (tickDelta < this.lastTickDelta) {
            this.koalalib$betterTime += 1.0f - this.lastTickDelta;
            this.koalalib$betterTime += tickDelta;
        } else {
            this.koalalib$betterTime += tickDelta - this.lastTickDelta;
        }
        PostShaderUniformRegistry.updatePostShaderUniformValue("KoalaLibTime", this.koalalib$betterTime / 20.0f);
    }
}
