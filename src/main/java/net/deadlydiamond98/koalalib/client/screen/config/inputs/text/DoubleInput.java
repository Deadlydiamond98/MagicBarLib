package net.deadlydiamond98.koalalib.client.screen.config.inputs.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.MathHelper;

public class DoubleInput extends NumberInput {

    public DoubleInput(String translation, TextRenderer textRenderer, int x, int y, int width, int height, double defaultValue) {
        super(translation, textRenderer, x, y, width, height, String.valueOf(defaultValue));
    }

    @Override
    protected String limitNumbers() {
        double decimalInput = Double.parseDouble(getText());
        decimalInput = MathHelper.clamp(decimalInput, this.min, this.max);
        return decimalInput + "";
    }

    @Override
    protected boolean isDecimal() {
        return true;
    }
}
