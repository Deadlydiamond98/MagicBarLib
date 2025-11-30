package net.deadlydiamond98.koalalib.mixin.client.shaders;

import net.deadlydiamond98.koalalib.client.PostShaderUniformRegistry;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PostEffectPass.class)
public class PostEffectPassMixin {
    @Shadow @Final private JsonEffectShaderProgram program;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/JsonEffectShaderProgram;getUniformByNameOrDummy(Ljava/lang/String;)Lnet/minecraft/client/gl/Uniform;", ordinal = 3))
    private void koalalib$render(float time, CallbackInfo ci) {
        PostShaderUniformRegistry.renderPostShaderUniforms(this.program);
    }
}
