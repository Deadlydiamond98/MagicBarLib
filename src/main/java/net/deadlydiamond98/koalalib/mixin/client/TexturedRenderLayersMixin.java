package net.deadlydiamond98.koalalib.mixin.client;

import net.deadlydiamond98.koalalib.client.SpriteIdentifierRegistry;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
    @Inject(method = "addDefaultTextures", at = @At("RETURN"))
    private static void koalalib$addDefaultTextures(Consumer<SpriteIdentifier> consumer, CallbackInfo info) {
        SpriteIdentifierRegistry.getSpriteIdentifiers().forEach(consumer);
    }
}
