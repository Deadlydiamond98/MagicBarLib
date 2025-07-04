package net.deadlydiamond98.koalalib.client.screen.config.entry;

import net.deadlydiamond98.koalalib.client.screen.config.inputs.IConfigEntry;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ConfigEntries {
    private final List<ClickableWidget> configEntries = new ArrayList<>();


    public void render(int width) {
        this.configEntries.forEach(widget -> {
            widget.setX((int) MathHelper.lerp(0.1, widget.getX(), width - 100));
        });
    }

    public void renderTooltips(TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY) {
        this.configEntries.forEach(widget -> {
            if (widget instanceof IConfigEntry button) {
                button.renderDescriptionTooltip(context, textRenderer, mouseX, mouseY);
            }
        });
    }
}
