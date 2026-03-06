package net.deadlydiamond98.koalalib.common.blocksets;

import net.deadlydiamond98.koalalib.util.datagen.RecipeDatagenUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.TagKey;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StoneBricksBlockset extends BaseStairSlabWallBlockset {

    public final BaseStairSlabWallBlockset mossy;
    public final Block cracked;

    public StoneBricksBlockset(String modID, String id, AbstractBlock.Settings settings) {
        this(modID, id, settings, true);
    }

    public StoneBricksBlockset(String modID, String id, AbstractBlock.Settings settings, boolean stripEndS) {
        super(modID, id, settings, stripEndS);
        this.mossy = new BaseStairSlabWallBlockset(modID, "mossy_" + id, settings, stripEndS);
        this.cracked = register(modID, "cracked_" + id, new Block(settings));
    }

    @Override
    public void generateModels(BlockStateModelGenerator modelGen, boolean uniqueSlab) {
        super.generateModels(modelGen, uniqueSlab);
        this.mossy.generateModels(modelGen, uniqueSlab);
        modelGen.registerSimpleCubeAll(this.cracked);
    }

    @Override
    public void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        super.generateRecipes(exporter);
        this.mossy.generateRecipes(exporter);
        RecipeProvider.offerCrackingRecipe(exporter, this.cracked, this.base);
        RecipeDatagenUtil.createMossyRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, this.mossy.base, this.base);
    }

    @Override
    public void generateRecipesStone(Consumer<RecipeJsonProvider> exporter, Block... additionalInputs) {
        super.generateRecipesStone(exporter, additionalInputs);
        this.mossy.generateRecipesStone(exporter);
    }

    @Override
    public void generateBlockTags(BiConsumer<TagKey<Block>, Block> tagConsumer, TagKey<Block>... mineableTags) {
        super.generateBlockTags(tagConsumer, mineableTags);
        this.mossy.generateBlockTags(tagConsumer, mineableTags);
    }

    @Override
    public void generateItemTags(BiConsumer<TagKey<Item>, ItemConvertible> tagConsumer) {
        super.generateItemTags(tagConsumer);
        this.mossy.generateItemTags(tagConsumer);
    }
}
