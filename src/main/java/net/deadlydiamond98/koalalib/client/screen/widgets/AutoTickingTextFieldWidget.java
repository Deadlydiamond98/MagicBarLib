package net.deadlydiamond98.koalalib.client.screen.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.Calendar;

/**
 * A Widget that "ticks" so that the _ that represents the current position animates
 */
public class AutoTickingTextFieldWidget extends TextFieldWidget {
    public AutoTickingTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        if (Calendar.getInstance().getTimeInMillis() % 2 == 0) {
            // TODO: FIX THIS
//            this.tick(); // This is here to animate the typing
        }
    }
}
