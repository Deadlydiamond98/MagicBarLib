package net.deadlydiamond98.koalalib.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class OrientableBlockWithEntity extends BlockWithEntity {
    public OrientableBlockWithEntity(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(getFacingProperty(), Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(getFacingProperty(), getPlayerFacing(ctx).getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(getFacingProperty(), rotation.rotate(state.get(getFacingProperty())));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(getFacingProperty())));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(getFacingProperty());
    }

    protected abstract Property<Direction> getFacingProperty();

    private Direction getPlayerFacing(ItemPlacementContext ctx) {
        return getFacingProperty() == Properties.FACING ? ctx.getPlayerLookDirection() : ctx.getHorizontalPlayerFacing();
    }
}
