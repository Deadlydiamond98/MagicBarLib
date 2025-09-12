package net.deadlydiamond98.koalalib.common.blocksets;

import net.deadlydiamond98.koalalib.util.datagen.BlockModelDatagenUtil;
import net.deadlydiamond98.koalalib.util.datagen.RecipeDatagenUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BaseStairSlabBlockset extends AbstractBlockset {
    private final boolean stripEndS;

    public final Block base;
    public final Block slab;
    public final Block stair;

    /**
     * Used to create blocksets/pallets/etc. with a lot less work!!<br><br>
     * This constructor will strip the "s" at the end of the id for stairs and slabs by default (for cases such as bricks, where the slabs and stairs aren't plural)
     *
     * @param modID your Mod ID
     * @param id The ID for your block
     * @param settings Block Settings that blocks will share
     */
    public BaseStairSlabBlockset(String modID, String id, AbstractBlock.Settings settings) {
        this(modID, id, settings, true);
    }

    /**
     * Used to create blocksets/pallets/etc. with a lot less work!!<br><br>
     *
     * @param modID your Mod ID
     * @param id The ID for your block
     * @param settings Block Settings that blocks will share
     * @param stripEndS If true, the "s" at the end of the id will be removed for stairs and slabs (for cases such as bricks, where the slabs and stairs aren't plural)
     */
    public BaseStairSlabBlockset(String modID, String id, AbstractBlock.Settings settings, boolean stripEndS) {
        super(modID, id);
        this.stripEndS = stripEndS;
        this.base = register(modID, id(), new Block(settings));
        this.slab = register(modID, id(stripEndS()) + "_slab", new SlabBlock(settings));
        this.stair = register(modID, id(stripEndS()) + "_stairs", new StairsBlock(this.base.getDefaultState(), settings));
    }

    @Override
    public void generateModels(BlockStateModelGenerator modelGen, @Nullable SharedModel sharedModel) {
        generateModels(modelGen, false);
    }

    /**
     * Call this in your Model Generator to add block models, with the addition of a boolean to determine if the slab
     * uses a unique texture
     */
    public void generateModels(BlockStateModelGenerator modelGen, boolean uniqueSlab) {
        modelGen.registerSimpleCubeAll(this.base);
        if (uniqueSlab) {
            BlockModelDatagenUtil.registerSlabUnique(modelGen, this.slab, this.base);
        } else {
            BlockModelDatagenUtil.registerSlab(modelGen, this.slab, this.base);
        }
        BlockModelDatagenUtil.registerStairs(modelGen, this.stair, this.base);
    }

    @Override
    public void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        super.generateRecipes(exporter);
        RecipeProvider.offerSlabRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, this.slab, this.base);
        RecipeDatagenUtil.createStairRecipe(exporter, this.stair, this.base);
    }

    /**
     * Call this to add recipes for blocks + recipes for Stone Cutting
     * @param exporter
     * @param additionalInputs Blocks that aren't the base block that can be turned into block variants (ex: stone -> stone brick slab)
     */
    public final void generateRecipesStone(Consumer<RecipeJsonProvider> exporter, Block... additionalInputs) {
        generateRecipes(exporter);
        stoneCutterRecipes(exporter, this.base);
        for (Block block : additionalInputs) {
            stoneCutterRecipes(exporter, block);
        }
    }

    /**
     * This creates stone cutting recipes for every block that's passed in.
     * @param exporter
     * @param block Block that is passed in to be cut into variants
     */
    protected void stoneCutterRecipes(Consumer<RecipeJsonProvider> exporter, Block block) {
        if (block != this.base) {
            RecipeProvider.offerStonecuttingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, this.base, block);
        }
        RecipeProvider.offerStonecuttingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, this.stair, block);
        RecipeProvider.offerStonecuttingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, this.slab, block, 2);
    }

    protected String stripEndS() {
        return this.stripEndS ? "s$" : "";
    }
}