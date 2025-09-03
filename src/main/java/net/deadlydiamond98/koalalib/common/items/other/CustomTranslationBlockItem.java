package net.deadlydiamond98.koalalib.common.items.other;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * Used to give a blockItem a different translation key than the block
 */
public class CustomTranslationBlockItem extends BlockItem {

    private final MutableText name;

    public CustomTranslationBlockItem(Block block, Settings settings, MutableText name) {
        super(block, settings);
        this.name = name;
    }

    @Override
    public Text getName(ItemStack stack) {
        return this.name;
    }
}
