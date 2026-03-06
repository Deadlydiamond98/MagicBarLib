package net.deadlydiamond98.koalalib.common.blocksets;

import net.deadlydiamond98.koalalib.util.datagen.BlockModelDatagenUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StoneBlockset extends BaseStairSlabWallBlockset {

    public final BlockSetType blockSetType;

    public final Block button;
    public final Block plate;

    public StoneBlockset(String modID, String id, AbstractBlock.Settings settings) {
        this(modID, id, settings, BlockSetType.STONE);
    }

    public StoneBlockset(String modID, String id, AbstractBlock.Settings settings, BlockSetType blockSetTypeReference) {
        this(modID, id, settings, blockSetTypeReference, true);
    }

    public StoneBlockset(String modID, String id, AbstractBlock.Settings settings, BlockSetType blockSetTypeReference, boolean stripEndS) {
        super(modID, id, settings, stripEndS);

        this.blockSetType = BlockSetTypeBuilder.copyOf(blockSetTypeReference).build(new Identifier(modID, id));

        this.button = this.register(modID, this.id() + "_button", new ButtonBlock(
                FabricBlockSettings.copyOf(this.base).noCollision().strength(0.5f).pistonBehavior(PistonBehavior.DESTROY),
                this.blockSetType, 20, false
        ));

        this.plate = this.register(modID, this.id() + "_pressure_plate", new PressurePlateBlock(
                PressurePlateBlock.ActivationRule.MOBS,
                FabricBlockSettings.copyOf(this.base).noCollision().strength(0.5f).pistonBehavior(PistonBehavior.DESTROY),
                this.blockSetType
        ));
    }

    @Override
    public void generateModels(BlockStateModelGenerator modelGen, boolean uniqueSlab) {
        super.generateModels(modelGen, uniqueSlab);
        BlockModelDatagenUtil.registerButton(modelGen, this.button, this.base);
        BlockModelDatagenUtil.registerPressurePlate(modelGen, this.plate, this.base);
    }

    @Override
    public void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        super.generateRecipes(exporter);
        RecipeProvider.offerPressurePlateRecipe(exporter, this.plate, this.base);
        RecipeProvider.offerSingleOutputShapelessRecipe(exporter, this.button, this.base, RecipeCategory.REDSTONE.getName());
    }

    @Override
    protected void stoneCutterRecipes(Consumer<RecipeJsonProvider> exporter, Block block) {
        super.stoneCutterRecipes(exporter, block);
        RecipeProvider.offerStonecuttingRecipe(exporter, RecipeCategory.REDSTONE, this.button, block);
        RecipeProvider.offerStonecuttingRecipe(exporter, RecipeCategory.REDSTONE, this.plate, block);
    }

    @Override
    public void generateBlockTags(BiConsumer<TagKey<Block>, Block> tagConsumer, TagKey<Block>... mineableTags) {
        super.generateBlockTags(tagConsumer, mineableTags);
        tagConsumer.accept(BlockTags.STONE_BUTTONS, this.button);
        tagConsumer.accept(BlockTags.STONE_PRESSURE_PLATES, this.plate);
    }

    @Override
    public void generateItemTags(BiConsumer<TagKey<Item>, ItemConvertible> tagConsumer) {
        super.generateItemTags(tagConsumer);
        tagConsumer.accept(ItemTags.STONE_BUTTONS, this.button);
    }
}
