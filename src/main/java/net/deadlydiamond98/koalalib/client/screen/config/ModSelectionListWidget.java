package net.deadlydiamond98.koalalib.client.screen.config;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.config.KoalaConfigCreator;
import net.deadlydiamond98.koalalib.config.configs.MainConfigs;
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
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * List of all Mod Config Files to select
 */
public class ModSelectionListWidget extends AlwaysSelectedEntryListWidget<ModSelectionListWidget.ModConfigSelectionEntry> {

    public static final Identifier DEFAULT_ICON = new Identifier(KoalaLib.MOD_ID, "textures/gui/config/default_icon.png");
    private int screenWidth;

    public ModSelectionListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);

        List<Pair<String, String>> modIDs = new ArrayList<>();
        List<Pair<String, String>> modCategories = new ArrayList<>();
        KoalaConfigCreator.MOD_CONFIGS.forEach((modID, pair) -> {
            if (pair.getB().isEmpty()) {
                modIDs.add(new Pair<>(modID, ""));
            } else {
                modCategories.add(new Pair<>(modID, pair.getB()));
            }
        });

        modIDs.sort((o1, o2) -> {
            String name1 = Text.translatable(o1.getA() + ".config.category").getString();
            String name2 = Text.translatable(o2.getA() + ".config.category").getString();
            return name1.compareToIgnoreCase(name2);
        });

        modCategories.sort((o2, o1) -> {
            String name1 = Text.translatable(o1.getA() + ".config.category").getString();
            String name2 = Text.translatable(o2.getA() + ".config.category").getString();
            return name1.compareToIgnoreCase(name2);
        });

        for (int i = modIDs.size() - 1; i >= 0; i--) {
            int index = i;
            modCategories.forEach(pair -> {
                if (modIDs.get(index).getA().equals(pair.getB())) {
                    modIDs.add(index + 1, pair);
                }
            });
        }

        modIDs.forEach(modID -> {
            ModConfigSelectionEntry configCategory = new ModConfigSelectionEntry(
                    client.textRenderer, modID.getA(), modID.getB()
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
            if (MainConfigs.fancyTransitions) {
                this.width = (int) MathHelper.lerp(0.1, this.width, this.screenWidth / 2.0);
            } else {
                this.width = (int) (this.screenWidth / 2.0);
            }
        } else {
            if (MainConfigs.fancyTransitions) {
                this.width = (int) MathHelper.lerp(0.1, this.width, this.screenWidth);
            } else {
                this.width = this.screenWidth;
            }
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
        private final boolean isCategory;

        public ModConfigSelectionEntry(TextRenderer textRenderer, String modid, String category) {
            this.textRenderer = textRenderer;
            this.modID = modid;
            this.isCategory = !category.isEmpty();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            // ICON
            if (!this.isCategory) {
                context.drawTexture(
                        getIconTexture(),
                        ModSelectionListWidget.this.width / 2 - 100, y - 1,
                        0, 0, 16, 16, 16, 16
                );
            }

            // TEXT
            context.drawTextWithShadow(
                    this.textRenderer,
                    this.isCategory ? Text.literal(" • ").append(getModTranslation()) : getModTranslation(),
                    ModSelectionListWidget.this.width / 2 - 100 + 21,
                    y + 2,
                    this.isCategory ? 0xBDBDBD : 0xFFFFFF
            );
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                ModSelectionListWidget.this.setSelected(this);
            }
            return button == 0;
        }

        public Text getModTranslation() {
            return Text.translatable(this.modID + ".config.category");
        }

        public Identifier getIconTexture() {
            ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
            Identifier icon = new Identifier(this.modID.substring(0, this.modID.length() - 5), "icon.png");
            Optional<Resource> resource = resourceManager.getResource(icon);
            return resource.isPresent() ? icon : DEFAULT_ICON;
        }

        @Override
        public Text getNarration() {
            return Text.empty();
        }
    }
}
