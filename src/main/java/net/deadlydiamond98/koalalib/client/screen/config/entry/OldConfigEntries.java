package net.deadlydiamond98.koalalib.client.screen.config.entry;

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
public class OldConfigEntries {
    private final List<ClickableWidget> entries = new ArrayList<>();

    public List<ClickableWidget> getEntries() {
        return this.entries;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta, int width) {
        for (int i = this.entries.size() - 1; i > 0; i--) {
            ClickableWidget element = this.entries.get(i);

            element.render(context, mouseX, mouseY, delta);
            element.setX((int) MathHelper.lerp(0.1, element.getX(), width + 100));

            if (element.getX() > width + 50) {
                this.entries.remove(i);
            }
        }
    }
}
