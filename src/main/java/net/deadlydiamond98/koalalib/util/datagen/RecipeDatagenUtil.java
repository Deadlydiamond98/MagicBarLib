package net.deadlydiamond98.koalalib.util.datagen;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class RecipeDatagenUtil {
    public static void createStairRecipe(Consumer<RecipeJsonProvider> exporter, Block stair, Block base) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, stair)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .input('#', base)
                .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
                .offerTo(exporter);
    }

    public static void createFenceRecipe(Consumer<RecipeJsonProvider> exporter, Block fence, Block base) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, fence, 3)
                .pattern("#s#")
                .pattern("#s#")
                .input('#', base)
                .input('s', Items.STICK)
                .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
                .offerTo(exporter);
    }

    public static void createGateRecipe(Consumer<RecipeJsonProvider> exporter, Block gate, Block base) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, gate, 3)
                .pattern("s#s")
                .pattern("s#s")
                .input('#', base)
                .input('s', Items.STICK)
                .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
                .offerTo(exporter);
    }

    public static void createDoorRecipe(Consumer<RecipeJsonProvider> exporter, Block door, Block base) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, door, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .input('#', base)
                .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
                .offerTo(exporter);
    }

    public static void createTrapdoorRecipe(Consumer<RecipeJsonProvider> exporter, Block door, Block base) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, door)
                .pattern("##")
                .pattern("##")
                .input('#', base)
                .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
                .offerTo(exporter);
    }

    public static void createSignRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible sign, Block base) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, sign, 3)
                .pattern("###")
                .pattern("###")
                .pattern(" s ")
                .input('#', base)
                .input('s', Items.STICK)
                .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
                .offerTo(exporter);
    }

    public static void createHangingSignRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible sign, Block base) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, sign, 6)
                .pattern("c c")
                .pattern("###")
                .pattern("###")
                .input('#', base)
                .input('c', Blocks.CHAIN)
                .criterion(FabricRecipeProvider.hasItem(base), FabricRecipeProvider.conditionsFromItem(base))
                .offerTo(exporter);
    }

    public static void createMossyRecipe(Consumer<RecipeJsonProvider> exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 1)
                .input(input)
                .input(Blocks.VINE)
                .group(category.getName())
                .criterion(FabricRecipeProvider.hasItem(input), FabricRecipeProvider.conditionsFromItem(input))
                .offerTo(exporter, FabricRecipeProvider.convertBetween(output, input) + "_vine");

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 1)
                .input(input)
                .input(Blocks.MOSS_BLOCK)
                .group(category.getName())
                .criterion(FabricRecipeProvider.hasItem(input), FabricRecipeProvider.conditionsFromItem(input))
                .offerTo(exporter, FabricRecipeProvider.convertBetween(output, input) + "_moss");
    }
}
