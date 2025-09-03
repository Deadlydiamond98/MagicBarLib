package net.deadlydiamond98.koalalib.client.events;

import net.deadlydiamond98.koalalib.common.items.magic.IMagicItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class KoalaItemTooltipEvents {
    public static void register() {
        ItemTooltipCallback.EVENT.register(KoalaItemTooltipEvents::magicItemTooltip);
    }

    private static void magicItemTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
        if (stack.getItem() instanceof IMagicItem magicItem) {
            int manaCost = magicItem.getManaCost(stack);
            Text attributeText = Text.literal(" " + manaCost).append(Text.translatable("attribute.koalalib.magic_cost")).formatted(Formatting.DARK_GREEN);
            int insertIndex = findInsertIndex(lines);
            boolean hasMainHandText = lines.stream()
                    .anyMatch(text -> text.getString().equals(Text.translatable("item.modifiers.mainhand").getString()));
            if (!hasMainHandText) {
                Text mainHandText = Text.translatable("item.modifiers.mainhand").formatted(Formatting.GRAY);
                lines.add(insertIndex, Text.empty());
                insertIndex++;
                lines.add(insertIndex, mainHandText);
                insertIndex++;
            }
            lines.add(insertIndex, attributeText);
        }
    }

    private static int findInsertIndex(List<Text> lines) {
        int insertIndex = lines.size();
        for (int i = 0; i < lines.size(); i++) {
            String lineString = lines.get(i).getString();
            if (lineString.contains("Attack Speed") || lineString.contains("Attack Damage")) {
                insertIndex = i + 1;
            } else if (lineString.contains("NBT") || lineString.contains(":")) {
                insertIndex = Math.min(insertIndex, i);
            }
        }
        return insertIndex;
    }
}
