package net.deadlydiamond98.koalalib.common.blocksets;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AbstractBlockset {
    protected final List<Block> blocks = new ArrayList<>();
    private final String baseID;

    /**
     * Used to create blocksets/pallets/etc. with a lot less work!!<br><br>
     * Example of use:
     *
     * <blockquote><pre>
     *     public static final Blockset BLOCKS = new Blockset("modID", "block_name");
     * </pre></blockquote><br><br>
     *
     * @param modID your Mod ID
     * @param id The ID for your block
     */
    public AbstractBlockset(String modID, String id) {
        this.baseID = id;
    }

    /**
     * Call this in your Lang Generator to add translations
     */
    public final void generateTranslations(FabricLanguageProvider.TranslationBuilder translation) {
        this.blocks.forEach(block -> generateTranslation(translation, block));
    }

    /**
     * Used to determine the translation a block uses, can be overriden to change the way it works for some blocks
     */
    protected void generateTranslation(FabricLanguageProvider.TranslationBuilder translation, Block block) {
        translation.add(block, lang(block));
    }

    /**
     * Call this in your Model Generator to add block models
     */
    public final void generateModels(BlockStateModelGenerator modelGen) {
        generateModels(modelGen, null);
    }

    /**
     * Call this in your Model Generator to add block models. This one specifically is used for blocks that share a model file
     */
    public void generateModels(BlockStateModelGenerator modelGen, @Nullable SharedModel sharedModel) {}

    /**
     * Call this in your Loot Table Data Generator to add loot tables
     */
    public final void generateLootTables(FabricBlockLootTableProvider lootTableProvider) {
        this.blocks.forEach(block -> generateLootTable(lootTableProvider, block));
    }

    /**
     * Used to determine the loot table a block uses, can be overriden to add additional cases
     */
    protected void generateLootTable(FabricBlockLootTableProvider lootTableProvider, Block block) {
        if (block instanceof DoorBlock) {
            lootTableProvider.addDrop(block, lootTableProvider.doorDrops(block));
        } else if (block instanceof SlabBlock) {
            lootTableProvider.addDrop(block, lootTableProvider.slabDrops(block));
        } else {
            lootTableProvider.addDrop(block);
        }
    }

    /**
     * Call this to add recipes for the blocks
     */
    public void generateRecipes(Consumer<RecipeJsonProvider> exporter) {}

    /**
     * Used to add all the blocks to appropriate Block Tags
     */
    public void generateBlockTags(BiConsumer<TagKey<Block>, Block> tagConsumer, TagKey<Block>... mineableTags) {
        for (TagKey<Block> minableTag : mineableTags) {
            for (Block block : this.blocks) {
                tagConsumer.accept(minableTag, block);
            }
        }
    }

    /**
     * Used to add all the blocks to appropriate Item Tags
     */
    public void generateItemTags(BiConsumer<TagKey<Item>, ItemConvertible> tagConsumer) {}

    /**
     * Call this in creative tab method to add all the blocks to a creative tab<br><br>
     * The order of blocks in the creative menu is determined by the order the blocks are registered in the constructor
     */
    public final void addToCreative(ItemGroup.Entries entry) {
        this.blocks.forEach(entry::add);
        addToCreative(entry);
    }

    /**
     * Used to add additional Blocks to Creative
     * @param entry
     */
    protected void addAdditionalToCreative(ItemGroup.Entries entry) {}

    /**
     * Get all of the blocks from the blockset as a list
     * @return Returns the full list of blocks
     */
    public final Block[] getAll() {return this.blocks.toArray(Block[]::new);}

    /**
     * Get all of the blocks from the blockset as a list + additional blocks that are supplied as parameters
     * @param blocks Additional Blocks to get with the list
     * @return Returns the full list of blocks + additional blocks that are supplied as parameters
     */
    public final Block[] getAll(Block... blocks) {
        return ArrayUtils.addAll(getAll(), blocks);
    }

    /**
     * Get the id that's used between the different blocks
     * @param regex Optional Regex used to strip characters from the id (used in cases such as bricks, which have the s removed at the end)
     * @return Returns the id that's used between the different blocks
     */
    public final String id(String... regex) {
        String id = this.baseID;
        for (String s : regex) {
            id = id.replaceAll(s, "");
        }
        return id;
    }

    /**
     * Converts a block's id to a lang format
     * @param block Block to get id from
     * @return Returns the lang
     */
    public String lang(Block block) {
        String[] words = Registries.BLOCK.getId(block).getPath().split("_");

        StringBuilder lang = new StringBuilder();
        for (String word : words) {

            if (word.length() <= 1) {
                lang.append(word.toUpperCase()).append(" ");
                continue;
            }
            lang.append(word.toUpperCase().charAt(0)).append(word.substring(1)).append(" ");
        }
        return lang.toString().strip();
    }

    /**
     * Default Register Method for blocks
     * @param modID Your Mod ID
     * @param id The ID of your block
     * @param block The block class that's being registered
     * @return Returns your registered block!
     */
    protected final Block register(String modID, String id, Block block) {
        return register(new Identifier(modID, id), block);
    }

    /**
     * Default Register Method for blocks
     * @param id The Identifier of your block
     * @param block The block class that's being registered
     * @return Returns your registered block!
     */
    protected final Block register(Identifier id, Block block) {
        Block registeredBlock = Registry.register(Registries.BLOCK, id, block);
        registerBlockItem(id, block);
        this.blocks.add(registeredBlock);
        return registeredBlock;
    }

    /**
     * Registers the Item for the block
     * @param id The Identifier of your block
     * @param block The block class that's being registered
     */
    protected final void registerBlockItem(Identifier id, Block block) {
        Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()));
    }

    protected final Item registerItem(Identifier id, Item item) {
        return Registry.register(Registries.ITEM, id, item);
    }

    protected final Block registerNoItem(String modID, String id, Block block) {
        return registerNoItem(new Identifier(modID, id), block);
    }

    protected final Block registerNoItem(Identifier id, Block block) {
        return Registry.register(Registries.BLOCK, id, block);
    }

    @FunctionalInterface
    public interface SharedModel {
        void registerSharedModel(BlockStateModelGenerator blockStateModelGenerator, Block block, String color);
    }
}