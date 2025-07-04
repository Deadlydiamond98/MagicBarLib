package net.deadlydiamond98.koalalib.client.screen.config;

import net.deadlydiamond98.koalalib.client.screen.config.entry.ConfigEntries;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Screen used for in-game config editing!
 */
public class KoalaConfigScreen extends GameOptionsScreen {
    private static final int SCROLL_STEP = 10;

    private final ConfigScrollBar scrollBar = new ConfigScrollBar();
    private final ConfigEntries configEntries = new ConfigEntries();

    private boolean firstInit;
    private ModSelectionListWidget modSelections;
    private ButtonWidget doneButton;
    private @Nullable String currentModID = null;

    private int scrollOffset;
    private int scrollBarX;

    public KoalaConfigScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable("koalalib.menu.configMenu"));
    }

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
        this.configEntries.getEntries().forEach(this::addDrawableChild);
        this.addSelectableChild(this.modSelections);
        this.scrollBarX = this.width + 190;

        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderDirtBGStuff(context, false);

        this.modSelections.setScreenWidth(this.width);
        this.modSelections.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        checkAndSwapConfigs();
        this.configEntries.renderEntries(context, mouseX, mouseY, delta, this.width);
        renderDirtBGStuff(context, true);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 16777215);

        this.doneButton.render(context, mouseX, mouseY, delta);
        this.doneButton.setPosition(this.width / 2 - 75, this.height - 38);

        this.configEntries.renderEntryTooltips(this.textRenderer, context, mouseX, mouseY);
        renderScrollBar(context);
    }

    private void checkAndSwapConfigs() {
        String modID = this.modSelections.getSelectionModID();
        if (modID != null && !Objects.equals(this.currentModID, modID)) {
            this.currentModID = modID;
            this.scrollOffset = 0;
            this.configEntries.swapDisplayedConfigEntries(modID, this.width, this.textRenderer);
            clearAndInit();
        }
    }

    @Override
    public void close() {
        Class<?> configScreen = KoalaConfigCreator.MOD_CONFIGS.get(this.currentModID);
        this.configEntries.applyConfigValues(configScreen);
        KoalaConfigCreator.updateAllConfigFiles();
        super.close();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX < this.width / 2.0) {
            return false;
        }

        this.scrollOffset = (int) Math.max(0, Math.min(getMaxScroll(), this.scrollOffset - (amount * SCROLL_STEP)));
        this.configEntries.scrollEntries(this.scrollOffset);
        return true;
    }

    private int getMaxScroll() {
        int top = 34;
        int bottom = this.height - 61;
        int availableHeight = bottom - top;
        int visibleButtons = availableHeight / 25;
        return Math.max(0, (this.configEntries.getEntries().size() - visibleButtons) * 25);
    }

    private void renderScrollBar(DrawContext context) {
        int scrollBarHeight = (this.height - 63) - 34;

        if (this.getMaxScroll() > 0) {
            this.scrollBarX = (int) MathHelper.lerp(0.1, this.scrollBarX, this.width - 10);

            int scrollBarThumbHeight = Math.max(20, (scrollBarHeight * scrollBarHeight) / (scrollBarHeight + getMaxScroll()));
            int scrollThumbY = 34 + (this.scrollOffset * (scrollBarHeight - scrollBarThumbHeight) / getMaxScroll());

            context.fill(this.scrollBarX, 34, this.scrollBarX + 6, this.height - 61, -16777216);
            context.fill(this.scrollBarX, scrollThumbY, this.scrollBarX + 6, scrollThumbY + scrollBarThumbHeight, -8355712);
            context.fill(this.scrollBarX, scrollThumbY, this.scrollBarX + 5, scrollThumbY + scrollBarThumbHeight - 1, -4144960);
        }
    }

    private void renderDirtBGStuff(DrawContext context, boolean isShadow) {
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
}
