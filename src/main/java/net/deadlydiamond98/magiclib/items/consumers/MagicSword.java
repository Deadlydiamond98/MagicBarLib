package net.deadlydiamond98.magiclib.items.consumers;

import net.deadlydiamond98.magiclib.items.MagicItemData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class MagicSword extends SwordItem implements MagicItemData {

    private final int manaCost;
    /**
     * @param manaCost, The cost of mana from the item's usage
     */
    public MagicSword(Settings settings, ToolMaterial toolMaterial, int attackDamage, float attackSpeed, int manaCost) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.manaCost = manaCost;
    }
    /**
     * displays the Mana cost of the item in a tooltip (think sword damage for example)
     */
    protected void doManaAction(PlayerEntity user) {
        user.removeMana(this.manaCost);
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }
}
