package net.deadlydiamond98.koalalib.util.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class BlockModelDatagenUtil {

    // Block Models

    public static void registerSlab(BlockStateModelGenerator blockStateModelGenerator, Block block, Block base) {
        TexturedModel texturedModel = TexturedModel.CUBE_ALL.get(base);
        TextureMap textureMap = texturedModel.getTextures();

        Identifier full = ModelIds.getBlockModelId(base);
        Identifier bottom = Models.SLAB.upload(block, textureMap, blockStateModelGenerator.modelCollector);
        Identifier top = Models.SLAB_TOP.upload(block, textureMap, blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(block, bottom, top, full));
    }

    public static void registerSlabUnique(BlockStateModelGenerator blockStateModelGenerator, Block block, Block base) {
        TextureMap textureMap = TextureMap.sideEnd(TextureMap.getId(block), TextureMap.all(base).getTexture(TextureKey.TOP));

        Identifier full = Models.CUBE_COLUMN.uploadWithoutVariant(block, "_double", textureMap, blockStateModelGenerator.modelCollector);
        Identifier bottom = Models.SLAB.upload(block, textureMap, blockStateModelGenerator.modelCollector);
        Identifier top = Models.SLAB_TOP.upload(block, textureMap, blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(block, bottom, top, full));
    }

    public static void registerStairs(BlockStateModelGenerator blockStateModelGenerator, Block block, Block base) {
        TexturedModel texturedModel = TexturedModel.CUBE_ALL.get(base);
        TextureMap textureMap = texturedModel.getTextures();

        Identifier reg = Models.STAIRS.upload(block, textureMap, blockStateModelGenerator.modelCollector);
        Identifier inner = Models.INNER_STAIRS.upload(block, textureMap, blockStateModelGenerator.modelCollector);
        Identifier outer = Models.OUTER_STAIRS.upload(block, textureMap, blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createStairsBlockState(block, inner, reg, outer));
    }

    public static void registerWall(BlockStateModelGenerator blockModelGenerators, Block block, Block base) {
        TexturedModel texturedModel = TexturedModel.CUBE_ALL.get(base);
        TextureMap textureMap = texturedModel.getTextures();

        Identifier inv = Models.WALL_INVENTORY.uploadWithoutVariant(block, "", textureMap, blockModelGenerators.modelCollector);
        Identifier reg = Models.TEMPLATE_WALL_POST.upload(block, textureMap, blockModelGenerators.modelCollector);
        Identifier side = Models.TEMPLATE_WALL_SIDE.upload(block, textureMap, blockModelGenerators.modelCollector);
        Identifier tall = Models.TEMPLATE_WALL_SIDE_TALL.upload(block, textureMap, blockModelGenerators.modelCollector);

        blockModelGenerators.blockStateCollector.accept(BlockStateModelGenerator.createWallBlockState(block, reg, side, tall));
    }

    public static void registerPillar(BlockStateModelGenerator blockStateModelGenerator, Block block) {
        blockStateModelGenerator.registerAxisRotated(block, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
    }

    public static void registerGlazedTerracottaLike(BlockStateModelGenerator blockStateModelGenerator, Block... blocks) {
        blockStateModelGenerator.registerSouthDefaultHorizontalFacing(TexturedModel.TEMPLATE_GLAZED_TERRACOTTA, blocks);
    }
}
