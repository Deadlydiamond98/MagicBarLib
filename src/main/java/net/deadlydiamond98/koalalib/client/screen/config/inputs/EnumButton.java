package net.deadlydiamond98.koalalib.client.screen.config.inputs;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class EnumButton extends ButtonWidget implements IConfigEntry {
    private final int originalY;
    private final String translation;
    private int renderY;
    private boolean hasDesc = true;

    private final String modID;

    private final Class<?> enumType;
    private Enum enumValue;

    public EnumButton(String translation, int x, int y, int width, int height, Class<?> enumType, Enum<?> enumValue, String modID) {
        super(x, y, width, height, Text.of(""), EnumButton::changeValue, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.enumType = enumType;
        this.enumValue = enumValue;
        this.originalY = y;
        this.renderY = y;
        this.translation = translation;
        this.modID = modID;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        renderTitleText(context, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    private static void changeValue(ButtonWidget button) {
        ((EnumButton) button).changeValue();
    }

    private void changeValue() {
        Object[] enumConstants = this.enumType.getEnumConstants();
        int ordinal = (enumValue.ordinal() + 1) % (enumConstants.length);
        this.enumValue = (Enum<?>) enumConstants[ordinal];
    }

    @Override
    public Text getMessage() {
        return Text.translatable(this.modID + this.enumValue.name());
    }

    public Enum<?> getEnum() {
        return this.enumValue;
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
