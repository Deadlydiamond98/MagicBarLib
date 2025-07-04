package net.deadlydiamond98.koalalib.client.screen.config.inputs;

import net.deadlydiamond98.koalalib.util.TextFormatHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public interface IConfigEntry {

    default void renderTitleText(DrawContext context, int x, int y, int width, int height) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        String name = Text.translatable(getTranslation()).getString();

        context.drawTextWithShadow(textRenderer, name, x - textRenderer.getWidth(name) - 5, y + height / 3, 0xFFFFFF);
    }

    default void renderDescriptionTooltip(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
        if (canRenderTooltip() && !getTranslation().isEmpty() && hasDesc()) {
            List<Text> tooltip = new ArrayList<>();

            int maxLen = 35;

            String description = Text.translatable(getTranslation() + ".desc").getString();

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
    void scroll(int scrollOffset);
    String getTranslation();

    boolean hasDesc();
    void enableDesc(boolean hasDesc);

//    default double getMax() {return 0;}
//    default void setMax(double max) {}
//    default double getMin() {return 0;}
//    default void setMin(double min) {}
}
