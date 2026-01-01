package net.deadlydiamond98.koalalib.client.screen.config.inputs;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Toggle Button for Boolean Config Values. I tried to move some repeated into a parent class for enumButton, but for some reason that breaks things...
 */
public class BooleanButton extends ButtonWidget implements IConfigEntry {
    private final int originalY;
    private final String translation;
    private int renderY;
    private boolean hasDesc = true;

    private boolean bl;

    public BooleanButton(String translation, int x, int y, int width, int height, boolean bl) {
        super(x, y, width, height, Text.of(bl ? "True" : "False"), BooleanButton::changeValue, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.bl = bl;
        this.originalY = y;
        this.renderY = y;
        this.translation = translation;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        renderTitleText(context, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public boolean getBool() {
        return this.bl;
    }

    private static void changeValue(ButtonWidget button) {
        ((BooleanButton) button).changeValue();
    }

    private void changeValue() {
        this.bl = !this.bl;
    }

    @Override
    public Text getMessage() {
        return Text.of(this.bl ? "True" : "False");
    }

    @Override
    public boolean canRenderTooltip() {
        return this.isHovered();
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
}
