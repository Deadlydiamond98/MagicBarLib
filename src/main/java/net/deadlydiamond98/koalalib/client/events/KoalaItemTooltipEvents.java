package net.deadlydiamond98.koalalib.client.events;

import net.deadlydiamond98.koalalib.common.items.magic.IMagicItem;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class KoalaItemTooltipEvents {

    public static void register() {
        ItemTooltipCallback.EVENT.register(KoalaItemTooltipEvents::magicItemTooltip);
    }

    private static void magicItemTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
        MinecraftClient client = MinecraftClient.getInstance();
        boolean debug = client.options.advancedItemTooltips;
        PlayerEntity player = client.player;

        if (stack.getItem() instanceof IMagicItem item && item.showTooltip(player, stack)) {
            Text title = Text.translatable(item.getTitleLangKey(player, stack)).formatted(Formatting.GRAY);
            Text costText = item.getMagicCostText(player, stack);

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).toString().equals(title.toString())) {
                    int index = item.insertionStartIndex(player, stack, i, lines.size(), debug);
                    lines.add(index, costText);
                    return;
                }
            }

            int index = item.insertionStartIndex(player, stack, -1, lines.size(), debug);
            lines.add(index++, ScreenTexts.EMPTY);
            lines.add(index++, title);
            lines.add(index, costText);
        }
    }
}
