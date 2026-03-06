package net.deadlydiamond98.koalalib.common.blocksets;

import net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs.CustomHangingSignBlock;
import net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs.CustomSignBlock;
import net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs.CustomWallHangingSignBlock;
import net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs.CustomWallSignBlock;
import net.deadlydiamond98.koalalib.util.datagen.BlockModelDatagenUtil;
import net.deadlydiamond98.koalalib.util.datagen.RecipeDatagenUtil;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.*;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WoodBlockset extends AbstractBlockset {

    public final BlockSetType blockSetType;
    public final WoodType woodType;

    private final boolean flammable;

    public final Block log;
    public final Block wood;
    public final Block strippedLog;
    public final Block strippedWood;

    public final Block plank;
    public final Block slab;
    public final Block stair;
    public final Block fence;
    public final Block gate;

    public final Block door;
    public final Block trapdoor;
    public final Block button;
    public final Block plate;

    public final Item signItem;
    public final Block sign;
    public final Block wallSign;

    public final Item hangingSignItem;
    public final Block hangingSign;
    public final Block wallHangingSign;

    public WoodBlockset(String modID, String type, AbstractBlock.Settings settings, BlockSetType blockSetTypeReference) {
        this(modID, type, settings, blockSetTypeReference, true);
    }

    public WoodBlockset(String modID, String type, AbstractBlock.Settings settings, BlockSetType blockSetTypeReference, boolean flammable) {
        super(modID, type);

        this.blockSetType = BlockSetTypeBuilder.copyOf(blockSetTypeReference).build(new Identifier(modID, type));
        this.woodType = new WoodType(type, this.blockSetType);

        // BLOCKS //////////////////////////////////////////////////////////////////////////////////////////////////////

        this.log = this.register(modID, this.id() + "_log", new PillarBlock(settings));
        this.wood = this.register(modID, this.id() + "_wood", new PillarBlock(settings));
        this.strippedLog = this.register(modID, "stripped_" + this.id() + "_log", new PillarBlock(settings));
        this.strippedWood = this.register(modID, "stripped_" + this.id() + "_wood", new PillarBlock(settings));

        this.plank = this.register(modID, this.id() + "_planks", new Block(settings));
        this.stair = this.register(modID, this.id() + "_stairs", new StairsBlock(this.plank.getDefaultState(), settings));
        this.slab = this.register(modID, this.id() + "_slab", new SlabBlock(settings));

        this.fence = this.register(modID, this.id() + "_fence", new FenceBlock(settings));
        this.gate = this.register(modID, this.id() + "_fence_gate", new FenceGateBlock(settings, this.woodType));

        this.door = this.register(modID, this.id() + "_door", new DoorBlock(FabricBlockSettings.copyOf(this.plank).nonOpaque(), this.blockSetType));
        this.trapdoor = this.register(modID, this.id() + "_trapdoor", new TrapdoorBlock(FabricBlockSettings.copyOf(this.plank).nonOpaque(), this.blockSetType));

        this.plate = this.register(modID, this.id() + "_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                FabricBlockSettings.copyOf(this.plank).noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY), this.blockSetType));
        this.button = this.register(modID, this.id() + "_button", new ButtonBlock(
                FabricBlockSettings.copyOf(this.plank).noCollision().strength(0.5F).pistonBehavior(PistonBehavior.DESTROY), this.blockSetType, 30, true));

        // SIGNS ///////////////////////////////////////////////////////////////////////////////////////////////////////

        Identifier signTexture = new Identifier(modID, "entity/signs/" + this.id());
        this.sign = this.registerNoItem(modID, this.id() + "_sign", new CustomSignBlock(FabricBlockSettings.copyOf(this.plank).noCollision().strength(1), this.woodType, signTexture));
        this.wallSign = this.registerNoItem(modID, this.id() + "_wall_sign", new CustomWallSignBlock(FabricBlockSettings.copyOf(this.plank).noCollision().strength(1).dropsLike(this.sign), this.woodType, signTexture));
        this.signItem = this.registerItem(new Identifier(modID, this.id() + "_sign"), new SignItem(new FabricItemSettings(), this.sign, this.wallSign));

        Identifier hangingSignTexture = new Identifier(modID, "entity/signs/hanging/" + this.id());
        this.hangingSign = this.registerNoItem(modID, this.id() + "_hanging_sign", new CustomHangingSignBlock(FabricBlockSettings.copyOf(this.plank).noCollision().strength(1), this.woodType, hangingSignTexture));
        this.wallHangingSign = this.registerNoItem(modID, this.id() + "_wall_hanging_sign", new CustomWallHangingSignBlock(FabricBlockSettings.copyOf(this.plank).noCollision().strength(1).dropsLike(this.sign), this.woodType, hangingSignTexture));
        this.hangingSignItem = this.registerItem(new Identifier(modID, this.id() + "_hanging_sign"), new HangingSignItem(this.hangingSign, this.wallHangingSign, new FabricItemSettings()));

        this.flammable = flammable;
        additionalWoodInit(flammable);
    }

    private void additionalWoodInit(boolean flammable) {
        StrippableBlockRegistry.register(this.log, this.strippedLog);
        StrippableBlockRegistry.register(this.wood, this.strippedWood);

        if (flammable) {
            FlammableBlockRegistry.getDefaultInstance().add(this.log, 5, 5);
            FlammableBlockRegistry.getDefaultInstance().add(this.wood, 5, 5);
            FlammableBlockRegistry.getDefaultInstance().add(this.strippedLog, 5, 5);
            FlammableBlockRegistry.getDefaultInstance().add(this.strippedWood, 5, 5);
            FlammableBlockRegistry.getDefaultInstance().add(this.plank, 5, 20);
            FlammableBlockRegistry.getDefaultInstance().add(this.slab, 5, 20);
            FlammableBlockRegistry.getDefaultInstance().add(this.stair, 5, 20);
            FlammableBlockRegistry.getDefaultInstance().add(this.fence, 5, 20);
            FlammableBlockRegistry.getDefaultInstance().add(this.gate, 5, 20);
        }
    }

    @Override
    protected void addAdditionalToCreative(ItemGroup.Entries entry) {
        super.addAdditionalToCreative(entry);
        entry.add(this.signItem);
        entry.add(this.hangingSignItem);
    }

    @Override
    public void generateModels(BlockStateModelGenerator modelGen, @Nullable AbstractBlockset.@Nullable SharedModel sharedModel) {
        modelGen.registerLog(this.log).log(this.log).wood(this.wood);
        modelGen.registerLog(this.strippedLog).log(this.strippedLog).wood(this.strippedWood);

        modelGen.registerSimpleCubeAll(this.plank);
        BlockModelDatagenUtil.registerSlab(modelGen, this.slab, this.plank);
        BlockModelDatagenUtil.registerStairs(modelGen, this.stair, this.plank);

        BlockModelDatagenUtil.registerButton(modelGen, this.button, this.plank);
        BlockModelDatagenUtil.registerPressurePlate(modelGen, this.plate, this.plank);

        BlockModelDatagenUtil.registerFence(modelGen, this.fence, this.plank);
        BlockModelDatagenUtil.registerFenceGate(modelGen, this.gate, this.plank);

        BlockModelDatagenUtil.registerSign(modelGen, this.sign, this.plank);
        BlockModelDatagenUtil.registerSign(modelGen, this.hangingSign, this.plank);

        modelGen.registerDoor(this.door);
        modelGen.registerOrientableTrapdoor(this.trapdoor);
    }

    @Override
    public void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        super.generateRecipes(exporter);

        RecipeProvider.offerShapelessRecipe(exporter, this.plank, this.log, RecipeCategory.BUILDING_BLOCKS.getName(), 4);
        RecipeProvider.offerShapelessRecipe(exporter, this.plank, this.wood, RecipeCategory.BUILDING_BLOCKS.getName(), 4);
        RecipeProvider.offerShapelessRecipe(exporter, this.plank, this.strippedLog, RecipeCategory.BUILDING_BLOCKS.getName(), 4);
        RecipeProvider.offerShapelessRecipe(exporter, this.plank, this.strippedWood, RecipeCategory.BUILDING_BLOCKS.getName(), 4);

        RecipeProvider.offerBarkBlockRecipe(exporter, this.wood, this.log);
        RecipeProvider.offerBarkBlockRecipe(exporter, this.strippedWood, this.strippedLog);

        RecipeProvider.offerSlabRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, this.slab, this.plank);
        RecipeDatagenUtil.createStairRecipe(exporter, this.stair, this.plank);

        RecipeDatagenUtil.createFenceRecipe(exporter, this.fence, this.plank);
        RecipeDatagenUtil.createGateRecipe(exporter, this.gate, this.plank);

        RecipeDatagenUtil.createDoorRecipe(exporter, this.door, this.plank);
        RecipeDatagenUtil.createTrapdoorRecipe(exporter, this.trapdoor, this.plank);

        RecipeProvider.offerPressurePlateRecipe(exporter, this.plate, this.plank);
        RecipeProvider.offerSingleOutputShapelessRecipe(exporter, this.button, this.plank, RecipeCategory.REDSTONE.getName());

        RecipeDatagenUtil.createSignRecipe(exporter, this.signItem, this.plank);
        RecipeDatagenUtil.createHangingSignRecipe(exporter, this.hangingSignItem, this.strippedLog);
    }

    @Override
    public void generateBlockTags(BiConsumer<TagKey<Block>, Block> tagConsumer, TagKey<Block>... mineableTags) {
        super.generateBlockTags(tagConsumer, mineableTags);

        if (this.flammable) {
            tagConsumer.accept(BlockTags.LOGS_THAT_BURN, this.log);
            tagConsumer.accept(BlockTags.LOGS_THAT_BURN, this.wood);
            tagConsumer.accept(BlockTags.LOGS_THAT_BURN, this.strippedLog);
            tagConsumer.accept(BlockTags.LOGS_THAT_BURN, this.strippedWood);
        } else {
            tagConsumer.accept(BlockTags.LOGS, this.log);
            tagConsumer.accept(BlockTags.LOGS, this.wood);
            tagConsumer.accept(BlockTags.LOGS, this.strippedLog);
            tagConsumer.accept(BlockTags.LOGS, this.strippedWood);
        }

        tagConsumer.accept(BlockTags.PLANKS, this.plank);
        tagConsumer.accept(BlockTags.WOODEN_SLABS, this.slab);
        tagConsumer.accept(BlockTags.WOODEN_STAIRS, this.stair);
        tagConsumer.accept(BlockTags.WOODEN_FENCES, this.fence);
        tagConsumer.accept(BlockTags.FENCE_GATES, this.gate);
        tagConsumer.accept(BlockTags.WOODEN_DOORS, this.door);
        tagConsumer.accept(BlockTags.TRAPDOORS, this.trapdoor);
        tagConsumer.accept(BlockTags.WOODEN_BUTTONS, this.button);
        tagConsumer.accept(BlockTags.WOODEN_PRESSURE_PLATES, this.plate);

        tagConsumer.accept(BlockTags.STANDING_SIGNS, this.sign);
        tagConsumer.accept(BlockTags.WALL_SIGNS, this.wallSign);

        tagConsumer.accept(BlockTags.CEILING_HANGING_SIGNS, this.hangingSign);
        tagConsumer.accept(BlockTags.WALL_HANGING_SIGNS, this.wallHangingSign);
    }

    @Override
    public void generateItemTags(BiConsumer<TagKey<Item>, ItemConvertible> tagConsumer) {
        super.generateItemTags(tagConsumer);

        if (this.flammable) {
            tagConsumer.accept(ItemTags.LOGS_THAT_BURN, this.log.asItem());
            tagConsumer.accept(ItemTags.LOGS_THAT_BURN, this.wood.asItem());
            tagConsumer.accept(ItemTags.LOGS_THAT_BURN, this.strippedLog.asItem());
            tagConsumer.accept(ItemTags.LOGS_THAT_BURN, this.strippedWood.asItem());
        } else {
            tagConsumer.accept(ItemTags.LOGS, this.log.asItem());
            tagConsumer.accept(ItemTags.LOGS, this.wood.asItem());
            tagConsumer.accept(ItemTags.LOGS, this.strippedLog.asItem());
            tagConsumer.accept(ItemTags.LOGS, this.strippedWood.asItem());

            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.log.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.wood.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.strippedLog.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.strippedWood.asItem());

            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.plank.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.slab.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.stair.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.fence.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.gate.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.door.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.trapdoor.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.button.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.plate.asItem());
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.signItem);
            tagConsumer.accept(ItemTags.NON_FLAMMABLE_WOOD, this.hangingSignItem);
        }

        tagConsumer.accept(ItemTags.PLANKS, this.plank.asItem());
        tagConsumer.accept(ItemTags.WOODEN_SLABS, this.slab.asItem());
        tagConsumer.accept(ItemTags.WOODEN_STAIRS, this.stair.asItem());
        tagConsumer.accept(ItemTags.WOODEN_FENCES, this.fence.asItem());
        tagConsumer.accept(ItemTags.FENCE_GATES, this.gate.asItem());
        tagConsumer.accept(ItemTags.WOODEN_DOORS, this.door.asItem());
        tagConsumer.accept(ItemTags.WOODEN_TRAPDOORS, this.trapdoor.asItem());
        tagConsumer.accept(ItemTags.WOODEN_BUTTONS, this.button.asItem());
        tagConsumer.accept(ItemTags.WOODEN_PRESSURE_PLATES, this.plate.asItem());
        tagConsumer.accept(ItemTags.SIGNS, this.signItem);
        tagConsumer.accept(ItemTags.HANGING_SIGNS, this.hangingSignItem);
    }
}
