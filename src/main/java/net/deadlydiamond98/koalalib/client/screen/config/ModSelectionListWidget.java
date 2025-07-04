package net.deadlydiamond98.koalalib.client.screen.config;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * List of all Mod Config Files to select
 */
public class ModSelectionListWidget extends AlwaysSelectedEntryListWidget<ModSelectionListWidget.ModConfigSelectionEntry> {

    public static final Identifier DEFAULT_ICON = new Identifier(KoalaLib.MOD_ID, "textures/gui/config/default_icon.png");

    private boolean enabled = true;
    private int screenWidth;

    public ModSelectionListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);

        List<String> modIDs = new ArrayList<>();
        KoalaConfigCreator.MOD_CONFIGS.forEach((modID, aClass) -> modIDs.add(modID));

        modIDs.sort((o1, o2) -> {
            String name1 = Text.translatable(o1 + ".config.title").getString();
            String name2 = Text.translatable(o2 + ".config.title").getString();
            return name1.compareToIgnoreCase(name2);
        });

        modIDs.forEach(modID -> {
            ModConfigSelectionEntry configCategory = new ModConfigSelectionEntry(
                    client.textRenderer, modID
            );
            this.addEntry(configCategory);
        });

        setRenderBackground(false);
        setRenderHorizontalShadows(false);
        this.screenWidth = width;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (this.getSelectedOrNull() != null) {
            this.width = (int) MathHelper.lerp(0.1, this.width, this.screenWidth / 2.0);
        } else {
            this.width = (int) MathHelper.lerp(0.1, this.width, this.screenWidth);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.getSelectedOrNull() != null && mouseX > this.width) {
            return false;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public @Nullable String getSelectionModID() {
        ModConfigSelectionEntry entry = this.getSelectedOrNull();

        if (entry != null) {
            return entry.modID;
        }
        return null;
    }

    public void setScreenWidth(int width) {
        this.screenWidth = width;
    }

    public class ModConfigSelectionEntry extends AlwaysSelectedEntryListWidget.Entry<ModConfigSelectionEntry> {

        private final TextRenderer textRenderer;
        private final String modID;

        public ModConfigSelectionEntry(TextRenderer textRenderer, String modid) {
            this.textRenderer = textRenderer;
            this.modID = modid;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            // ICON
            context.drawTexture(
                    getIconTexture(),
                    ModSelectionListWidget.this.width / 2 - 100, y - 1,
                    0, 0, 16, 16, 16, 16
            );

            // TEXT
            context.drawTextWithShadow(
                    this.textRenderer,
                    getModTranslation(),
                    ModSelectionListWidget.this.width / 2 - 100 + 21,
                    y + 2,
                    16777215
            );
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                ModSelectionListWidget.this.setSelected(this);
            }
            return button == 0;
        }

        public Text getModTranslation() {
            return Text.translatable(this.modID + ".config.title");
        }

        public Identifier getIconTexture() {
            ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
            Identifier icon = new Identifier(this.modID, "icon.png");
            Optional<Resource> resource = resourceManager.getResource(icon);
            return resource.isPresent() ? icon : DEFAULT_ICON;
        }

        @Override
        public Text getNarration() {
            return Text.empty();
        }
    }
}
