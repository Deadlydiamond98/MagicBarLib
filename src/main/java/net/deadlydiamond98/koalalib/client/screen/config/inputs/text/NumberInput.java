package net.deadlydiamond98.koalalib.client.screen.config.inputs.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public class NumberInput extends StringInput {

    public NumberInput(String translation, TextRenderer textRenderer, int x, int y, int width, int height, int defaultValue) {
        this(translation, textRenderer, x, y, width, height, String.valueOf(defaultValue));
    }

    protected NumberInput(String translation, TextRenderer textRenderer, int x, int y, int width, int height, String defaultValue) {
        super(translation, textRenderer, x, y, width, height, defaultValue);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (!(this.getText().isEmpty() || this.getText().equals(".") || this.getText().equals("-")) && !isSelected()) {
            setText(limitNumbers());
        }
    }

    @Override
    public void write(String text) {
        super.write(checkNumericalInput(text));
    }

    private String checkNumericalInput(String text) {
        String decimalPoint = isDecimal() ? "." : "";
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

    protected boolean isDecimal() {
        return false;
    }

    protected String limitNumbers() {
        int numInput = Integer.parseInt(getText());
        numInput = (int) MathHelper.clamp(numInput, this.min, this.max);
        return numInput + "";
    }

    @Override
    public String getText() {
        String text = super.getText();
        if (text.isEmpty()) {
            return String.valueOf(this.defaultValue);
        }
        return text;
    }
}
