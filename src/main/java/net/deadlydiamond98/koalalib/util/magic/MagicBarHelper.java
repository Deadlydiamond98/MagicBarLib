package net.deadlydiamond98.koalalib.util.magic;

import net.deadlydiamond98.koalalib.util.mixindata.IMagicBarMixinData;
import net.minecraft.entity.LivingEntity;

/**
 * Helper Class used to modify the Mana and Max Mana of the Magic Bar, and other things related to Magic use.
 */
public class MagicBarHelper {

    /**
     * Add Mana to an entity
     * @param entity The entity to add Mana to
     * @param amount The amount of Mana to add to the player
     */
    public static void addMana(LivingEntity entity, int amount) {
        if (amount < 0) {
            removeMana(entity, amount * -1);
        }
        else {
            if (canAddMana(entity, amount)) {
                if (amount + getMana(entity) <= getMaxMana(entity)) {
                    setMana(entity, getMana(entity) + amount);
                }
                else if (getMana(entity) < getMaxMana(entity) && amount + getMana(entity) > getMaxMana(entity)) {
                    setMana(entity, getMaxMana(entity));
                }
            }
        }
    }

    /**
     * Remove Mana from an entity, this method will also add a delay to passive mana regeneration
     * @param entity The entity to remove Mana from
     * @param amount The amount of Mana to remove from the player
     */
    public static void removeMana(LivingEntity entity, int amount) {
        removeMana(entity, amount, true);
    }

    /**
     * Remove Mana from an entity, this method will also add a delay to passive mana regeneration
     * @param entity The entity to remove Mana from
     * @param amount The amount of Mana to remove from the player
     * @param addDelay Whether a delay should be applied to passive mana regen
     */
    public static void removeMana(LivingEntity entity, int amount, boolean addDelay) {
        if (amount < 0) {
            addMana(entity, amount * -1);
        }
        if (canRemoveMana(entity, amount)) {
            if (getMana(entity) - amount >= 0) {
                setMana(entity, getMana(entity) - amount);
            }
            else if (getMana(entity) > 0 && getMana(entity) - amount < 0) {
                setMana(entity, 0);
            }
            getBar(entity).koalalib$applyRegenDelay(addDelay);
        }
    }

    /**
     * Check if an amount of Mana can be added
     * @param entity The entity who you want to add mana to
     * @param amount The amount you want to check for adding
     * @return Whether the Mana could or couldn't be added
     */
    public static boolean canAddMana(LivingEntity entity, int amount) {
        if (amount + getMana(entity) <= getMaxMana(entity)) {
            return true;
        }
        return getMana(entity) < getMaxMana(entity) && amount + getMana(entity) > getMaxMana(entity);
    }

    /**
     * Check if an amount of Mana can be removed
     * @param entity The entity who you want to remove mana from
     * @param amount The amount you want to check for removing
     * @return Whether the Mana could or couldn't be removed
     */
    public static boolean canRemoveMana(LivingEntity entity, int amount) {
        return getMana(entity) - amount >= 0;
    }

    /**
     * Increase an entity's Maximum Mana
     * @param entity The entity who you want to add more max mana to
     * @param amount The amount of Mana that the Max Mana should increase by
     * @param replenish Whether mana the amount of mana that's given should also be added to the entity's current mana
     */
    public static void increaseMaxMana(LivingEntity entity, int amount, boolean replenish) {
        if (amount < 0) {
            decreaseMaxMana(entity, amount * -1);
        }
        else {
            setMaxMana(entity, getMaxMana(entity) + amount);
            if (replenish) {
                addMana(entity, amount);
            }
        }
    }

    /**
     * Decrease an entity's Maximum Mana
     * @param entity The entity who you want to remove max mana from
     * @param amount The amount of Mana that the Max Mana should decrease by
     */
    public static void decreaseMaxMana(LivingEntity entity, int amount) {
        if (amount < 0) {
            increaseMaxMana(entity, amount * -1, false);
        } else {
            if (canDecreaseMaxMana(entity, amount)) {
                setMaxMana(entity, getMaxMana(entity) - amount);
                if (getMaxMana(entity) < getMana(entity)) {
                    setMana(entity, getMaxMana(entity));
                }
            }
        }
    }

    /**
     * Check if an amount of Mana can be decreased (making sure the mana doesn't enter negative numbers)
     * @param entity The entity who you want to remove mana from
     * @param amount The amount you want to check for removing
     * @return Whether the Mana could or couldn't be removed
     */
    public static boolean canDecreaseMaxMana(LivingEntity entity, int amount) {
        return getMaxMana(entity) - amount > 0;
    }

    /**
     * Set the current amount of Mana an entity has
     * @param entity The entity who's having Mana modified
     * @param amount The amount of Mana the entity should have
     */
    public static void setMana(LivingEntity entity, int amount) {
        getBar(entity).koalalib$setMana(amount);
    }

    /**
     * Get the current amount of Mana an entity has
     * @param entity The entity whose Mana level you want to get
     */
    public static int getMana(LivingEntity entity) {
        return getBar(entity).koalalib$getMana();
    }

    /**
     * Set the maximum amount of Mana an entity can have
     * @param entity The entity who's having Mana modified
     * @param amount The maximum amount of Mana the entity should have
     */
    public static void setMaxMana(LivingEntity entity, int amount) {
        getBar(entity).koalalib$setMaxMana(amount);
    }

    /**
     * Get the maximum amount of Mana an entity can have
     * @param entity The entity whose max Mana level you want to get
     */
    public static int getMaxMana(LivingEntity entity) {
        return getBar(entity).koalalib$getMaxMana();
    }

    /**
     * Helper Method to get the MagicBar without constantly casting entity
     * @param entity The Living Entity who is having magic values changed
     */
    public static IMagicBarMixinData getBar(LivingEntity entity) {
        return (IMagicBarMixinData) entity;
    }
}
