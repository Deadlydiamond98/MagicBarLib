package net.deadlydiamond98.koalalib.common.blocks.advancement;

import net.deadlydiamond98.koalalib.init.KoalaLibBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

/**
 * This block is only breakable if the block is placed by a player in survival or the player breaking it has the given advancement
 */
public class AdvancementNeededFacingBlock extends FacingBlock implements IAdvancementNeeded {
    public static final BooleanProperty PLAYERMADE = KoalaLibBlockProperties.PLAYER_MADE_PROPERY;
    private final String advancementID;

    public AdvancementNeededFacingBlock(Settings settings, Identifier advancementID) {
        this(settings, advancementID.toString());
    }

    public AdvancementNeededFacingBlock(Settings settings, String advancementID) {
        super(settings);
        this.advancementID = advancementID;
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.DOWN).with(PLAYERMADE, false));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide()).with(PLAYERMADE, isPlayerPlaced(ctx));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(FACING, mirror.apply(state.get(FACING)));
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        return hasAdvancment(player, this.advancementID) || state.get(PLAYERMADE) ? super.calcBlockBreakingDelta(state, player, world, pos) : -1;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PLAYERMADE, FACING);
    }
}
