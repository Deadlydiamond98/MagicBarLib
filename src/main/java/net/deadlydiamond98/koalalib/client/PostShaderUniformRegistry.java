package net.deadlydiamond98.koalalib.client;

import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.JsonEffectShaderProgram;

import java.util.HashMap;
import java.util.Map;

public class PostShaderUniformRegistry {
    private static final Map<String, Float> POST_UNIFORMS = new HashMap<>();

    public static void updatePostShaderUniformValue(String id, float newValue) {
        POST_UNIFORMS.put(id, newValue);
    }

    public static void renderPostShaderUniforms(JsonEffectShaderProgram program) {
        POST_UNIFORMS.forEach((string, f) -> {
            GlUniform glUniform = program.getUniformByName(string);
            if (glUniform != null) {
                glUniform.set(f);
            }
        });
    }
}
