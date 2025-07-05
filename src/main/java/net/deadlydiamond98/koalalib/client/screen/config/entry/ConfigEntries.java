package net.deadlydiamond98.koalalib.client.screen.config.entry;

import net.deadlydiamond98.koalalib.client.screen.config.inputs.BooleanButton;
import net.deadlydiamond98.koalalib.client.screen.config.inputs.ConfigTextInput;
import net.deadlydiamond98.koalalib.client.screen.config.inputs.EnumButton;
import net.deadlydiamond98.koalalib.client.screen.config.inputs.IConfigEntry;
import net.deadlydiamond98.koalalib.config.CFGProperties;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.deadlydiamond98.koalalib.config.configs.MainConfigs;
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
    public final List<ClickableWidget> entries = new ArrayList<>();

    public List<ClickableWidget> getEntries() {
        return this.entries;
    }

    public void renderEntries(DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
        this.entries.forEach(widget -> {
            if (MainConfigs.fancyTransitions) {
                widget.setX((int) MathHelper.lerp(0.1, widget.getX(), width - 100));
            } else {
                widget.setX(width - 100);
            }
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
    public void swapDisplayedConfigEntries(String oldModID, String modID, int width, TextRenderer textRenderer) {
        if (oldModID != null) {
            Class<?> oldConfigScreen = KoalaConfigCreator.MOD_CONFIGS.get(oldModID).getA();
            applyConfigValues(oldConfigScreen);
        }

        Class<?> configScreen = KoalaConfigCreator.MOD_CONFIGS.get(modID).getA();

        this.entries.clear();

        try {
            int i = 0;
            for (Field field : configScreen.getFields()) {
                Object value = field.get(configScreen);

                this.entries.add(getWidget(field, value, i++, width, textRenderer, modID));
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
                } else if (entry instanceof EnumButton enumm) {
                    value = enumm.getEnum();
                } else if (entry instanceof ConfigTextInput input) {
                    String tempVal = input.getText();
                    if (type == int.class) {
                        value = Integer.parseInt(tempVal);
                    } else if (type == double.class) {
                        value = Double.parseDouble(tempVal);
                    } else if (type == float.class) {
                        value = Float.parseFloat(tempVal);
                    }
                    else {
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
    private ClickableWidget getWidget(Field field, Object value, int offsetY, int screenWidth, TextRenderer textRenderer, String modID) {
        Class<?> type = field.getType();

        String translation = modID + ".config." + field.getName();

        int x = screenWidth + 400;
        int y = (offsetY * 25) + 34;
        int width = 75;
        int height = 20;

        ClickableWidget entry;
        if (type == int.class) {
            entry = new ConfigTextInput(translation, textRenderer, x, y, width, height, (int)value);
        } else if (type == double.class) {
            entry = new ConfigTextInput(translation, textRenderer, x, y, width, height, (double)value);
        } else if (type == float.class) {
            entry = new ConfigTextInput(translation, textRenderer, x, y, width, height, (float)value);
        } else if (type == boolean.class) {
            entry = new BooleanButton(translation, x, y, width, height, (boolean)value);
        } else if (type.isEnum()) {
            entry = new EnumButton(translation, x, y, width, height, type, (Enum<?>) value, modID + ".enum." + field.getName() + ".");
        } else {
            entry = new ConfigTextInput(translation, textRenderer, x, y, width, height, value.toString());
        }

        if (field.isAnnotationPresent(CFGProperties.class)) {
            CFGProperties cfgProperties = field.getAnnotation(CFGProperties.class);
            entry = applyProperties(entry, cfgProperties);
        }

        return entry;
    }

    private ClickableWidget applyProperties(ClickableWidget entry, CFGProperties cfgProperties) {
        if (entry instanceof IConfigEntry configEntry) {
            configEntry.enableDesc(cfgProperties.hasDesc());
        }

        return entry;
    }
}
