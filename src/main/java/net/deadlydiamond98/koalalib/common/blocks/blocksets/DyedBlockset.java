package net.deadlydiamond98.koalalib.common.blocks.blocksets;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.item.ItemGroup;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class DyedBlockset {

    public final Block red;
    public final Block orange;
    public final Block yellow;
    public final Block lime;
    public final Block green;
    public final Block light_blue;
    public final Block cyan;
    public final Block blue;
    public final Block purple;
    public final Block magenta;
    public final Block pink;
    public final Block white;
    public final Block gray;
    public final Block light_gray;
    public final Block black;
    public final Block brown;

    /**
     * Allows for the creation of 16 dyed blocks at once! Example:
     *
     * <blockquote><pre>
     *     public static final DyedBlockset DYED_BLOCKS = new DyedBlockset("dyed_block",
     *             () -> new SlabBlock(FabricBlockSettings.copyOf(Blocks.DIRT)),
     *             BlocksTest::registerBlock);
     *
     *     public static Block registerBlock(String blockName, Block block) {
     *         return Registry.register(Registries.BLOCK, new Identifier(KoalaLib.MOD_ID, blockName), block);
     *     }
     * </pre></blockquote><br><br>
     * Using the above as an example, you can acess the blocks like this:
     * <blockquote><pre>
     *     // black can be substituted for any other color
     *     DYED_BLOCKS.black;
     * </pre></blockquote><br><br>
     *
     * @param name The type of block, this will come after the color. For example, if the name were "pot", you would have "red_pot", "orange_pot", "yellow_pot", etc...
     * @param block The block you're creating, all of the blocks will use this for creation
     * @param registerMethod The Registration Method
     */
    public DyedBlockset(String name, Supplier<Block> block, BiFunction<String, Block, Block> registerMethod) {
        this.red = registerMethod.apply("red_" + name, block.get());
        this.orange = registerMethod.apply("orange_" + name, block.get());
        this.yellow = registerMethod.apply("yellow_" + name, block.get());
        this.lime = registerMethod.apply("lime_" + name, block.get());
        this.green = registerMethod.apply("green_" + name, block.get());
        this.light_blue = registerMethod.apply("light_blue_" + name, block.get());
        this.cyan = registerMethod.apply("cyan_" + name, block.get());
        this.blue = registerMethod.apply("blue_" + name, block.get());
        this.purple = registerMethod.apply("purple_" + name, block.get());
        this.magenta = registerMethod.apply("magenta_" + name, block.get());
        this.pink = registerMethod.apply("pink_" + name, block.get());
        this.white = registerMethod.apply("white_" + name, block.get());
        this.gray = registerMethod.apply("gray_" + name, block.get());
        this.light_gray = registerMethod.apply("light_gray_" + name, block.get());
        this.black = registerMethod.apply("black_" + name, block.get());
        this.brown = registerMethod.apply("brown_" + name, block.get());
    }

    /**
     * Call this in a CreativeTabEvent to add all of the blocks at once. The blocks are organized based on how other
     * dyed blocks are organized in vanilla Minecraft.
     */
    public void addDyedBlocksToCreative(ItemGroup.Entries entry) {
        entry.add(this.white);
        entry.add(this.light_gray);
        entry.add(this.gray);
        entry.add(this.black);
        entry.add(this.brown);
        entry.add(this.red);
        entry.add(this.orange);
        entry.add(this.yellow);
        entry.add(this.lime);
        entry.add(this.green);
        entry.add(this.cyan);
        entry.add(this.light_blue);
        entry.add(this.blue);
        entry.add(this.purple);
        entry.add(this.magenta);
        entry.add(this.pink);
    }

    /**
     * Used to get all the blocks as an array
     */
    public Block[] getAll() {
        return new Block[] {
                this.red,
                this.orange,
                this.yellow,
                this.lime,
                this.green,
                this.light_blue,
                this.cyan,
                this.blue,
                this.purple,
                this.magenta,
                this.pink,
                this.white,
                this.light_gray,
                this.gray,
                this.black,
                this.brown
        };
    }

    /**
     * Get a color based on a number
     * @return returns a string of the color
     */
    public String getColor(int i) {
        return switch (i) {
            case 0 -> "red";
            case 1 -> "orange";
            case 2 -> "yellow";
            case 3 -> "lime";
            case 4 -> "green";
            case 5 -> "light_blue";
            case 6 -> "cyan";
            case 7 -> "blue";
            case 8 -> "purple";
            case 9 -> "magenta";
            case 10 -> "pink";
            case 11 -> "white";
            case 12 -> "light_gray";
            case 13 -> "gray";
            case 14 -> "black";
            case 15 -> "brown";
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }

    /**
     * Can be used with datagen to register dyed blocks all at once.
     */
    public void registerBlockModels(BlockStateModelGenerator blockStateModelGenerator, RegisterBlockModel regModel) {
        int i = 0;
        for (Block block : getAll()) {
            String color = getColor(i);
            regModel.registerBlockModel(blockStateModelGenerator, block, color);
            i++;
        }
    }

    /**
     * Used to get all the blocks as an array + any blocks added as args
     */
    public Block[] getAll(Block... block) {
        return ArrayUtils.addAll(getAll(), block);
    }

    @FunctionalInterface
    public interface RegisterBlockModel {
        void registerBlockModel(BlockStateModelGenerator blockStateModelGenerator, Block block, String color);
    }
}