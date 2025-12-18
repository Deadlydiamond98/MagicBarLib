package net.deadlydiamond98.koalalib.util;

import net.deadlydiamond98.koalalib.KoalaLib;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Deprecated(forRemoval = true)
public class RegistryHelper {

    public static class Blocks {
        /**
         * Registers a block, with an optional Item
         * @param id the Identifier for the block
         * @param block the block
         * @param withItem whether there should be an item
         * @return returns the registered block
         */
        public static Block register(Identifier id, Block block, boolean withItem) {
            if (withItem) {
                Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()));
            }
            return Registry.register(Registries.BLOCK, id, block);
        }
    }

    public static class Entities {
        /**
         * Creates a Fabric Entity Type Builder, albeit with significantly less typing!
         * @param entityClass entity's class
         * @param spawnGroup the spawn group
         * @param width width of bounding box
         * @param height height of bounding box
         */
        public static <T extends net.minecraft.entity.Entity> FabricEntityTypeBuilder<T> create(Class<T> entityClass, SpawnGroup spawnGroup, float width, float height) {
            return FabricEntityTypeBuilder.create(spawnGroup, factory(entityClass)).dimensions(EntityDimensions.fixed(width, height));
        }

        /**
         * Registration Method
         * @param id Identifier for the Entity Type
         * @param builder the FabricEntityTypeBuilder, in this case the method create() should be used
         */
        public static <T extends net.minecraft.entity.Entity> EntityType<T> register(Identifier id, FabricEntityTypeBuilder<T> builder) {
            return Registry.register(Registries.ENTITY_TYPE, id, builder.build());
        }

        /**
         * Creates an instance of an Entity to be used for FabricEntityTypeBuilder
         */
        private static <T extends net.minecraft.entity.Entity> EntityType.EntityFactory<T> factory(Class<T> entityClass) {
            return (EntityType<T> type, World world) -> {
                try {
                    return entityClass.getConstructor(EntityType.class, World.class).newInstance(type, world);
                } catch (Exception ignored) {
                    return null;
                }
            };
        }
    }
}
