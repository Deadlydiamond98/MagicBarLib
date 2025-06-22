package net.deadlydiamond98.koalalib.util;

import net.minecraft.util.math.MathHelper;

public class ColorHelper {

    public static int lerpHexColor(int startColor, int endColor, float tickDelta) {
        return lerpHexColor(startColor, endColor, tickDelta, 1.0f);
    }

    public static int lerpHexColor(int startColor, int endColor, float tickDelta, float alpha) {
        int startA = (startColor >> 24) & 0xFF;
        int startR = (startColor >> 16) & 0xFF;
        int startG = (startColor >> 8) & 0xFF;
        int startB = startColor & 0xFF;

        int endA = (endColor >> 24) & 0xFF;
        int endR = (endColor >> 16) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int endB = endColor & 0xFF;

        int a = (int) (MathHelper.lerp(tickDelta, startA, endA * alpha));
        int r = MathHelper.lerp(tickDelta, startR, endR);
        int g = MathHelper.lerp(tickDelta, startG, endG);
        int b = MathHelper.lerp(tickDelta, startB, endB);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int[] hexToARGB(int hexColor) {
        int a = (hexColor >> 24) & 0xFF;
        int r = (hexColor >> 16) & 0xFF;
        int g = (hexColor >> 8) & 0xFF;
        int b = hexColor & 0xFF;

        return new int[]{a, r, g, b};
    }
}
