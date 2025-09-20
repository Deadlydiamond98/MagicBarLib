package net.deadlydiamond98.koalalib.client.events;

import net.deadlydiamond98.koalalib.common.items.magic.IMagicItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class KoalaItemTooltipEvents {

    private static final Text MAINHAND_TITLE = Text.translatable("item.modifiers.mainhand").formatted(Formatting.GRAY);
    private static int index = 1;

    public static void register() {
        ItemTooltipCallback.EVENT.register(KoalaItemTooltipEvents::magicItemTooltip);
    }

    private static void magicItemTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
        if (stack.getItem() instanceof IMagicItem magicItem && magicItem.showTooltip(stack)) {

            int mana = magicItem.getManaCost(stack);

            if (lines.stream().noneMatch(text -> text.getString().equals(MAINHAND_TITLE.getString()))) {
                insertLine(lines, ScreenTexts.EMPTY);
                insertLine(lines, MAINHAND_TITLE);
            } else {
                for (int i = 1; i < lines.size(); i++) {
                    if (lines.get(i).contains(Text.translatable("attribute.name.generic.attack_speed"))) {
                        index = i + 1;
                        break;
                    }
                }
            }
            insertLine(lines, ScreenTexts.space().append(Text.translatable("attribute.koalalib.magic_cost", mana).formatted(Formatting.DARK_GREEN)));
            index = 1;
        }
    }

    private static void insertLine(List<Text> lines, Text text) {
        lines.add(index++, text);
    }
}
