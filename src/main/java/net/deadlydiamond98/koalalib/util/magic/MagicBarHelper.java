package net.deadlydiamond98.koalalib.util.magic;

import net.deadlydiamond98.koalalib.init.KoalaLibEntityAttributes;
import net.deadlydiamond98.koalalib.util.mixinterfaces.IMagicBarData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.UUID;

/**
 * Helper Class used to modify the Mana and Max Mana of the Magic Bar, and other things related to Magic use.
 */
public class MagicBarHelper {

    // REGULAR MANA VALUES /////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add Mana to an entity
     * @param entity The entity to add Mana to
     * @param amount The amount of Mana to add to the player
     */
    public static boolean addMana(LivingEntity entity, int amount) {
        if (amount < 0) {
            return removeMana(entity, amount * -1);
        }  else if (canAddMana(entity, amount)) {
            if (amount + getMana(entity) <= getMaxMana(entity)) {
                setMana(entity, getMana(entity) + amount);
            }
            else if (getMana(entity) < getMaxMana(entity) && amount + getMana(entity) > getMaxMana(entity)) {
                setMana(entity, getMaxMana(entity));
            }
            return true;
        }
        return canAddMana(entity, amount);
    }

    /**
     * Remove Mana from an entity, this method will also add a delay to passive mana regeneration
     * @param entity The entity to remove Mana from
     * @param amount The amount of Mana to remove from the player
     */
    public static boolean removeMana(LivingEntity entity, int amount) {
        return removeMana(entity, amount, true);
    }

    /**
     * Remove Mana from an entity, this method will also add a delay to passive mana regeneration
     * @param entity The entity to remove Mana from
     * @param amount The amount of Mana to remove from the player
     * @param addDelay Whether a delay should be applied to passive mana regen
     */
    public static boolean removeMana(LivingEntity entity, int amount, boolean addDelay) {
        if (amount < 0) {
            return addMana(entity, amount * -1);
        }
        else if (canRemoveMana(entity, amount)) {
            if (getMana(entity) - amount >= 0) {
                setMana(entity, getMana(entity) - amount);
            }
            else if (getMana(entity) > 0 && getMana(entity) - amount < 0) {
                setMana(entity, 0);
            }
            getBar(entity).koalalib$applyRegenDelay(addDelay);
            return true;
        }
        return canRemoveMana(entity, amount);
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

    // MAX MANA VALUES /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns an Entity's Max Mana based on the GENERIC_MAX_MAGIC Attribute
     * @param entity the Entity
     */
    public static int getMaxMana(LivingEntity entity) {
        return (int) entity.getAttributeValue(KoalaLibEntityAttributes.GENERIC_MAX_MAGIC);
    }

    /**
     * Returns the Base Value of an Entity's Max Mana without Attribute Modifiers
     * @param entity the Entity
     */
    public static int getUnmodifiedMaxMana(LivingEntity entity) {
        return (int) entity.getAttributeBaseValue(KoalaLibEntityAttributes.GENERIC_MAX_MAGIC);
    }

    /**
     * Applies an Attribute Modifier to an Entity's Max Mana
     * @param uuid the Modifier UUID
     * @param name the Modifier name
     * @param entity the Entity
     * @param amount the Amount of Magic added
     */
    public static void applyMaxManaModifier(UUID uuid, String name, LivingEntity entity, int amount) {
        EntityAttributeInstance attribute = entity.getAttributeInstance(KoalaLibEntityAttributes.GENERIC_MAX_MAGIC);
        if (attribute != null) {
            removeMaxManaModifier(uuid, entity);
            EntityAttributeModifier modifier = new EntityAttributeModifier(uuid, name, amount, EntityAttributeModifier.Operation.ADDITION);
            attribute.addPersistentModifier(modifier);
        }
    }

    /**
     * Removes an Attribute Modifier from the Entity's Max Mana
     * @param uuid the Modifier UUID
     * @param entity the Entity
     */
    public static void removeMaxManaModifier(UUID uuid, LivingEntity entity) {
        EntityAttributeInstance attribute = entity.getAttributeInstance(KoalaLibEntityAttributes.GENERIC_MAX_MAGIC);
        if (attribute != null) {
            if (attribute.getModifier(uuid) != null) {
                attribute.removeModifier(uuid);
            }
        }
    }

    // MANA REGEN //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if an entity can passively regenerate Mana
     * @param entity the Entity
     */
    public static boolean canRegenerateMana(LivingEntity entity) {
        return getBar(entity).koalalib$isManaRegenEnabled();
    }

    /**
     * Determines if an entity can passively regenerate Mana
     * @param entity the Entity
     * @param bl allow Passive Magic Regen
     */
    public static void enableManaRegen(LivingEntity entity, boolean bl) {
        getBar(entity).koalalib$setManaRegenAbility(bl);
    }

    /**
     * Setting the value here changes if Passive Mana regen requires full hunger
     * @param entity the Entity
     * @param bl stop Regen if not full Hunger
     */
    public static void requiteFullHungerForPassiveRegen(LivingEntity entity, boolean bl) {
        getBar(entity).koalalib$requireFullHungerForMagicRegen(bl);
    }

    /**
     * Gets the Cap for Passive Mana Regen
     * @param entity the Entity
     */
    public static int getManaRegenCap(LivingEntity entity) {
        return getBar(entity).koalalib$getMagicRegenCap();
    }

    /**
     * Set the Maximum amount of Mana that can be restored via passive Regen
     * @param entity the Entity
     * @param cap the Max amount of Mana before Regen stops
     */
    public static void setManaRegenCap(LivingEntity entity, int cap) {
        getBar(entity).koalalib$setMagicRegenCap(cap);
    }

    // OTHER ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper Method to get the MagicBar without constantly casting entity
     * @param entity The Living Entity who is having magic values changed
     */
    public static IMagicBarData getBar(LivingEntity entity) {
        return (IMagicBarData) entity;
    }

}
