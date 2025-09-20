package net.deadlydiamond98.koalalib.util;


public class ColorHelper {

    /**
     * Blends two hex colors together
     * @param start the first color
     * @param end the second color
     * @param step how close the color should be to b (numbers closer to 1 are closer to b)
     * @return the blended color
     */
    public static int blendHexColors(int start, int end, float step) {
        int[] startARGB = HexToARGB(start);
        int[] endARGB = HexToARGB(end);

        int cA = blendARGBValue(startARGB[0], endARGB[0], step);
        int cR = blendARGBValue(startARGB[1], endARGB[1], step);
        int cG = blendARGBValue(startARGB[2], endARGB[2], step);
        int cB = blendARGBValue(startARGB[3], endARGB[3], step);

        return ARGBToHex(cA, cR, cG, cB);
    }

    /**
     * Blends a single value from an ARGB Color (ex: blending Blue Value from both)
     * @param start the value from the first ARGB color
     * @param end the value from the second ARGB color
     * @param step how close the color should be to b (numbers closer to 1 are closer to b)
     * @return returns the blended value
     */
    public static int blendARGBValue(int start, int end, float step) {
        if (step > 1) {
            step = 1;
        } else if (step < 0) {
            step = 0;
        }
        float iRatio = 1 - step;

        return ((int)(start * iRatio) + (int)(end * step));
    }

    /**
     * Converts hex to ARGB
     * @param hex hex color
     * @return hex color in ARGB form
     */
    public static int[] HexToARGB(int hex) {
        int a = (hex >> 24) & 0xFF;
        int r = (hex >> 16) & 0xFF;
        int g = (hex >> 8) & 0xFF;
        int b = hex & 0xFF;

        return new int[]{a, r, g, b};
    }

    /**
     * Converts ARGB to Hex color
     * @param a alpha value
     * @param r red value
     * @param g green value
     * @param b blue value
     * @return Hex Color
     */
    public static int ARGBToHex(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
