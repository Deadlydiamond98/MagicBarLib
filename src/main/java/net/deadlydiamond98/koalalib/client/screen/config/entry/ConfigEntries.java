package net.deadlydiamond98.koalalib.client.screen.config.entry;

import net.deadlydiamond98.koalalib.client.screen.config.inputs.BooleanButton;
import net.deadlydiamond98.koalalib.client.screen.config.inputs.ConfigTextInput;
import net.deadlydiamond98.koalalib.client.screen.config.inputs.IConfigEntry;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.util.math.MathHelper;
import net.deadlydiamond98.koalalib.client.screen.config.KoalaConfigScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains and displays the config entries that are currently being used. <br>
 * Moved to its own class to make things less cluttered in {@link KoalaConfigScreen}.
 */
public class ConfigEntries {

    private final OldConfigEntries oldConfigEntries = new OldConfigEntries();
    private final List<ClickableWidget> entries = new ArrayList<>();

    public List<ClickableWidget> getEntries() {
        return this.entries;
    }

    public void renderEntries(DrawContext context, int mouseX, int mouseY, float delta, int width) {
        this.oldConfigEntries.render(context, mouseX, mouseY, delta, width);

        this.entries.forEach(widget -> {
            widget.setX((int) MathHelper.lerp(0.1, widget.getX(), width - 100));
        });
    }

    public void renderEntryTooltips(TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY) {
        this.entries.forEach(widget -> {
            if (widget instanceof IConfigEntry button) {
                button.renderDescriptionTooltip(context, textRenderer, mouseX, mouseY);
            }
        });
    }

    public void scrollEntries(int scrollOffset) {
        this.entries.forEach(widget -> {
            if (widget instanceof IConfigEntry entry) {
                entry.scroll(-scrollOffset);
            }
        });
    }

    /**
     * Saves the values input into the current entries, moves them to the oldConfigEntry list (for swiping away), and then creates new ones.
     */
    public void swapDisplayedConfigEntries(String modID, int width, TextRenderer textRenderer) {
        Class<?> configScreen = KoalaConfigCreator.MOD_CONFIGS.get(modID);

        applyConfigValues(configScreen);
        this.oldConfigEntries.getEntries().addAll(this.entries);

        this.entries.clear();

        try {
            int i = 0;
            for (Field field : configScreen.getFields()) {

                Class<?> type = field.getType();
                Object value = field.get(configScreen);

                this.entries.add(getWidget(type, value, i++, width, textRenderer, field.getName()));
            }

        } catch (Exception ignored) {}
    }

    /**
     * Saves the values input into the current entries
     */
    public void applyConfigValues(Class<?> configScreen) {
        try {
            int i = 0;
            for (Field field : configScreen.getFields()) {
                ClickableWidget entry = this.entries.get(i++);
                Class<?> type = field.getType();

                Object value;
                if (entry instanceof BooleanButton bl) {
                    value = bl.getBool();
                } else if (entry instanceof ConfigTextInput input) {
                    String tempVal = input.getText();
                    if (type == int.class) {
                        value = Integer.parseInt(tempVal);
                    } else if (type == double.class) {
                        value = Double.parseDouble(tempVal);
                    } else if (type == float.class) {
                        value = Float.parseFloat(tempVal);
                    } else {
                        value = tempVal;
                    }
                } else {
                    return;
                }

                field.set(configScreen, value);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Returns the widget corresponding to the value type
     */
    private ClickableWidget getWidget(Class<?> type, Object value, int offsetY, int screenWidth, TextRenderer textRenderer, String name) {
        int x = screenWidth + 100;
        int y = (offsetY * 25) + 34;
        int width = 75;
        int height = 20;

        if (type == int.class) {
            return new ConfigTextInput(textRenderer, x, y, width, height, (int)value);
        } else if (type == double.class) {
            return new ConfigTextInput(textRenderer, x, y, width, height, (double)value);
        } else if (type == float.class) {
            return new ConfigTextInput(textRenderer, x, y, width, height, (float)value);
        } else if (type == boolean.class) {
            return new BooleanButton(x, y, width, height, (boolean)value);
        } else {
            return new ConfigTextInput(textRenderer, x, y, width, height, value.toString());
        }
    }
}
