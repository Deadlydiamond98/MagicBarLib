package net.deadlydiamond98.magiclib.util;

/**
 * This is added to the Living Entity class, all methods here can be called by a Living Entity
 * <p>
 * example:
 *      public void doManaThingToEntity(LivingEntity entity) {
 *          entity.addMana(1);
 *      }
 */
public interface ManaEntityData {

    /**
     * Set the entity's Mana level, the mana shouldn't be less than 0, or greater than the max mana
     * @param value, mana amount
     */
    default void setMana(int value) {}

    /**
     * Get the entity's Current Mana level
     */
    default int getMana() {
        return 0;
    }
    /**
     * Set the entity's Max Mana level, the mana shouldn't be less than 1
     * @param value, max mana amount
     */
    default void setMaxMana(int value) {}
    /**
     * Get the entity's Maximum Mana Level
     */
    default int getMaxMana() {
        return 0;
    }


    /**
     * Adds Mana to entity, if the entity's mana isn't at the max level
     * If the amount of mana added to the entity is greater than the max level, but the entity doesn't have max mana, the entity's mana will become Max
     * If the amount of mana added is negative, removeMana() will be called
     *
     * @param amount, amount to add to the entity
     */
    default void addMana(int amount) {}
    /**
     * Removes Mana from the entity, if the entity's mana isn't 0 and applies a delay before regenerating mana again
     * If the amount of mana removed is greater than the entity's current mana, the mana won't be removed
     * If the amount of mana added is negative, addMana() will be called
     *
     * @param amount, amount to remove from the entity
     */
    default void removeMana(int amount) {}
    /**
     * Removes Mana from the entity, if the entity's mana isn't 0
     * If the amount of mana removed is greater than the entity's current mana, the mana won't be removed
     * If the amount of mana added is negative, addMana() will be called
     *
     * @param amount, amount to remove from the entity
     * @param addDelay, whether or no to add a delay before regenerating
     */
    default void removeMana(int amount, boolean addDelay) {}

    /**
     * Returns true if the entity's mana is less than max
     *
     * @param amountToGive, amount of mana you want to give the entity
     */
    default boolean canAddMana(int amountToGive) {return false;}
    /**
     * Returns true if the entity's mana is greater than or equal to amountToRemove
     * @param amountToRemove, amount of mana you want to give the entity
     */
    default boolean canRemoveMana(int amountToRemove) {return false;}

    /**
     *
     * @param amount amount of mana you want to add to the entity's maximum Mana
     * @param replenish whether the entity recovers mana that is equal to the max increase
     */
    default void increaseMaxMana(int amount, boolean replenish) {}

    /**
     * If the amount of max mana - the amount removed is less than 1, nothing will happen
     *
     * @param amount amount of mana to remove from the entity's maximum Mana
     */
    default void decreaseMaxMana(int amount) {}

    /**
     * Checks if the maximum amount of mana can be decreased
     * @param amount amount of mana you want to remove
     */
    default boolean canDecreaseMaxMana(int amount) {return false;}

    /**
     * if true, allows the entity to regenerate Mana
     */
    default boolean hasManaRegen() {return false;}

    /**
     * Toggle an entities ability to regenerate Mana
     * @param manaRegen
     */
    default void setManaRegen(boolean manaRegen) {}


















    /**
     * Doesn't do anything, only persists to not cause crashes with Legend of Steve 1.0.3.1
     */
    @Deprecated
    default void enableManaRegen(boolean regen, int tickPause, int amount) {}

    /**
     * The setWhenNeededRenderTime and getWhenNeededRenderTime should be left alone, they are purely for rendering the
     * Mana bar when it's set to only render when needed
     */
    default void setWhenNeededRenderTime(int whenNeededRenderTime) {}
    /**
     * The setWhenNeededRenderTime and getWhenNeededRenderTime should be left alone, they are purely for rendering the
     * Mana bar when it's set to only render when needed
     */
    default int getWhenNeededRenderTime() {return 0;}
}
