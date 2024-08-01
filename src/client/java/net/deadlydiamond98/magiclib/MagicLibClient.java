package net.deadlydiamond98.magiclib;

import net.deadlydiamond98.magiclib.items.MagicItemData;
import net.deadlydiamond98.magiclib.renderer.ManaHudOverlay;
import net.deadlydiamond98.magiclib.networking.ZeldaClientPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class MagicLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ZeldaClientPackets.registerC2SPackets();
		HudRenderCallback.EVENT.register(new ManaHudOverlay());
		addMagicItemToolTips();
	}

	private void addMagicItemToolTips() {
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (stack.getItem() instanceof MagicItemData magicItem) {

				int manaCost = magicItem.getManaCost();
				Text attributeText = Text.literal(" " + manaCost).append(Text.translatable("attribute.magiclib.magic_cost")).formatted(Formatting.DARK_GREEN);
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
		});
	}

	private int findInsertIndex(List<Text> lines) {
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