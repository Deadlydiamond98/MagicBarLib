package net.deadlydiamond98.koalalib.common.blocksets;

import net.deadlydiamond98.koalalib.util.datagen.BlockModelDatagenUtil;
import net.deadlydiamond98.koalalib.util.datagen.ItemModelDatagenUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.WallBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import org.jetbrains.annotations.Nullable;

public class BaseStairSlabWallBlockset extends BaseStairSlabBlockset {

    public final Block wall;

    public BaseStairSlabWallBlockset(String modID, String id, AbstractBlock.Settings settings) {
        this(modID, id, settings, true);
    }

    public BaseStairSlabWallBlockset(String modID, String id, AbstractBlock.Settings settings, boolean stripEndS) {
        super(modID, id, settings, stripEndS);
        this.wall = register(modID, id(stripEndS()) + "_wall", new WallBlock(settings));
        BlocksetTagLists.WALLS.add(this.wall);
    }

    @Override
    public void generateModels(BlockStateModelGenerator modelGen, @Nullable SharedModel sharedModel) {
        super.generateModels(modelGen, sharedModel);
        BlockModelDatagenUtil.registerWall(modelGen, this.wall, this.base);
    }
}