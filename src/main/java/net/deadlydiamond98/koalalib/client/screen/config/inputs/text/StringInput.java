package net.deadlydiamond98.koalalib.client.screen.config.inputs.text;

import net.deadlydiamond98.koalalib.client.screen.config.inputs.IConfigEntry;
import net.deadlydiamond98.koalalib.client.screen.widgets.AutoTickingTextFieldWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * A Text Field Input used for Config values that use Strings, Integers, Doubles, or Floats
 */
public class StringInput extends AutoTickingTextFieldWidget implements IConfigEntry {
    private final int originalY;
    private final String translation;
    private int renderY;
    private boolean hasDesc = true;
    protected String defaultValue;

    protected double max = Integer.MAX_VALUE;
    protected double min = Integer.MIN_VALUE;

    public StringInput(String translation, TextRenderer textRenderer, int x, int y, int width, int height, String defaultValue) {
        super(textRenderer, x, y, width, height, Text.empty());
        this.setText(defaultValue);
        this.defaultValue = defaultValue;
        this.translation = translation;
        this.originalY = y;
        this.renderY = y;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTitleText(context, this.getX(), this.getY(), this.getWidth(), this.getHeight());
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
}
