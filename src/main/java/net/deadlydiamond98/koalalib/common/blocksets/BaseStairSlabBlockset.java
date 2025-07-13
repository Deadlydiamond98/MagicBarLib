package net.deadlydiamond98.koalalib.common.blocksets;

import net.deadlydiamond98.koalalib.util.datagen.BlockModelDatagenUtil;
import net.deadlydiamond98.koalalib.util.datagen.ItemModelDatagenUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import org.jetbrains.annotations.Nullable;

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

        BlocksetTagLists.SLABS.add(this.slab);
        BlocksetTagLists.STAIRS.add(this.stair);
    }

    @Override
    public void generateModels(BlockStateModelGenerator modelGen, @Nullable SharedModel sharedModel) {
        modelGen.registerSimpleCubeAll(this.base);
        BlockModelDatagenUtil.registerSlab(modelGen, this.slab, this.base);
        BlockModelDatagenUtil.registerStairs(modelGen, this.stair, this.base);
    }

    protected String stripEndS() {
        return this.stripEndS ? "s$" : "";
    }
}