package net.deadlydiamond98.koalalib.common.blocksets;

import net.deadlydiamond98.koalalib.util.datagen.BlockModelDatagenUtil;
import net.deadlydiamond98.koalalib.util.datagen.ItemModelDatagenUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.WallBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.recipe.book.RecipeCategory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BaseStairSlabWallBlockset extends BaseStairSlabBlockset {

    public final Block wall;

    public BaseStairSlabWallBlockset(String modID, String id, AbstractBlock.Settings settings) {
        this(modID, id, settings, true);
    }

    public BaseStairSlabWallBlockset(String modID, String id, AbstractBlock.Settings settings, boolean stripEndS) {
        super(modID, id, settings, stripEndS);
        this.wall = register(modID, id(stripEndS()) + "_wall", new WallBlock(settings));
    }

    @Override
    public void generateModels(BlockStateModelGenerator modelGen, boolean uniqueSlab) {
        super.generateModels(modelGen, uniqueSlab);
        BlockModelDatagenUtil.registerWall(modelGen, this.wall, this.base);
    }

    @Override
    public void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        super.generateRecipes(exporter);
        RecipeProvider.offerWallRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, this.wall, this.base);
    }

    @Override
    protected void stoneCutterRecipes(Consumer<RecipeJsonProvider> exporter, Block block) {
        super.stoneCutterRecipes(exporter, block);
        RecipeProvider.offerStonecuttingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, this.wall, block);
    }
}