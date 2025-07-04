package net.deadlydiamond98.koalalib.client.screen.config.inputs;

import net.deadlydiamond98.koalalib.util.TextFormatHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public interface IConfigEntry {

    default void renderTitleText(DrawContext context, int x, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

//        context.drawTextWithShadow(textRenderer, "Test", x, y, 0xFFFFFF);
    }

    default void renderDescriptionTooltip(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
        if (canRenderTooltip() && !getTranslation().isEmpty()) {
            List<Text> tooltip = new ArrayList<>();

            int maxLen = 35;

            String description = Text.translatable(getTranslation()).getString();

            while (description.length() > maxLen) {
                int split = TextFormatHelper.findSplitIndex(description, maxLen);
                tooltip.add(Text.literal(description.substring(0, split).trim()));
                description = description.substring(split).trim();
            }
            tooltip.add(Text.literal(description));

            context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
        }
    }

    boolean canRenderTooltip();

    default String getTranslation() {
        return "";
    }

    void scroll(int scrollOffset);

//    void setTranslation(String translation);
}
