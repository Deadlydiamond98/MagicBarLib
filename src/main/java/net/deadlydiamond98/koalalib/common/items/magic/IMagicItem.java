package net.deadlydiamond98.koalalib.common.items.magic;

import net.deadlydiamond98.koalalib.util.magic.MagicBarHelper;
import net.deadlydiamond98.koalalib.util.magic.MagicCostModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public interface IMagicItem extends IShowsMagicBar {

    /**
     * The Base Magic cost of the item, this is what the default cost will be
     * without modifications via things such as Enchantments
     * @param player the player
     * @param stack the item
     * @return the base magic cost
     */
    int getBaseMagicCost(PlayerEntity player, ItemStack stack);

    /**
     * This returns the final Magic Cost of the item with Enchantments that could modify the cost
     * @param player the player
     * @param stack the item
     * @return the Magic Cost for the item
     */
    default int getMagicCost(PlayerEntity player, ItemStack stack) {
        MagicCostModifier modifier = new MagicCostModifier();
        modifier.applyEnchantments(stack);
        return modifier.calculate(getBaseMagicCost(player, stack));
    }

    /**
     * If the Player is in creative or has enough Magic, this will return True, and consume the Magic if the player isn't in creative.
     * If this fails, onMagicActionFailed() will run.
     * @param player the player
     * @param stack the item
     * @return whether the player has enough magic or is in creative
     */
    default boolean hasEnoughMagic(PlayerEntity player, ItemStack stack) {
        return hasEnoughMagic(player, stack, true);
    }

    /**
     * If the player has enough Magic, this will return True, and consume the Magic.
     * If this fails, onMagicActionFailed() will run.
     * @param player the player
     * @param stack the item
     * @param checkCreative if true, this will always return true if the player is in Creative
     * @return whether the player has enough magic
     */
    default boolean hasEnoughMagic(PlayerEntity player, ItemStack stack, boolean checkCreative) {
        boolean isCreative = player.isCreative() && checkCreative;
        if (isCreative || MagicBarHelper.removeMana(player, getMagicCost(player, stack))) {
            return true;
        }
        onMagicActionFailed(player, stack);
        return false;
    }

    /**
     * This method is run whenever hasEnoughMagic() fails
     * @param player the player
     * @param stack the item
     */
    default void onMagicActionFailed(PlayerEntity player, ItemStack stack) {}

    // TOOTIP //////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Allows for a tooltip to be shown displaying the Magic Cost of the Item in a
     * Similar way to Attack Damage or Protection. This is only run on the Client Side
     * @param player the player
     * @param stack the item
     * @return whether the tooltip should be present
     */
    default boolean showTooltip(PlayerEntity player, ItemStack stack) {
        return true;
    }

    /**
     * This returns the Translation Key for the item's modifiers (ex: "When in Main Hand:")
     * @param player the player
     * @param stack the stack
     * @return the Translation Key for the item's modifiers
     */
    default String getTitleLangKey(PlayerEntity player, ItemStack stack) {
        String base = "item.modifiers.";
        if (stack.getItem() instanceof ArmorItem armor) {
            return switch (armor.getType()) {
                case HELMET -> base + "head";
                case LEGGINGS -> base + "legs";
                case BOOTS -> base + "feet";
                default -> base + "chest";
            };
        }
        return base + "mainhand";
    }

    /**
     * Returns the Text for the Magic Cost
     * @param player the player
     * @param stack the stack
     * @return the Text used for the Magic Cost
     */
    default Text getMagicCostText(PlayerEntity player, ItemStack stack) {
        Formatting formatting = stack.getItem() instanceof ArmorItem ? Formatting.BLUE : Formatting.DARK_GREEN;
        Text magicCostText =  Text.translatable("attribute.koalalib.magic_cost", getMagicCost(player, stack));
        return ScreenTexts.space().append(magicCostText).formatted(formatting);
    }

    /**
     * Determines Where the Magic Cost tooltip is inserted on the Item, can be overridden if the tooltip appears in the wrong position
     * @param player the Player
     * @param stack the Item
     * @param titleIndex the index of the title text
     * @param maxIndex the max Index
     * @param debugEnabled whether item debug (F3 + H) is active
     * @return returns the Position that the Magic Cost will appear in the Item's tooltips
     */
    default int insertionStartIndex(PlayerEntity player, ItemStack stack, int titleIndex, int maxIndex, boolean debugEnabled) {
        if (titleIndex != -1) {
            int index = titleIndex + 3;
            if (stack.getItem() instanceof ArmorItem armor) {
                index += armor.getMaterial().getKnockbackResistance() > 0 ? 1 : 0;
            }
            return index;
        }
        // If no title is currently present, the index will be at the end of the tooltips (but before debug info)
        return Math.max(0, maxIndex - (debugEnabled ? (stack.hasNbt() ? 2 : 1) : 0));
    }

    @Deprecated(forRemoval = true)
    default int getManaCost() {
        return 0;
    }
}
