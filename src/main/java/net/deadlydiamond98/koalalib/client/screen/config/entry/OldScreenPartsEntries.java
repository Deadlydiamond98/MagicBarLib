package net.deadlydiamond98.koalalib.client.screen.config.entry;

import net.deadlydiamond98.koalalib.client.screen.config.ConfigScrollBar;
import net.deadlydiamond98.koalalib.config.configs.MainConfigs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.util.math.MathHelper;
import net.deadlydiamond98.koalalib.client.screen.config.KoalaConfigScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains config entries that are being swiped away. <br>
 * Moved to its own class to make things less cluttered in {@link KoalaConfigScreen}.
 */
public class OldScreenPartsEntries {
    public final List<ClickableWidget> entries = new ArrayList<>();
    public final List<ConfigScrollBar> scrollBars = new ArrayList<>();

    public List<ClickableWidget> getEntries() {
        return this.entries;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {

        if (MainConfigs.fancyTransitions) {
            for (int i = this.entries.size() - 1; i >= 0; i--) {
                ClickableWidget element = this.entries.get(i);

                element.render(context, mouseX, mouseY, delta);
                element.setX((int) MathHelper.lerp(0.1, element.getX(), width + 400));

                if (element.getX() > width + 200) {
                    this.entries.remove(i);
                }
            }

            for (int i = this.scrollBars.size() - 1; i >= 0; i--) {
                ConfigScrollBar element = this.scrollBars.get(i);

                element.renderOld(context, width, height);
                element.scrollBarX = (int) MathHelper.lerp(0.1, element.scrollBarX, width + 400);

                if (element.scrollBarX > width + 200) {
                    this.scrollBars.remove(i);
                }
            }
        } else {
            this.entries.clear();
            this.scrollBars.clear();
        }
    }
}
