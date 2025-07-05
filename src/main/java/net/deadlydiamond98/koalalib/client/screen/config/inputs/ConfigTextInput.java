package net.deadlydiamond98.koalalib.client.screen.config.inputs;

import net.deadlydiamond98.koalalib.client.screen.widgets.AutoTickingTextFieldWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

/**
 * A Text Field Input used for Config values that use Strings, Integers, Doubles, or Floats
 */
public class ConfigTextInput extends AutoTickingTextFieldWidget implements IConfigEntry {
    private final boolean isNumber, isDecimal;
    private final int originalY;
    private final String translation;
    private int renderY;
    private boolean hasDesc = true;
    private String defaultValue;

    private double max = Integer.MAX_VALUE;
    private double min = Integer.MIN_VALUE;

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
        this.defaultValue = defaultValue;
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

        if (!(this.getText().isEmpty() || this.getText().equals(".") || this.getText().equals("-")) && !isSelected()) {
            setText(limitNumbers());
        }
    }

    @Override
    public void write(String text) {
        super.write(this.isNumber ? checkNumericalInput(text) : text);
    }

    private String limitNumbers() {
        if (this.isNumber) {

            if (this.isDecimal) {
                double decimalInput = Double.parseDouble(getText());
                decimalInput = MathHelper.clamp(decimalInput, min, max);
                return decimalInput + "";
            }

            int numInput = Integer.parseInt(getText());
            numInput = (int) MathHelper.clamp(numInput, min, max);
            return numInput + "";
        }
        return getText();
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

    public void setMaxNumber(double max) {
        this.max = max;
    }

    public void setMinNumber(double min) {
        this.min = min;
    }

    @Override
    public String getText() {
        String text = super.getText();
        if (text.isEmpty() && this.isNumber) {
            return String.valueOf(this.defaultValue);
        }
        return text;
    }
}
