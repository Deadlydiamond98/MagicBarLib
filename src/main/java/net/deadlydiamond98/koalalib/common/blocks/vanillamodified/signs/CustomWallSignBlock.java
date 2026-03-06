package net.deadlydiamond98.koalalib.common.blocks.vanillamodified.signs;

import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.util.Identifier;

public class CustomWallSignBlock extends WallSignBlock implements ICustomSign {
    private final Identifier texture;

    public CustomWallSignBlock(Settings settings, WoodType woodType, Identifier texture) {
        super(settings, woodType);
        this.texture = texture;
    }

    @Override
    public WoodType getWoodType() {
        return WoodType.OAK;
    }

    @Override
    public Identifier getTexture() {
        return this.texture;
    }
}
