package net.deadlydiamond98.koalalib.client.screen.config;

import net.deadlydiamond98.koalalib.client.screen.config.entry.ConfigEntries;
import net.deadlydiamond98.koalalib.config.KoalaLibConfigs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public class ConfigScrollBar {
    private static final int SCROLL_STEP = 10;

    private int scrollOffset;
    public int scrollBarX;
    private int lastListSize;

    public ConfigScrollBar(int scrollBarX) {
        this.scrollBarX = scrollBarX;
    }

    public int scroll(double amount, int height, ConfigEntries configEntries) {
        this.lastListSize = configEntries.getEntries().size();
        this.scrollOffset = (int) Math.max(0, Math.min(getMaxScroll(height, this.lastListSize), this.scrollOffset - (amount * SCROLL_STEP)));
        return this.scrollOffset;
    }

    public void render(DrawContext context, int width, int height, ConfigEntries configEntries) {
        this.lastListSize = configEntries.getEntries().size();
        renderOld(context, width, height);
    }

    public void renderOld(DrawContext context, int width, int height) {
        int scrollBarHeight = (height - 63) - 34;

        if (this.getMaxScroll(height, this.lastListSize) > 0) {
            this.scrollBarX = KoalaLibConfigs.Main.fancyTransitions ? (int) MathHelper.lerp(0.1, this.scrollBarX, width - 10) : width - 10;

            int scrollBarThumbHeight = Math.max(20, (scrollBarHeight * scrollBarHeight) / (scrollBarHeight + getMaxScroll(height, this.lastListSize)));
            int scrollThumbY = 34 + (this.scrollOffset * (scrollBarHeight - scrollBarThumbHeight) / getMaxScroll(height, this.lastListSize));

            context.fill(this.scrollBarX, 34, this.scrollBarX + 6, height - 61, -16777216);
            context.fill(this.scrollBarX, scrollThumbY, this.scrollBarX + 6, scrollThumbY + scrollBarThumbHeight, -8355712);
            context.fill(this.scrollBarX, scrollThumbY, this.scrollBarX + 5, scrollThumbY + scrollBarThumbHeight - 1, -4144960);
        }
    }

    public int getMaxScroll(int height, int listSize) {
        int top = 34;
        int bottom = height - 61;
        int availableHeight = bottom - top;
        int visibleButtons = availableHeight / 25;
        return Math.max(0, (listSize - visibleButtons) * 25);
    }
}
