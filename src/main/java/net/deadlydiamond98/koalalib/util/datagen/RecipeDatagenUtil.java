package net.deadlydiamond98.koalalib.util.datagen;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;


public class RecipeDatagenUtil {
    public static void createStairRecipe(RecipeExporter exporter, Block stair, Block base) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, stair)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .input('#', base)
                .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
                .offerTo(exporter);
    }
}
