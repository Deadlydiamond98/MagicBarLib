package net.deadlydiamond98.koalalib.client.screen.config.inputs;

import net.deadlydiamond98.koalalib.client.screen.widgets.AutoTickingTextFieldWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * A Text Field Input used for Config values that use Strings, Integers, Doubles, or Floats
 */
public class ConfigTextInput extends AutoTickingTextFieldWidget implements IConfigEntry {
    private final boolean isNumber, isDecimal;
    private final int originalY;
    private final String translation;
    private int renderY;
    private boolean hasDesc = true;

    public ConfigTextInput(String translation, TextRenderer textRenderer, int x, int y, int width, int height, double defaultValue) {
        this(translation, textRenderer, x, y, width, height, String.valueOf(defaultValue), true, true);
    }

    public ConfigTextInput(String translation, TextRenderer textRenderer, int x, int y, int width, int height, int defaultValue) {
        this(translation, textRenderer, x, y, width, height, String.valueOf(defaultValue), true, false);
    }

    public ConfigTextInput(String translation, TextRenderer textRenderer, int x, int y, int width, int height, String defaultValue) {
        this(translation, textRenderer, x, y, width, height, defaultValue, false, false);
    }

    private ConfigTextInput(String translation, TextRenderer textRenderer, int x, int y, int width, int height, String defaultValue, boolean isNumber, boolean isDecimal) {
        super(textRenderer, x, y, width, height, Text.empty());
        this.setText(defaultValue);
        this.translation = translation;
        this.isNumber = isNumber;
        this.isDecimal = isDecimal;
        this.originalY = y;
        this.renderY = y;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTitleText(context, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void write(String text) {
        super.write(this.isNumber ? checkNumericalInput(text) : text);
    }

    private String checkNumericalInput(String text) {
        String decimalPoint = this.isDecimal ? "." : "";
        String numsOnly = text.replaceAll("[^\\d" + decimalPoint + "-]", "");

        if (!getText().isEmpty()) {
            boolean canNegate = getText().charAt(0) != '-' && getCursor() == 0;

            if (numsOnly.contains("-") && !(canNegate)) {
                return "";
            }

            if (getText().contains(".") && numsOnly.contains(".")) {
                return "";
            }
        }
        return numsOnly;
    }

    @Override
    public void scroll(int scrollOffset) {
        this.renderY = this.originalY + scrollOffset;
        this.setY(this.renderY);
    }

    @Override
    public String getTranslation() {
        return this.translation;
    }

    @Override
    public boolean hasDesc() {
        return this.hasDesc;
    }

    @Override
    public void enableDesc(boolean hasDesc) {
        this.hasDesc = hasDesc;
    }

    @Override
    public boolean canRenderTooltip() {
        return this.isHovered();
    }
}
