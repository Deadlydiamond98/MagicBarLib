package net.deadlydiamond98.koalalib.client.screen.config;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.client.screen.config.inputs.ConfigTextInput;
import net.deadlydiamond98.koalalib.client.screen.config.inputs.BooleanButton;
import net.deadlydiamond98.koalalib.client.screen.config.inputs.IConfigEntry;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Screen used for in-game config editing!
 */
public class KoalaConfigScreen extends GameOptionsScreen {
    private static final int SCROLL_STEP = 10;

    private final List<ClickableWidget> configEntries = new ArrayList<>();
    private final List<ClickableWidget> oldconfigEntries = new ArrayList<>();

    private boolean firstInit;
    private ModSelectionListWidget modSelections;
    private ButtonWidget doneButton;
    private @Nullable String currentModID = null;

    private int scrollOffset;

    public KoalaConfigScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable("koalalib.menu.configMenu"));
    }

    /**
     * Initializes everything (or just re-adds children if called after first time)
     */
    @Override
    protected void init() {

        if (!this.firstInit) {
            this.modSelections = new ModSelectionListWidget(
                    this.client, this.width, this.height, 32, this.height - 61, 18
            );
            this.doneButton = ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.close())
                    .dimensions(this.width / 2 - 75, this.height - 38, 150, 20).build();
            this.firstInit = !this.firstInit;
        }
        this.addSelectableChild(this.doneButton);
        this.configEntries.forEach(this::addDrawableChild);
        this.addSelectableChild(this.modSelections);

        super.init();
    }

    /**
     * Default render method, where everything renders
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBGStuff(context, false);

        oldConfigEntryRender(context, mouseX, mouseY, delta);
        this.modSelections.setScreenWidth(this.width);
        this.modSelections.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        generateConfigButtons();
        this.configEntries.forEach(this::configEntryRender);
        renderBGStuff(context, true);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 16777215);
        this.doneButton.render(context, mouseX, mouseY, delta);
        this.doneButton.setX(this.width / 2 - 75);
        this.doneButton.setY(this.height - 38);
        this.configEntries.forEach(clickableWidget -> renderTooltips(clickableWidget, context, mouseX, mouseY));
    }

    private void renderTooltips(ClickableWidget clickableWidget, DrawContext context, int mouseX, int mouseY) {
        if (clickableWidget instanceof IConfigEntry button) {
            button.renderDescriptionTooltip(context, this.textRenderer, mouseX, mouseY);
        }
    }


    /**
     * Renders the background and Foreground Dirt to Match Menus
     */
    private void renderBGStuff(DrawContext context, boolean isShadow) {
        int left = 0;
        int right = this.width;
        int top = 32;
        int bottom = this.height - 61;

        if (!isShadow) {
            // Background Texture
            context.setShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
            context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, left, top, (float)right, (float)(bottom + (int)this.modSelections.getScrollAmount()),
                    right - left, bottom - top, 32, 32);
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            context.enableScissor(left, top, right, bottom);
        } else {
            // Shadow Things
            context.disableScissor();
            context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
            context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, left, 0, 0.0F, 0.0F, this.width, top, 32, 32);
            context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, left, bottom, 0.0F, (float)bottom, this.width, this.height - bottom, 32, 32);
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            context.fillGradient(RenderLayer.getGuiOverlay(), left, top, right, top + 4, -16777216, 0, 0);
            context.fillGradient(RenderLayer.getGuiOverlay(), left, bottom - 4, right, bottom, 0, -16777216, 0);
        }
    }

    /**
     * Handles the Swipe Away Animation of Previous Config Input Fields, and Removes them when off-screen
     */
    private void oldConfigEntryRender(DrawContext context, int mouseX, int mouseY, float delta) {
        for (int i = this.oldconfigEntries.size() - 1; i > 0; i--) {
            ClickableWidget element = this.oldconfigEntries.get(i);

            element.render(context, mouseX, mouseY, delta);
            element.setX((int) MathHelper.lerp(0.1, element.getX(), this.width + 100));

            if (element.getX() > this.width + 50) {
                this.oldconfigEntries.remove(i);
            }
        }
    }

    /**
     * Handles the Slide-in animation of Config Input Fields
     */
    private void configEntryRender(ClickableWidget element) {
        element.setX((int) MathHelper.lerp(0.1, element.getX(), this.width - 100));
    }

    /**
     * Generates Config Inputs when a selection is made!
     */
    private void generateConfigButtons() {
        String modID = this.modSelections.getSelectionModID();
        if (modID != null && !Objects.equals(this.currentModID, modID)) {
            this.currentModID = modID;
            this.scrollOffset = 0;

            Class<?> configScreen = KoalaConfigCreator.MOD_CONFIGS.get(modID);

            try {
                int i = 0;
                for (Field field : configScreen.getFields()) {
                    ClickableWidget entry = this.configEntries.get(i++);
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
            this.oldconfigEntries.addAll(this.configEntries);

            this.configEntries.clear();

            try {
                int i = 0;
                for (Field field : configScreen.getFields()) {

                    Class<?> type = field.getType();
                    Object value = field.get(configScreen);

                    this.configEntries.add(getWidget(type, value, i++, field.getName()));
                }

            } catch (Exception ignored) {}
            clearAndInit();
        }
    }

    /**
     * Gets the widget type for a config Input
     */
    private ClickableWidget getWidget(Class<?> type, Object value, int offsetY, String name) {
        int x = this.width + 100;
        int y = (offsetY * 25) + 34;
        int width = 75;
        int height = 20;

        if (type == int.class) {
            return new ConfigTextInput(client.textRenderer, x, y, width, height, (int)value);
        } else if (type == double.class) {
            return new ConfigTextInput(client.textRenderer, x, y, width, height, (double)value);
        } else if (type == float.class) {
            return new ConfigTextInput(client.textRenderer, x, y, width, height, (float)value);
        } else if (type == boolean.class) {
            return new BooleanButton(x, y, width, height, (boolean)value);
        } else {
            return new ConfigTextInput(client.textRenderer, x, y, width, height, value.toString());
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX < this.width / 2.0) {
            return false;
        }

        this.scrollOffset = (int) Math.max(0, Math.min(getMaxScroll(), this.scrollOffset - (amount * SCROLL_STEP)));

        this.configEntries.forEach(widget -> {
            if (widget instanceof IConfigEntry entry) {
                entry.scroll(-this.scrollOffset);
            }
        });

        return true;
    }

    private int getMaxScroll() {
        int top = 34;
        int bottom = this.height - 61;
        int availableHeight = bottom - top;
        int visibleButtons = availableHeight / 25;
        return Math.max(0, (this.configEntries.size() - visibleButtons) * 25);
    }

    /**
     * Regular Close Method, but with the added functionality of updating config files
     */
    @Override
    public void close() {
        Class<?> configScreen = KoalaConfigCreator.MOD_CONFIGS.get(this.currentModID);
        try {
            int i = 0;
            for (Field field : configScreen.getFields()) {
                ClickableWidget entry = this.configEntries.get(i++);
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

        KoalaConfigCreator.updateAllConfigFiles();
        super.close();
    }
}
