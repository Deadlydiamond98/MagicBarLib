package net.deadlydiamond98.magiclib.items.consumers;

import net.deadlydiamond98.magiclib.items.MagicItemData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformationItem extends Item implements MagicItemData {
    protected final Map<Block, Block> blockConversionMap;
    protected final Map<EntityType,  EntityType> entityConversionMap;
    protected final List<EntityType> entityBlacklistConversionMap;
    protected final Map<Item,  Item> itemConversionMap;
    private final int manaCost;
    private final boolean consumed;
    private final int cooldown;
    private final EntityType defaultEntity;
    private final boolean hasDefault;
    private final int entityHealthThreshold;

    /**
     * The Transformation Item is used for transforming one thing to another, similar to Zelda's transformation powder
     *
     * @param manaCost,         The cost of mana from the item's usage
     * @param consumed,         Whether the item is consumed on use
     * @param cooldown,         Item use Cooldown, if any
     * @param entityHealthThreshold If an entity has greater than this much HP, it isn't convertable
     */
    public TransformationItem(Settings settings, int manaCost, boolean consumed, int cooldown, boolean hasDefault, EntityType defaultEntity, int entityHealthThreshold) {
        super(settings);
        this.manaCost = manaCost;
        this.consumed = consumed;
        this.cooldown = cooldown;
        this.hasDefault = hasDefault;
        this.defaultEntity = defaultEntity;
        this.entityHealthThreshold = entityHealthThreshold;

        this.blockConversionMap = new HashMap<>();
        initializeBlockConversions();
        this.entityConversionMap = new HashMap<>();
        initializeEntityConversions();
        this.itemConversionMap = new HashMap<>();
        initializeItemConversions();
        this.entityBlacklistConversionMap = new ArrayList<>();
        initializeEntityBlacklist();
    }

    /**
     * Anything Defined in here will be unabled to be transformed under any circumstance
     *
     */
    protected void initializeEntityBlacklist() {
    }

    /**
     * Anything Defined in here will be added to the Block Conversion Map, and used to transform Blocks
     * an example of this is: blockConversionMap.put(Blocks.GRASS_BLOCK, Blocks.MYCELIUM);
     *
     * This example will convert a Grass Block into a Mycelium Block
     */
    protected void initializeBlockConversions() {
    }
    /**
     * Anything Defined in here will be added to the Entity Conversion Map, and used to transform Entities
     * an example of this is: entityConversionMap.put(EntityType.ZOGLIN, EntityType.HOGLIN);
     *
     * This example will convert a Zoglin into a Hoglin. (Note that this only works for Living Entities)
     */
    protected void initializeEntityConversions() {
    }
    /**
     * Anything Defined in here will be added to the Item Conversion Map, and used to transform Item
     * an example of this is: itemConversionMap.put(Items.IRON_INGOT, Items.GOLD_INGOT);
     *
     * This example will convert a stack of Iron Ingots into a stack of Gold Ingots if right-clicked
     */
    protected void initializeItemConversions() {
    }

    /**
     * Do things once mana is consumed
     */
    protected void consumeMana(PlayerEntity user, ItemStack stack, World world) {
        user.removeMana(this.manaCost);
        if (this.consumed) {
            stack.decrement(1);
        }
        if (this.cooldown > 0) {
            user.getItemCooldownManager().set(this, this.cooldown);
        }
    }
    /**
     * Run when item can't use mana
     */
    protected void doNoManaEvent(PlayerEntity user, World world) {
    }


    /**
     * displays the Mana cost of the item in a tooltip (think sword damage for example)
     */
    @Override
    public int getManaCost() {
        return this.manaCost;
    }





    private static <T extends Comparable<T>> BlockState copyProperty(BlockState fromState, BlockState toState, Property<T> property) {
        return toState.with(property, fromState.get(property));
    }
    private <T extends Entity, U extends Entity> ActionResult convertEntity(EntityType<U> convertFrom, EntityType<T> converted, Entity entity, PlayerEntity user, ItemStack stack) {
        if (convertFrom.equals(entity.getType())) {
            World world = user.getWorld();

            Entity newEntity = converted.create(world);
            if (newEntity == null) {
                return ActionResult.FAIL;
            }

            newEntity.setPos(entity.getX(), entity.getY() + 0.01, entity.getZ());

            entity.discard();
            world.spawnEntity(newEntity);

            consumeMana(user, stack, world);

            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockPos offsetPos = pos.offset(context.getSide(), 1);
        BlockState state = world.getBlockState(pos);
        PlayerEntity user = context.getPlayer();

        if (user != null && (user.canRemoveMana(this.manaCost) || user.isCreative())) {
            Block replacementBlock = this.blockConversionMap.get(state.getBlock());
            if (replacementBlock != null) {
                BlockState newState = replacementBlock.getDefaultState();

                for (Property<?> property : state.getProperties()) {
                    if (newState.contains(property)) {
                        newState = copyProperty(state, newState, property);
                    }
                }

                world.setBlockState(pos, newState);

            }

            Box box = new Box(offsetPos);
            List<Entity> entities = world.getEntitiesByClass(Entity.class, box, entity -> entity instanceof ItemEntity);

            for (Entity entity : entities) {
                ItemEntity itemEntity = (ItemEntity) entity;
                if (itemConversionMap.containsKey(itemEntity.getStack().getItem())) {
                    Item newItem = itemConversionMap.get(itemEntity.getStack().getItem());

                    ItemStack item = new ItemStack(newItem);
                    item.setCount(itemEntity.getStack().getCount());
                    itemEntity.setStack(item);
                }
            }

            consumeMana(user, context.getStack(), world);
            return ActionResult.SUCCESS;
        }
        else {
            doNoManaEvent(user, user.getWorld());
        }
        return super.useOnBlock(context);
    }


    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.canRemoveMana(this.manaCost) || user.isCreative()) {
            EntityType replacementEntity = this.entityConversionMap.get(entity.getType());
            boolean blackListed = this.entityBlacklistConversionMap.contains(entity.getType());

            if (blackListed) {
                return super.useOnEntity(stack, user, entity, hand);
            }
            if (replacementEntity != null) {
                return convertEntity(entity.getType(), replacementEntity, entity, user, stack);
            }
            if (this.hasDefault && entity.getMaxHealth() <= this.entityHealthThreshold) {
                return convertEntity(entity.getType(), this.defaultEntity, entity, user, stack);
            }
        }
        else {
            doNoManaEvent(user, user.getWorld());
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
