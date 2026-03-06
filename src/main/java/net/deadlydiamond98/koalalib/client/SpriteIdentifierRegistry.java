package net.deadlydiamond98.koalalib.client;

import net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs.ICustomSign;
import net.deadlydiamond98.koalalib.common.blocksets.WoodBlockset;
import net.minecraft.block.Block;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SpriteIdentifierRegistry {
    private static final List<SpriteIdentifier> SPRITE_IDENTIFIERS = new ArrayList<>();

    public static void registerSigns(SpriteIdentifier sprite) {
        SPRITE_IDENTIFIERS.add(sprite);
    }

    public static void registerSigns(Block... blocks) {
        for (Block block : blocks) {
            if (block instanceof ICustomSign customSign) {
                registerSigns(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, customSign.getTexture()));
            }
        }
    }

    public static void registerSigns(WoodBlockset... blocksets) {
        for (WoodBlockset blockset : blocksets) {
            registerSigns(blockset.sign, blockset.hangingSign);
        }
    }

    public static Collection<SpriteIdentifier> getSpriteIdentifiers() {
        return Collections.unmodifiableList(SPRITE_IDENTIFIERS);
    }
}
