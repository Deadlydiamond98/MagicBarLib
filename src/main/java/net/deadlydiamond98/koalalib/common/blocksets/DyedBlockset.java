package net.deadlydiamond98.koalalib.common.blocksets;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class DyedBlockset  extends AbstractBlockset {

    public final Block white;
    public final Block light_gray;
    public final Block gray;
    public final Block black;
    public final Block brown;
    public final Block red;
    public final Block orange;
    public final Block yellow;
    public final Block lime;
    public final Block green;
    public final Block cyan;
    public final Block light_blue;
    public final Block blue;
    public final Block purple;
    public final Block magenta;
    public final Block pink;

    public DyedBlockset(String modID, String id, Supplier<Block> block) {
        super(modID, id);

        this.white = register(modID, "white_" + id(), block.get());
        this.light_gray = register(modID, "light_gray_" + id(), block.get());
        this.gray = register(modID, "gray_" + id(), block.get());
        this.black = register(modID, "black_" + id(), block.get());
        this.brown = register(modID, "brown_" + id(), block.get());
        this.red = register(modID, "red_" + id(), block.get());
        this.orange = register(modID, "orange_" + id(), block.get());
        this.yellow = register(modID, "yellow_" + id(), block.get());
        this.lime = register(modID, "lime_" + id(), block.get());
        this.green = register(modID, "green_" + id(), block.get());
        this.cyan = register(modID, "cyan_" + id(), block.get());
        this.light_blue = register(modID, "light_blue_" + id(), block.get());
        this.blue = register(modID, "blue_" + id(), block.get());
        this.purple = register(modID, "purple_" + id(), block.get());
        this.magenta = register(modID, "magenta_" + id(), block.get());
        this.pink = register(modID, "pink_" + id(), block.get());
    }

    @Override
    public void generateModels(BlockStateModelGenerator modelGen, @Nullable SharedModel sharedModel) {
        this.blocks.forEach(block -> {
            String color = getColor(block);
            sharedModel.registerSharedModel(modelGen, block, color);
        });
    }

    private String getColor(Block block) {
        String[] words = Registries.BLOCK.getId(block).getPath().split("_");
        return words[0] + (words[0].contains("light") ? "_" + words[1] : "");
    }
}