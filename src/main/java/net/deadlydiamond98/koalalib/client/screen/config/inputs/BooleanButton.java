package net.deadlydiamond98.koalalib.client.screen.config.inputs;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Toggle Button for Boolean Config Values
 */
public class BooleanButton extends ButtonWidget implements IConfigEntry {
    private final int originalY;
    private int renderY;

    private boolean bl;

    public BooleanButton(int x, int y, int width, int height, boolean bl) {
        super(x, y, width, height, Text.of(bl ? "True" : "False"), BooleanButton::changeValue, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.bl = bl;
        this.originalY = y;
        this.renderY = y;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTitleText(context, this.getX(), this.getY());
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
}
