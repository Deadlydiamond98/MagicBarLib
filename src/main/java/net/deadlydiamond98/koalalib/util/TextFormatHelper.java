package net.deadlydiamond98.koalalib.util;

public class TextFormatHelper {

    public static int findCommaSplitIndex(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return -1;
        }

        int splitIndex = -1;
        for (int i = 0; i < maxLength; i++) {
            if (text.charAt(i) == ',') {
                splitIndex = i + 1;
            }
        }

        return splitIndex;
    }

    public static int findSplitIndex(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text.length();
        }

        int splitIndex = maxLength;
        while (splitIndex > 0 && text.charAt(splitIndex) != ' ') {
            splitIndex--;
        }

        return (splitIndex > 0) ? splitIndex : maxLength;
    }

    /**
     * Takes in a string, and returns the last color code present.<br>
     * If there is no color code present, returns an empty string
     * @param text text to find color code in
     * @return returns the color code symbol, or an empty string
     */
    public static String returnColorFormatSymbol(String text) {
        int i = text.lastIndexOf("§");

        if (i != -1 && i < text.length() - 1) {
            return text.substring(i, i + 2);
        }
        return "";
    }
}