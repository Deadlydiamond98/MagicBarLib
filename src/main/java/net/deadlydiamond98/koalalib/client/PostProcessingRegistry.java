package net.deadlydiamond98.koalalib.client;

/*
 * This file is part of KoalaLib, but it uses parts of code from the Citadel project by Alexthe668.
 * The original project is licensed under the GNU Lesser General Public License.
 * For more details, visit: https://github.com/AlexModGuy/Citadel/tree/master
 */


import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.deadlydiamond98.koalalib.KoalaLib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostProcessingRegistry {

    private static final List<Identifier> REGISTRY = new ArrayList<>();
    private static final Map<Identifier, PostEffect> POST_EFFECTS = new HashMap<>();

    public static void clear(){
        for (PostEffect postEffect : POST_EFFECTS.values()) {
            postEffect.close();
        }
        POST_EFFECTS.clear();
    }

    public static void registerEffect(Identifier id) {
        REGISTRY.add(id);
    }

    public static void onInitializeOutline() {
        clear();
        MinecraftClient minecraft = MinecraftClient.getInstance();
        for (Identifier id : REGISTRY) {
            PostEffectProcessor postProcessor;
            Framebuffer buffer;
            try {
                postProcessor = new PostEffectProcessor(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getFramebuffer(), id);
                postProcessor.setupDimensions(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
                buffer = postProcessor.getSecondaryTarget("final");
            } catch (IOException ioexception) {
                KoalaLib.LOGGER.warn("Failed to load shader: {}", id, ioexception);
                postProcessor = null;
                buffer = null;
            } catch (JsonSyntaxException jsonsyntaxexception) {
                KoalaLib.LOGGER.warn("Failed to parse shader: {}", id, jsonsyntaxexception);
                postProcessor = null;
                buffer = null;
            }
            POST_EFFECTS.put(id, new PostEffect(postProcessor, buffer, false));
        }
    }

    public static void resize(int x, int y) {
        for (PostEffect postEffect : POST_EFFECTS.values()) {
            postEffect.resize(x, y);
        }
    }

    public static PostEffectProcessor getPostChainFor(Identifier id) {
        PostEffect effect = POST_EFFECTS.get(id);
        return effect == null ? null : effect.getPostProcessor();
    }

    public static Framebuffer getRenderTargetFor(Identifier id) {
        PostEffect effect = POST_EFFECTS.get(id);
        return effect == null ? null : effect.getBuffer();
    }

    public static void renderEffectForNextTick(Identifier id) {
        PostEffect effect = POST_EFFECTS.get(id);
        if (effect != null) {
            effect.setEnabled(true);
        }
    }

    public static void blitEffects() {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        for (PostEffect postEffect : POST_EFFECTS.values()) {
            if (postEffect.getPostProcessor() != null && postEffect.isEnabled()) {
                postEffect.getBuffer().draw(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight(), false);
                postEffect.setEnabled(false);
                postEffect.getBuffer().clear(MinecraftClient.IS_SYSTEM_MAC);
                MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void copyDepth(Framebuffer mainTarget) {
        for (PostEffect postEffect : POST_EFFECTS.values()) {
            if (postEffect.getPostProcessor() != null && postEffect.isEnabled()) {
                postEffect.getBuffer().clear(MinecraftClient.IS_SYSTEM_MAC);
                postEffect.getBuffer().copyDepthFrom(mainTarget);
            }
        }
    }

    public static void processEffects(Framebuffer mainTarget, float f) {
        for(PostEffect postEffect : POST_EFFECTS.values()) {
            if (postEffect.isEnabled() && postEffect.postProcessor != null) {
                postEffect.postProcessor.render(MinecraftClient.getInstance().getTickDelta());
                mainTarget.beginWrite(false);
            }
        }
    }


    private static class PostEffect {
        private final PostEffectProcessor postProcessor;
        private final Framebuffer buffer;
        private boolean enabled;

        public PostEffect(PostEffectProcessor postChain, Framebuffer renderTarget, boolean enabled) {
            this.postProcessor = postChain;
            this.buffer = renderTarget;
            this.enabled = enabled;
        }

        public PostEffectProcessor getPostProcessor() {
            return postProcessor;
        }

        public Framebuffer getBuffer() {
            return buffer;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void close(){
            if (postProcessor != null) {
                postProcessor.close();
            }
        }

        public void resize(int x, int y){
            if (postProcessor != null) {
                postProcessor.setupDimensions(x, y);
            }
        }
    }
}