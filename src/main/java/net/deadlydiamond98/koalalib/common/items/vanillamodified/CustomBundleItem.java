package net.deadlydiamond98.koalalib.common.items.vanillamodified;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.KoalaNbtHelper;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This Item is used for adding a custom bundle (with much more customizability than the vanilla bundle)
 */
public class CustomBundleItem extends Item {

    public final Predicate<ItemStack> stackPredicate;
    public final boolean insertOnPickup;
    public final int maxStorage;

    public CustomBundleItem(Item.Settings settings, int maxStorage, boolean insertOnPickup, TagKey<Item> acceptedItems) {
        this(settings, maxStorage, insertOnPickup, stack -> stack.isIn(acceptedItems));
    }

    public CustomBundleItem(Item.Settings settings, int maxStorage, boolean insertOnPickup, Predicate<ItemStack> stackPredicate) {
        super(settings);
        this.maxStorage = maxStorage;
        this.stackPredicate = stackPredicate;
        this.insertOnPickup = insertOnPickup;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType == ClickType.RIGHT) {
            ItemStack slotStack = slot.getStack();

            if (slotStack.isEmpty()) {
                ItemStack removedStack = CustomBundleItem.removeFromBundle(stack);
                if (!removedStack.isEmpty()) {
                    slot.setStack(removedStack);
                    playRemoveSound(player);
                }
            } else if (CustomBundleItem.addToBundle(stack, slotStack)) {
                playInsertSound(player);
            }
            return true;
        }

        return super.onStackClicked(stack, slot, clickType, player);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (otherStack.isEmpty()) {
                ItemStack removedStack = CustomBundleItem.removeFromBundle(stack);
                if (!removedStack.isEmpty()) {
                    playRemoveSound(player);
                    cursorStackReference.set(removedStack);
                }
            } else if (addToBundle(stack, otherStack)) {
                this.playInsertSound(player);
            }
            return true;
        }
        return false;
    }

    /**
     * If true, items will automatically enter the bag when picked up from the ground
     */
    public boolean canInsertOnPickup() {
        return this.insertOnPickup;
    }

    // ITEM ADDING AND REMOVING ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Adds an item to the bundle if possible
     * @param bundle the bundle
     * @param putStack the stack to insert
     * @return whether the stack is successfully inserted
     */
    public static boolean addToBundle(ItemStack bundle, ItemStack putStack) {
        if (bundle.getItem() instanceof CustomBundleItem bundleItem && bundleItem.canInsertItem(bundle, putStack)) {
            List<ItemStack> stacks = getItemStacks(bundle);
            int putMax = Math.min(putStack.getCount(), bundleItem.getMaxInsertables(bundle) / Math.max(1, bundleItem.getItemOccupancy(putStack)));

            if (putMax <= 0) {
                return false;
            }

            for (ItemStack bundleStack : stacks) {
                if (ItemStack.canCombine(bundleStack, putStack)) {
                    bundleStack.increment(putMax);
                    putStack.decrement(putMax);
                    putItemStacks(bundle, stacks);
                    return true;
                }
            }
            if (!putStack.isEmpty()) {
                ItemStack putStackCopy = new ItemStack(putStack.getItem(), putMax);
                putStackCopy.setNbt(putStack.getNbt());
                stacks.add(putStackCopy);
                putStack.decrement(putMax);
                putItemStacks(bundle, stacks);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the first stack from the bundle and updates the contents
     * @param bundle the bundle
     * @return returns the removed stack
     */
    public static ItemStack removeFromBundle(ItemStack bundle) {
        ItemStack stack = getFirstStack(bundle);
        return removeFromBundle(bundle, stack, stack.getCount(), true);
    }

    /**
     * Removes the first stack from the bundle
     * @param bundle the bundle
     * @param count the amount to be removed
     * @param updateContents whether the contents should be updated (such as when removing an item through creative usage)
     * @return returns the removed stack
     */
    public static ItemStack removeFromBundle(ItemStack bundle, int count, boolean updateContents) {
        return removeFromBundle(bundle, getFirstStack(bundle), count, updateContents);
    }

    /**
     * Removes an item from the bundle's inventory
     * @param bundle the bundle
     * @param getStack the type of stack to be retrieved
     * @param count the stack count
     * @param updateContents whether the contents should be updated (such as when removing an item through creative usage)
     * @return returns the removed stack
     */
    public static ItemStack removeFromBundle(ItemStack bundle, ItemStack getStack, int count, boolean updateContents) {
        count = Math.max(0, Math.min(count, getStack.getMaxCount()));
        if (bundle.getItem() instanceof CustomBundleItem) {
            List<ItemStack> stacks = getItemStacks(bundle);

            for (ItemStack bundleStack : stacks) {
                if (ItemStack.canCombine(bundleStack, getStack) && bundleStack.getCount() >= count) {
                    ItemStack removedStack = new ItemStack(bundleStack.getItem(), count);
                    removedStack.setNbt(bundleStack.getNbt());
                    bundleStack.decrement(count);
                    if (updateContents) {
                        putItemStacks(bundle, stacks);
                    }
                    return removedStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Returns the first stack in the bundle, or an empty item if none
     * @param bundle the bundle
     * @return the first stack
     */
    public static ItemStack getFirstStack(ItemStack bundle) {
        List<ItemStack> stacks = getItemStacks(bundle);
        if (!stacks.isEmpty()) {
            return stacks.get(0);
        }
        return ItemStack.EMPTY;
    }

    /**
     * Shifts the items in the bundle's inventory by 1
     * @param bundle the bundle
     */
    public static void cycleInventory(ItemStack bundle) {
        cycleInventory(bundle, 1);
    }

    /**
     * Shifts the Items in the bundle's inventory
     * @param bundle the bundle
     * @param shift the amount to shift by
     */
    public static void cycleInventory(ItemStack bundle, int shift) {
        List<ItemStack> stacks = getItemStacks(bundle);
        Collections.rotate(stacks, shift);
        putItemStacks(bundle, stacks);
    }

    /**
     * Checks if the bundle can be filled
     *
     * @param bundle the bundle
     * @param putStack the stack to attempt to insert
     * @return whether the bundle can be filled
     */
    public boolean canInsertItem(ItemStack bundle, ItemStack putStack) {
        if (!isFull(bundle) && putStack.getItem().canBeNested() && !(putStack.getItem() instanceof CustomBundleItem)) {
            return this.stackPredicate != null && this.stackPredicate.test(putStack);
        }
        return false;
    }

    public int getMaxInsertables(ItemStack bundle) {
        return this.maxStorage - getOccupancy(bundle);
    }

    public boolean isFull(ItemStack bundle) {
        return getOccupancy(bundle) >= this.maxStorage;
    }

    /**
     * Returns the amount of items inside the bundle
     * @param bundle the bundle
     * @return the item count
     */
    public int getOccupancy(ItemStack bundle) {
        int count = 0;
        for (ItemStack stack : getItemStacks(bundle)) {
            count += Math.min(this.maxStorage, Math.max(getItemOccupancy(stack), 1)) * stack.getCount();
        }
        return count;
    }

    /**
     * Returns the amount of space that an item will take up in a bundle
     * @param stack the stack to check
     * @return returns the space that the item will take up
     */
    public int getItemOccupancy(ItemStack stack) {
        return 1;
    }

    // NBT /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a List of stored Items in the bundle
     * @param bundle the bundle
     * @return the list of stored items
     */
    public static List<ItemStack> getItemStacks(ItemStack bundle) {
        NbtList nbtList = getOrCreateInventory(bundle);

        if (!nbtList.isEmpty()) {
            List<ItemStack> stacks = new ArrayList<>();
            for (NbtElement nbt : nbtList) {
                stacks.add(KoalaNbtHelper.largeItemStackFromNBT((NbtCompound) nbt));
            }
            return stacks;
        }
        return new ArrayList<>();
    }

    /**
     * Stores a List of ItemStacks in the bundle's inventory
     * @param bundle the bundle
     * @param stacks the stacks
     */
    public static void putItemStacks(ItemStack bundle, List<ItemStack> stacks) {
        NbtList nbtList = new NbtList();
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                nbtList.add(KoalaNbtHelper.largeItemStackToNBT(stack));
            }
        }
        putInventory(nbtList, bundle);
    }

    /**
     * Gets the nbt list from the bundle, or creates and returns one if none is present
     * @param bundle the bundle
     * @return the NBT list
     */
    protected static NbtList getOrCreateInventory(ItemStack bundle) {
        NbtList inventory = getInventory(bundle);
        if (inventory == null) {
            NbtList nbtList = new NbtList();
            putInventory(nbtList, bundle);
            return nbtList;
        }
        return inventory;
    }

    /**
     * Gets the nbt list from the bundle
     * @param bundle the bundle
     * @return the NBT list or null
     */
    @Nullable
    protected static NbtList getInventory(ItemStack bundle) {
        NbtCompound nbtCompound = bundle.getOrCreateNbt();
        if (nbtCompound.contains("Items")) {
            return nbtCompound.getList("Items", 10);
        }
        return null;
    }

    /**
     * Puts an ItemStack list into the bundle's nbt
     * @param nbtList the list of NBT to put
     * @param bundle the bundle
     */
    protected static void putInventory(NbtList nbtList, ItemStack bundle) {
        NbtCompound nbtCompound = bundle.getOrCreateNbt();
        nbtCompound.put("Items", nbtList);
    }


    // GUI /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.minecraft.bundle.fullness", getOccupancy(stack), this.maxStorage).formatted(Formatting.GRAY));
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> bundledItems = DefaultedList.of();
        bundledItems.addAll(getItemStacks(stack));
        return Optional.of(new BundleTooltipData(bundledItems, isFull(stack) ? 64 : 0));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return getOccupancy(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12 * getOccupancy(stack) / this.maxStorage, 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x6666FF;
    }

    // SFX /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void playRemoveSound(Entity entity) {
        playSound(entity, SoundEvents.ITEM_BUNDLE_REMOVE_ONE);
    }

    public void playInsertSound(Entity entity) {
        playSound(entity, SoundEvents.ITEM_BUNDLE_INSERT);
    }

    public void playDropContentsSound(Entity entity) {
        playSound(entity, SoundEvents.ITEM_BUNDLE_DROP_CONTENTS);
    }

    private void playSound(Entity entity, SoundEvent sound) {
        entity.playSound(sound, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f);
    }
}