package net.deadlydiamond98.koalalib.common.items.vanillamodified;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CustomShieldItem extends ShieldItem {

    private static final List<Pair<? extends CustomShieldItem, Integer>> SHIELD_DISABLING_COOLDOWNS = new ArrayList<>();

    /**
     * Class for adding Shields that can be disabled and damaged
     */
    public CustomShieldItem(Settings settings) {
        this(settings, 100);
    }

    /**
     * Class for adding Shields that can be disabled and damaged
     * @param disabledCooldown The amount of time to disable the shield for when it's disabled
     */
    public CustomShieldItem(Settings settings, int disabledCooldown) {
        super(settings);
        SHIELD_DISABLING_COOLDOWNS.add(new Pair<>(this, disabledCooldown));
    }

    /**
     * Returns a List of Damage types, Damage Types defined here won't damage your shield
     */
    public List<RegistryKey<DamageType>> ignoredDamageTypes() {
        return List.of();
    }

    /**
     * Runs when your shield is disabled
     */
    public void onShieldDisable(PlayerEntity user) {
    }

    /**
     * Used to determine whether your shield can or can't be disabled
     */
    public boolean canDisable(PlayerEntity user) {
        return true;
    }

    public static void attemptDamageSheild(PlayerEntity user, ItemStack activeStack, float amount, DamageSource source) {
        SHIELD_DISABLING_COOLDOWNS.forEach(integerPair -> {
            CustomShieldItem shield = integerPair.getLeft();

            if (activeStack.getItem().equals(shield)) {

                for (RegistryKey<DamageType> damageType : shield.ignoredDamageTypes()) {
                    if (source.isOf(damageType)) {
                        return;
                    }
                }
                shield.damageShield(user, amount);
            }
        });
    }

    public static void disableShield(PlayerEntity user, ItemCooldownManager itemCooldownManager) {
        SHIELD_DISABLING_COOLDOWNS.forEach(integerPair -> {
            CustomShieldItem shield = integerPair.getLeft();
            int cooldown = integerPair.getRight();
            if (shield.canDisable(user)) {
                itemCooldownManager.set(shield, cooldown);
            }
        });

        if (user.getActiveItem().getItem() instanceof CustomShieldItem sheild) {
            sheild.onShieldDisable(user);
        }
    }

    protected void damageShield(PlayerEntity user, float amount) {
        World world = user.getWorld();
        ItemStack stack = user.getActiveItem();

        if (world.isClient()) {
            user.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        }

        if (amount >= 3) {
            int i = 1 + MathHelper.floor(amount);
            Hand hand = user.getActiveHand();
            stack.damage(i, user, (player) -> player.sendToolBreakStatus(hand));

            if (stack.isEmpty()) {
                if (hand == Hand.MAIN_HAND) {
                    user.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    user.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                user.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
            }
        }
    }
}
