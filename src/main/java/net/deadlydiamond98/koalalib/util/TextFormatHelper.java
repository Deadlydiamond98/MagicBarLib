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
}