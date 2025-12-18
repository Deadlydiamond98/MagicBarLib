package net.deadlydiamond98.koalalib.client.screen.config;

import net.deadlydiamond98.koalalib.client.screen.config.entry.ConfigEntries;
import net.deadlydiamond98.koalalib.client.screen.config.entry.OldScreenPartsEntries;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Screen used for in-game config editing!
 */
public class KoalaConfigScreen extends GameOptionsScreen {
    public final OldScreenPartsEntries oldConfigEntries = new OldScreenPartsEntries();
    private final ConfigEntries configEntries = new ConfigEntries();

    private boolean firstInit;
    private ModSelectionListWidget modSelections;
    private ButtonWidget doneButton;
    private @Nullable String currentModID = null;
    private ConfigScrollBar scrollBar;

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
        this.scrollBar = new ConfigScrollBar(this.width + 490);

        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderDirtBGStuff(context, false);

        this.modSelections.setScreenWidth(this.width);
        this.modSelections.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        checkAndSwapConfigs();
        this.scrollBar.render(context, this.width, this.height, this.configEntries);
        this.oldConfigEntries.render(context, mouseX, mouseY, delta, width, height);
        this.configEntries.renderEntries(context, mouseX, mouseY, delta, this.width, this.height);
        renderDirtBGStuff(context, true);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 16777215);

        this.doneButton.render(context, mouseX, mouseY, delta);
        this.doneButton.setPosition(this.width / 2 - 75, this.height - 38);

        this.configEntries.renderEntryTooltips(this.textRenderer, context, mouseX, mouseY);
    }

    private void checkAndSwapConfigs() {
        String modID = this.modSelections.getSelectionModID();
        if (modID != null && !Objects.equals(this.currentModID, modID)) {
            this.oldConfigEntries.getEntries().addAll(this.configEntries.getEntries());
            this.oldConfigEntries.scrollBars.add(this.scrollBar);

            this.configEntries.swapDisplayedConfigEntries(this.currentModID, modID, this.width, this.textRenderer);
            this.currentModID = modID;
            clearAndInit();
        }
    }

    @Override
    public void close() {
        if (this.currentModID != null) {
            Class<?> configScreen = KoalaConfigCreator.MOD_CONFIGS.get(this.currentModID).getA();
            this.configEntries.applyConfigValues(configScreen);
        }
        KoalaConfigCreator.updateAllConfigFiles();
        super.close();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX > this.width / 2.0 || this.modSelections.getMaxScroll() <= 0) {
            this.configEntries.scrollEntries(this.scrollBar.scroll(amount, this.height, this.configEntries));
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
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
