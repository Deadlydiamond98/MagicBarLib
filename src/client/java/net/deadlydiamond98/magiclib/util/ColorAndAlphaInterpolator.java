package net.deadlydiamond98.magiclib.util;

import net.minecraft.util.math.MathHelper;

public class ColorAndAlphaInterpolator {

    // used for smoothly transitioning colors and textures (via alpha) for the Magic bar

    private int startColor;
    private int endColor;
    private float startAlpha;
    private float endAlpha;
    private float transitionTime;
    private float elapsedTime;

    public ColorAndAlphaInterpolator(int startColor, int endColor, float startAlpha, float endAlpha, float transitionTime) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
        this.transitionTime = transitionTime;
        this.elapsedTime = 0;
    }

    // return the color over time with lerping, my beloved
    public int updateAndGetColor(float deltaTime, float globalAlpha) {

        elapsedTime += deltaTime;

        float t = MathHelper.clamp(elapsedTime / transitionTime, 0, 1);

        return lerpColor(startColor, endColor, t, globalAlpha);
    }

    // same thing, but instead alpha
    public float updateAndGetAlpha(float deltaTime) {
        elapsedTime += deltaTime;

        float t = MathHelper.clamp(elapsedTime / transitionTime, 0, 1);

        return MathHelper.lerp(t, startAlpha, endAlpha);
    }

    // Lerp the color, since it's not a number, but rather a hex code, this is how it's done
    private int lerpColor(int startColor, int endColor, float t, float globalAlpha) {
        int startA = (startColor >> 24) & 0xFF;
        int startR = (startColor >> 16) & 0xFF;
        int startG = (startColor >> 8) & 0xFF;
        int startB = startColor & 0xFF;

        int endA = (endColor >> 24) & 0xFF;
        int endR = (endColor >> 16) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int endB = endColor & 0xFF;

        int a = (int) (MathHelper.lerp(t, startA, endA) * globalAlpha);
        int r = MathHelper.lerp(t, startR, endR);
        int g = MathHelper.lerp(t, startG, endG);
        int b = MathHelper.lerp(t, startB, endB);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    // restart the transition and used variables for reuse
    public void resetTransition(int startColor, int endColor, float startAlpha, float endAlpha, float transitionTime) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
        this.transitionTime = transitionTime;
        this.elapsedTime = 0;
    }
}